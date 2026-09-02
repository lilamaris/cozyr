package com.lilamaris.cozyr.identity.contract.provider;

import com.lilamaris.cozyr.identity.contract.schema.Scope;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceScopeRegistry {
    private final Map<String, Scope> serviceScopeRegistry;

    public ServiceScopeRegistry(List<ServiceDescriptor> descriptors) {
        Map<String, Scope> registry = new HashMap<>();

        descriptors.forEach(descriptor -> {
            var scope = descriptor.defaultScope();

            var key = keyOf(scope);
            if (registry.putIfAbsent(key, scope) != null)
                throw new IllegalArgumentException("Duplicated service found: " + key);
        });

        this.serviceScopeRegistry = Map.copyOf(registry);
    }

    public List<Scope> getAllRoles() {
        return List.copyOf(serviceScopeRegistry.values());
    }

    private String keyOf(Scope scope) {
        return scope.service();
    }
}
