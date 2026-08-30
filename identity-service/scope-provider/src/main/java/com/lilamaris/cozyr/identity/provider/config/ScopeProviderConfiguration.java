package com.lilamaris.cozyr.identity.provider.config;

import com.lilamaris.cozyr.identity.contract.provider.ServiceScopeProvider;
import com.lilamaris.cozyr.identity.contract.provider.ServiceScopeRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ServiceLoader;

@Configuration
public class ScopeProviderConfiguration {
    @Bean
    ServiceScopeRegistry serviceScopeRegistry() {
        var providers = ServiceLoader.load(ServiceScopeProvider.class).stream()
                .map(ServiceLoader.Provider::get)
                .toList();
        return new ServiceScopeRegistry(providers);
    }
}
