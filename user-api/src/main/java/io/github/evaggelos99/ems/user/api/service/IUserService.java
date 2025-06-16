package io.github.evaggelos99.ems.user.api.service;

import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IUserService extends IService<User, UserDto> {

    /**
     * Retrieve a User entity by its ID.
     * @param id the UUID of the User entity
     * @return {@link Mono} containing the User entity if found, otherwise empty.
     */
    Mono<User> getEntity(UUID id);

    /**
     * Add a User entity to the system.
     * @param role the User entity to be added
     * @param entityUuid the UUID of the entity to which the User will be added
     * @return {@link Mono} that completes when the User entity is added.
     */
    Mono<Void> addEntity(User role, UUID entityUuid);

}