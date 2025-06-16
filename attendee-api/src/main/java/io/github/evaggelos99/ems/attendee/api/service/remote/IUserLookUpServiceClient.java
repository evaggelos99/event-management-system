package io.github.evaggelos99.ems.attendee.api.service.remote;

import io.github.evaggelos99.ems.common.api.service.remote.IRemoteServiceClient;
import io.github.evaggelos99.ems.user.api.UserDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IUserLookUpServiceClient extends IRemoteServiceClient {

    /**
     * Returns the user matching the provided UUID
     *
     * @param id the UUID of the user
     * @return the TicketDto based on the ID
     */
    Mono<UserDto> lookUpUser(final UUID id);

    /**
     * Returns the user matching the provided the entity UUID
     *
     * @param id the UUID of the entity
     * @return the TicketDto based on the ID
     */
    Mono<UserDto> lookUpEntity(final UUID id);
}
