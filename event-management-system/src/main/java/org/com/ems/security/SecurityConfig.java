package org.com.ems.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.proc.DefaultJOSEObjectTypeVerifier;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.proc.ConfigurableJWTProcessor;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

	http.cors(Customizer.withDefaults());

	http.csrf(Customizer.withDefaults())//
		.authorizeHttpRequests(x -> x.requestMatchers("/swagger-ui/**").permitAll()//
			.requestMatchers("/event").hasAnyRole("USER")//
			.requestMatchers("/sponsor").hasAnyRole("USER")//
			.requestMatchers("/attendee").hasAnyRole("USER")//
			.requestMatchers("/organizer").hasAnyRole("USER")//
			.requestMatchers("/ticket").hasAnyRole("USER")//
			.requestMatchers("/v3/api-docs/**").permitAll());

	http.oauth2ResourceServer(i -> i.jwt(x -> x.jwtAuthenticationConverter(this.converter())));

	return http.build();

    }

    JwtDecoder jwtDecoder(@Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") final String uri) {

	return NimbusJwtDecoder.withJwkSetUri(uri).jwtProcessorCustomizer(this::addJwtTypeVerifier).build();

    }

    private void addJwtTypeVerifier(final ConfigurableJWTProcessor<SecurityContext> customizer) {

	customizer.setJWSTypeVerifier(new DefaultJOSEObjectTypeVerifier<>(new JOSEObjectType("jwt")));

    }

    @SuppressWarnings("unchecked")
    private Converter<Jwt, ? extends AbstractAuthenticationToken> converter() {

	final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
	jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
		x -> ((List<String>) x.getClaims().get("roles")).stream().map(this::mapToAuthority).toList());

	return jwtAuthenticationConverter;

    }

    private GrantedAuthority mapToAuthority(final String i) {

	return () -> i;

    }

}
