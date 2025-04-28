package io.github.evaggelos99.ems.security.lib;

import io.github.evaggelos99.ems.user.api.UserDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Interface that should be extended by the identity managers the services support
 */
public interface IOnboardingIdentityManagerService {

    Mono<IdentityUserDto> enrollUser(UserDto userDto);

    Mono<IdentityUserDto> editUser(UserDto userDto);

    Mono<IdentityUserDto> getUser(UUID uuid);
}
