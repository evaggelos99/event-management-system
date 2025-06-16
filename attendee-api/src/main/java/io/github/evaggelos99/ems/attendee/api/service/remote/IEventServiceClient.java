package io.github.evaggelos99.ems.attendee.api.service.remote;

import io.github.evaggelos99.ems.common.api.service.remote.IRemoteServiceClient;
import io.github.evaggelos99.ems.ticket.api.TicketDto;
import jakarta.validation.constraints.NotNull;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IEventServiceClient extends IRemoteServiceClient {

    /**
     * Adds the Attendee ID to the Event object
     *
     * @param ticketDto    the ticketDto
     * @param attendeeId the attendee uuid
     * @return {@link Boolean#TRUE} if it was sucessfully added to the event object
     * or else {@link Boolean#FALSE}
     */
    Mono<TicketDto> addAttendee(final TicketDto ticketDto, final UUID attendeeId);

    Mono<Boolean> removeAttendee(UUID uuid, UUID attendeeId);
}
