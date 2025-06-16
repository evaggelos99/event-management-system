package io.github.evaggelos99.ems.user.api.service;

import io.github.evaggelos99.ems.common.api.dto.IdpUserProperties;
import io.github.evaggelos99.ems.user.api.UserDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Interface that should be extended by the identity managers the services support
 */
public interface OnboardingIdentityManagerService {

    Mono<IdpUserProperties> enrollUser(UserDto userDto);

    Mono<IdpUserProperties> editUser(UserDto userDto);

    Mono<IdpUserProperties> getUser(UUID uuid);
}
