package io.github.evaggelos99.ems.security.lib;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

public class SecurityContextHelper {

    /**
     * Helper method that checks the authentication object
     * and compares it with the roles that are passed as parameters
     *
     * @param roles to be compared against
     * @return {@link Boolean#TRUE} if all the roles passed are in the authentication object
     * otherwise {@link Boolean#FALSE}
     */
    public static Mono<Boolean> filterRoles(Roles... roles) {

        final List<String> listOfRoles = Arrays.stream(roles).map(Enum::name).toList();

        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> Flux.fromIterable(listOfRoles)
                        .map(grantedAuthority -> auth.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .anyMatch(authority -> authority.equals(grantedAuthority))
                        )
                        .all(result -> result));
    }

}
