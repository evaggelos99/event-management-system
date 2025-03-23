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
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    public static final String USER = "ROLE_USER";

    @Bean
    SecurityWebFilterChain securityFilterChain(final ServerHttpSecurity http) {

        return http.cors(Customizer.withDefaults())
            .csrf(Customizer.withDefaults())
            .authorizeExchange(this::setupRequestMatchers2)
            .oauth2ResourceServer(outhResourceServer -> outhResourceServer
                .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(this.converter())))
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
        .formLogin(ServerHttpSecurity.FormLoginSpec::disable).build();
    }

    private void setupRequestMatchers2(ServerHttpSecurity.AuthorizeExchangeSpec authorizeExchangeSpec) {
        authorizeExchangeSpec.pathMatchers("/event").hasAuthority(USER);
        authorizeExchangeSpec.pathMatchers("/sponsor").hasAuthority(USER);
        authorizeExchangeSpec.pathMatchers("/attendee").hasAuthority(USER);
        authorizeExchangeSpec.pathMatchers("/organizer").hasAuthority(USER);
        authorizeExchangeSpec.pathMatchers("/ticket").hasAuthority(USER);
        authorizeExchangeSpec.pathMatchers("/swagger-ui.html").permitAll();
        authorizeExchangeSpec.pathMatchers("/webjars/**").permitAll();
        authorizeExchangeSpec.pathMatchers("/v3/api-docs/**").permitAll();
    }

    @SuppressWarnings("unchecked")
    private Converter<Jwt, ? extends Mono<AbstractAuthenticationToken>> converter() {
        final ReactiveJwtAuthenticationConverter jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                this::getRolesFromJwt);

        return jwtAuthenticationConverter;
    }

    private Flux<GrantedAuthority> getRolesFromJwt(final Jwt x) {
        return Flux.fromStream(((List<String>) x.getClaims().get("roles")).stream().map(this::mapToAuthority));
    }

    private GrantedAuthority mapToAuthority(final String i) {
        return () -> i;
    }

    @Bean
    ReactiveJwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") final String jwkUri) {
        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkUri).build();
    }
}
