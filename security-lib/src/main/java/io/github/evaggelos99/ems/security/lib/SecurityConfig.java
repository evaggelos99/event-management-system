package io.github.evaggelos99.ems.security.lib;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityFilterChain(final ServerHttpSecurity http) {

        return http.cors(ServerHttpSecurity.CorsSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(this::setupRequestMatchers2)
                .oauth2ResourceServer(authResourceServer -> authResourceServer
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(converter())))
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .build();
    }

    private void setupRequestMatchers2(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {

        authorizeExchangeSpec.pathMatchers("/event/**").authenticated();
        authorizeExchangeSpec.pathMatchers("/event-stream/**").authenticated();
        authorizeExchangeSpec.pathMatchers("/sponsor/**").authenticated();
        authorizeExchangeSpec.pathMatchers("/attendee/**").authenticated();
        authorizeExchangeSpec.pathMatchers("/organizer/**").authenticated();
        authorizeExchangeSpec.pathMatchers("/ticket/**").authenticated();
        authorizeExchangeSpec.pathMatchers("/user/**").authenticated();
        authorizeExchangeSpec.pathMatchers("/path/**").authenticated();
        authorizeExchangeSpec.pathMatchers("/swagger-ui.html").permitAll();
        authorizeExchangeSpec.pathMatchers("/webjars/**").permitAll();
        authorizeExchangeSpec.pathMatchers("/v3/api-docs/**").permitAll();
    }

    private Converter<Jwt, ? extends Mono<AbstractAuthenticationToken>> converter() {

        final ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                this::getRolesFromJwt);

        return jwtAuthenticationConverter;
    }

    @SuppressWarnings("unchecked")
    private Flux<GrantedAuthority> getRolesFromJwt(final Jwt x) {
        return Flux.fromStream(((List<String>) x.getClaims().get("roles")).stream().map(SimpleGrantedAuthority::new));
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") final String jwkUri) {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkUri).build();
    }
}
