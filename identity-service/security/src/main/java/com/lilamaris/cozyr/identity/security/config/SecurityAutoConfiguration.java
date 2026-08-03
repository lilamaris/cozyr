package com.lilamaris.cozyr.identity.security.config;

import com.lilamaris.cozyr.identity.resource.server.filter.IdentityAuthenticationConverter;
import com.lilamaris.cozyr.identity.resource.server.filter.IdentityContextBindingFilter;
import com.lilamaris.cozyr.identity.security.credential.filter.JsonCredentialSignInProcessingFilter;
import com.lilamaris.cozyr.identity.security.credential.filter.JsonCredentialSignUpProcessingFilter;
import com.lilamaris.cozyr.identity.security.credential.provider.CredentialSignInProvider;
import com.lilamaris.cozyr.identity.security.credential.provider.CredentialSignUpProvider;
import com.lilamaris.cozyr.kernel.security.handler.ProblemDetailAccessDeniedHandler;
import com.lilamaris.cozyr.kernel.security.handler.ProblemDetailAuthenticationEntryPoint;
import com.lilamaris.cozyr.kernel.web.response.ProblemDetailFactory;
import com.lilamaris.cozyr.kernel.web.response.ServletJsonResponseWriter;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@AutoConfiguration
@EnableWebSecurity
@EnableMethodSecurity
@ConditionalOnProperty(
        prefix = "cozyr.identity-service.security",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = true
)
@EnableConfigurationProperties(SecurityProperties.class)
public class SecurityAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(SecurityFilterChain.class)
    SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            JwtDecoder jwtDecoder,
            IdentityContextBindingFilter contextBindingFilter,
            IdentityAuthenticationConverter authenticationConverter,
            ObjectProvider<CorsConfigurationSource> corsSourceProvider,
            JsonCredentialSignInProcessingFilter signInProcessingFilter,
            JsonCredentialSignUpProcessingFilter signUpProcessingFilter,
            AuthenticationEntryPoint authenticationEntryPoint,
            AccessDeniedHandler accessDeniedHandler,
            SecurityProperties properties
    ) {
        configureCsrf(http, properties);
        configureCors(http, corsSourceProvider, properties);

        http
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        http
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/.well-known/jwks.json", "/api/v1/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/api/v1/user/**").authenticated()
                        .anyRequest().denyAll()
                )
                .addFilterBefore(signInProcessingFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(signUpProcessingFilter, UsernamePasswordAuthenticationFilter.class);

        http
                .oauth2ResourceServer(
                        oauth2 -> oauth2
                                .jwt(
                                        jwt -> jwt
                                                .decoder(jwtDecoder)
                                                .jwtAuthenticationConverter(authenticationConverter)
                                )
                                .authenticationEntryPoint(authenticationEntryPoint)
                )
                .exceptionHandling(exception -> exception.accessDeniedHandler(accessDeniedHandler))
                .addFilterAfter(contextBindingFilter, BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @ConditionalOnMissingBean
    AuthenticationManager authenticationManager(
            CredentialSignInProvider credentialSignInProvider,
            CredentialSignUpProvider credentialSignUpProvider
    ) {
        return new ProviderManager(List.of(
                credentialSignInProvider,
                credentialSignUpProvider
        ));
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(
            prefix = "cozyr.identity-service.security.cors",
            name = "enabled",
            havingValue = "true",
            matchIfMissing = true
    )
    CorsConfigurationSource corsConfigurationSource(SecurityProperties properties) {
        var cors = properties.cors();

        var configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(cors.allowedOrigins());
        configuration.setAllowedMethods(cors.allowedMethods());
        configuration.setAllowedHeaders(cors.allowedHeaders());
        configuration.setAllowCredentials(cors.allowCredentials());
        configuration.setExposedHeaders(cors.exposedHeaders());
        configuration.validateAllowCredentials();

        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    @ConditionalOnMissingBean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    @ConditionalOnMissingBean
    ServletJsonResponseWriter servletJsonResponseWriter(ObjectMapper objectMapper) {
        return new ServletJsonResponseWriter(objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    ProblemDetailAuthenticationEntryPoint problemDetailAuthenticationEntryPoint(
            ProblemDetailFactory problemDetailFactory,
            ServletJsonResponseWriter responseWriter
    ) {
        return new ProblemDetailAuthenticationEntryPoint(problemDetailFactory, responseWriter);
    }

    @Bean
    @ConditionalOnMissingBean
    ProblemDetailAccessDeniedHandler problemDetailAccessDeniedHandler(
            ProblemDetailFactory problemDetailFactory,
            ServletJsonResponseWriter responseWriter
    ) {
        return new ProblemDetailAccessDeniedHandler(problemDetailFactory, responseWriter);
    }

    private void configureCsrf(HttpSecurity http, SecurityProperties properties) {
        if (properties.csrfEnabled()) http.csrf(Customizer.withDefaults());
        else http.csrf(AbstractHttpConfigurer::disable);
    }

    private void configureCors(HttpSecurity http, ObjectProvider<CorsConfigurationSource> corsSourceProvider, SecurityProperties properties) {
        var cors = properties.cors();
        if (cors.enabled()) {
            var corsSource = Optional.ofNullable(corsSourceProvider.getIfAvailable())
                    .orElseThrow(() -> new IllegalStateException("""
                            CORS is enabled but CorsConfigurationSource bean is not available.
                            Check capstone.bootstrap.security.cors.enabled or define a CorsConfigurationSource bean.
                            """));
            http.cors(configurer -> configurer.configurationSource(corsSource));
        } else {
            http.cors(AbstractHttpConfigurer::disable);
        }
    }
}
