package com.lilamaris.cozyr.identity.provider;

import com.lilamaris.cozyr.identity.application.port.out.ServiceScopeReader;
import com.lilamaris.cozyr.identity.contract.provider.ServiceScopeRegistry;
import com.lilamaris.cozyr.identity.contract.schema.Scope;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ServiceScopeReaderRegistryAdapter implements ServiceScopeReader {
    private final ServiceScopeRegistry registry;

    @Override
    public List<Scope> getAllScopes() {
        return registry.getAllRoles();
    }
}
