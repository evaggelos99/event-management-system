package com.github.evaggelos99.ems.attendee.service.remote;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface IEventServiceClient {

    /**
     *
     * Adds the Attendee ID to the Event object
     *
     * @param eventId    the event uuid
     * @param attendeeId the attendee uuid
     * @return {@value Boolean#TRUE} if it was sucessfully added to the event object
     *         or else {@value Boolean#FALSE}
     */
    Mono<Boolean> addAttendee(final UUID eventId,
			      final UUID attendeeId);
}
