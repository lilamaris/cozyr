package com.lilamaris.cozyr.identity.provider.config;

import com.lilamaris.cozyr.identity.contract.provider.ServiceDescriptor;
import com.lilamaris.cozyr.identity.contract.provider.ServiceScopeRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ServiceLoader;

@Configuration
public class ScopeProviderConfiguration {
    @Bean
    ServiceScopeRegistry serviceScopeRegistry() {
        var providers = ServiceLoader.load(ServiceDescriptor.class).stream()
                .map(ServiceLoader.Provider::get)
                .toList();
        return new ServiceScopeRegistry(providers);
    }
}
