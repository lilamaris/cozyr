package com.lilamaris.cozyr.identity.io;

import com.lilamaris.cozyr.identity.application.model.RSAKeyPair;
import com.lilamaris.cozyr.identity.application.port.out.JwksReader;
import com.lilamaris.cozyr.identity.io.config.FileIOJwksProperties;
import com.lilamaris.cozyr.kernel.core.condition.ObjectPrecondition;
import com.lilamaris.cozyr.kernel.core.condition.StringPrecondition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Function;
import java.util.stream.Collectors;

public class JwksReaderIOAdapter implements JwksReader {
    private static final String PUBLIC_KEY_FILENAME = "public.pem";
    private static final String PRIVATE_KEY_FILENAME = "private.pem";

    private final String activeSignableKid;
    private final String keyBaseLocation;
    private final Map<String, RSAKeyPair> keyMap;

    public JwksReaderIOAdapter(FileIOJwksProperties properties, ResourceLoader loader) {
        ObjectPrecondition.requireNonNull(properties, "properties");
        this.activeSignableKid = StringPrecondition.requireNonBlank(properties.activeSignableKid(), "activeSignableKid");
        this.keyBaseLocation = StringPrecondition.requireNonBlank(properties.keyBaseLocation(), "keyBaseLocation");

        this.keyMap = properties.keys().stream()
                .map(kid -> loadKey(kid, loader))
                .collect(Collectors.toUnmodifiableMap(
                        RSAKeyPair::kid,
                        Function.identity()
                ));
    }

    @Override
    public RSAKeyPair findSignableKey() {
        var key = keyMap.get(activeSignableKid);
        if (key == null) throw new NoSuchElementException("No active signable key exists. kid=" + activeSignableKid);
        return key;
    }

    @Override
    public List<RSAKeyPair> findVerifiableKeys() {
        return keyMap.values().stream().toList();
    }

    private RSAKeyPair loadKey(String kid, ResourceLoader resourceLoader) {
        RSAPublicKey publicKey;
        RSAPrivateKey privateKey = null;
        var publicKeyLocation = keyLocation(kid, PUBLIC_KEY_FILENAME);
        var privateKeyLocation = keyLocation(kid, PRIVATE_KEY_FILENAME);

        try {
            var publicKeyResource = resourceLoader.getResource(publicKeyLocation);
            publicKey = readPublicKey(publicKeyResource);

            var privateKeyResource = resourceLoader.getResource(privateKeyLocation);
            if (privateKeyResource.exists()) {
                privateKey = readPrivateKey(privateKeyResource);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Failed to load key. kid=%s, publicKey location=%s, privateKey location=%s".formatted(kid, publicKeyLocation, privateKeyLocation), e);
        }

        return RSAKeyPair.signable(kid, publicKey, privateKey);
    }

    private String keyLocation(String kid, String filename) {
        return keyBaseLocation + kid + "/" + filename;
    }

    private RSAPrivateKey readPrivateKey(Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        key = key
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    private RSAPublicKey readPublicKey(Resource resource) throws Exception {
        String key = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        key = key
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] decoded = Base64.getDecoder().decode(key);
        return (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
    }
}
