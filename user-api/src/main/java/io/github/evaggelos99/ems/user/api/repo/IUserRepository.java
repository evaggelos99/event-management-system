package io.github.evaggelos99.ems.user.api.repo;

import io.github.evaggelos99.ems.common.api.db.IRepository;
import io.github.evaggelos99.ems.user.api.User;
import io.github.evaggelos99.ems.user.api.UserDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Users's Repository
 *
 * @author Evangelos Georgiou
 */
public interface IUserRepository extends IRepository<User, UserDto> {

    /**
     * Finds a user by their entity UUID which is the UUID of the object not the user
     * @param entityUuid the UUID of the entity that the user represents, e.g., an attendee, organizer, or sponsor
     * @return {@link Mono} of {@link User} if found, otherwise empty
     */
    Mono<User> findByEntityId(UUID entityUuid);

    /**
     * Adds an entity UUID to a user role.
     * @param role the user role to which the entity UUID will be added
     * @param entityUuid the UUID of the entity that the user represents, e.g., an attendee, organizer, or sponsor
     * @return {@link Mono} of {@link Void} indicating completion
     */
    Mono<Void> addEntityUuid(User role, UUID entityUuid);
}
