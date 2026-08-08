package com.lilamaris.cozyr.identity.io;

import com.lilamaris.cozyr.identity.application.model.RSAKeyPair;
import com.lilamaris.cozyr.identity.application.port.out.JwksReader;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JwksReaderIOAdapter implements JwksReader {
    private final String activeSignableKid;
    private final Path keyBasePath;
    private final Map<String, RSAKeyPair> keyMap;

    public JwksReaderIOAdapter(Path keyBasePath, String activeSignableKid, String publicKeyPrefix, String privateKeyPrefix) {
        this.keyBasePath = ObjectPrecondition.requireNonNull(keyBasePath, "keyBasePath");
        this.activeSignableKid = StringPrecondition.requireNonBlank(activeSignableKid, "activeSignableKid");
        publicKeyPrefix = StringPrecondition.requireNonBlank(publicKeyPrefix, "publicKeyPrefix");
        privateKeyPrefix = StringPrecondition.requireNonBlank(privateKeyPrefix, "privateKeyPrefix");

        this.keyMap = loadKeys(keyBasePath, publicKeyPrefix, privateKeyPrefix).stream().collect(
                Collectors.toUnmodifiableMap(
                        RSAKeyPair::kid,
                        Function.identity()
                )
        );

        if (!keyMap.containsKey(activeSignableKid)) {
            throw new IllegalStateException(
                    "No active signable key exists. activeSignableKid=%s, availableKids=%s, keyBasePath=%s"
                            .formatted(activeSignableKid, availableKids(), this.keyBasePath)
            );
        }
    }

    @Override
    public RSAKeyPair findSignableKey() {
        var key = keyMap.get(activeSignableKid);
        if (key == null) {
            throw new NoSuchElementException(
                    "No active signable key exists. activeSignableKid=%s, availableKids=%s, keyBasePath=%s"
                            .formatted(activeSignableKid, availableKids(), keyBasePath)
            );
        }
        return key;
    }

    @Override
    public List<RSAKeyPair> findVerifiableKeys() {
        return keyMap.values().stream().toList();
    }

    private List<RSAKeyPair> loadKeys(Path keyBasePath, String publicKeyPrefix, String privateKeyPrefix) {
        if (!Files.exists(keyBasePath)) {
            throw new IllegalStateException(
                    "JWKS key base directory does not exist. keyBasePath=%s"
                            .formatted(keyBasePath)
            );
        }
        if (!Files.isDirectory(keyBasePath)) {
            throw new IllegalStateException(
                    "JWKS key base path is not a directory. keyBasePath=%s"
                            .formatted(keyBasePath)
            );
        }

        try (var directories = Files.list(keyBasePath)) {
            var keyPairs = directories
                    .filter(Files::isDirectory)
                    .map(keyEntryPath -> loadKeyPair(keyEntryPath, publicKeyPrefix, privateKeyPrefix))
                    .toList();
            if (keyPairs.isEmpty()) {
                throw new IllegalStateException(
                        "No JWKS key directories found. keyBasePath=%s"
                                .formatted(keyBasePath)
                );
            }
            return keyPairs;
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to list JWKS key directories. keyBasePath=%s"
                            .formatted(keyBasePath),
                    e
            );
        }
    }

    private List<String> availableKids() {
        return keyMap.keySet().stream().sorted().toList();
    }

    private RSAKeyPair loadKeyPair(Path keyEntryPath, String publicKeyPrefix, String privateKeyPrefix) {
        var publicKeyPath = keyEntryPath.resolve(publicKeyPrefix);
        var privateKeyPath = keyEntryPath.resolve(privateKeyPrefix);
        var kid = keyEntryPath.getFileName().toString();

        try {
            var publicKey = readPublicKeyByte(publicKeyPath);
            var privateKey = readPrivateKeyByte(privateKeyPath);

            return RSAKeyPair.signable(kid, publicKey, privateKey);
        } catch (IOException e) {
            throw new IllegalStateException(
                    "Failed to read JWKS key files. kid=%s, publicKeyPath=%s, privateKeyPath=%s"
                            .formatted(kid, publicKeyPath, privateKeyPath),
                    e
            );
        } catch (InvalidKeySpecException | IllegalArgumentException e) {
            throw new IllegalStateException(
                    "Failed to parse JWKS key files. kid=%s, publicKeyPath=%s, privateKeyPath=%s, expectedPublicKeyFormat=%s, expectedPrivateKeyFormat=%s"
                            .formatted(kid, publicKeyPath, privateKeyPath, "X.509 PEM", "PKCS#8 PEM"),
                    e
            );
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("RSA algorithm is not available while loading JWKS key. kid=" + kid, e);
        } catch (IllegalStateException e) {
            throw new IllegalStateException(
                    "Failed to validate JWKS key pair. kid=%s, publicKeyPath=%s, privateKeyPath=%s"
                            .formatted(kid, publicKeyPath, privateKeyPath),
                    e
            );
        }
    }

    private RSAPrivateKey readPrivateKeyByte(Path path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var raw = Files.readString(path, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        var keyByte = Base64.getDecoder().decode(raw);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(keyByte));
    }

    private RSAPublicKey readPublicKeyByte(Path path) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        var raw = Files.readString(path, StandardCharsets.UTF_8)
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        var keyByte = Base64.getDecoder().decode(raw);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(keyByte));
    }
}
