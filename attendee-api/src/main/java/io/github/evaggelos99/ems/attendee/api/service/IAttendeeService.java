package io.github.evaggelos99.ems.attendee.api.service;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import io.github.evaggelos99.ems.common.api.service.IService;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IAttendeeService extends IService<Attendee, AttendeeDto> {

    /**
     * This method adds a ticket to the Attendee, cascades and adds the attendee to
     * the {@code Event} as well.
     *
     * @param attendeeId the attendee uuid
     * @param ticketId   the ticket uuid
     * @return {@link Boolean#TRUE} if the action succeeded else {@link Boolean#FALSE}
     */
    Mono<Boolean> addTicket(UUID attendeeId, UUID ticketId);

}
