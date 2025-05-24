package io.github.evaggelos99.ems.event.api.service;

import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.api.EventStream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IEventService extends IService<Event, EventDto> {

    /**
     * This method is used for adding an attendee to the event
     *
     * @param eventId the UUID of the Event
     * @param attendeeId the UUID of the Attendee
     * @return {@link Boolean#TRUE} if the action succeeded else
     * {@link Boolean#FALSE}
     */
    Mono<Boolean> addAttendee(UUID eventId, UUID attendeeId);

    /**
     * This method is used for removing an attendee from the event
     *
     * @param eventId the UUID of the Event
     * @param attendeeId the UUID of the Attendee
     * @return {@link Boolean#TRUE} if the action succeeded else
     * {@link Boolean#FALSE}
     */
    Mono<Boolean> removeAttendee(UUID eventId, UUID attendeeId);

    /**
     * This method is used for adding a sponsor to the event
     *
     * @param eventId the UUID of the Event
     * @param sponsorId the UUID of the Sponsor
     * @return {@link Boolean#TRUE} if the action succeeded else
     * {@link Boolean#FALSE}
     */
    Mono<Boolean> addSponsor(UUID eventId, UUID sponsorId);

    /**
     * This method is used for removing a sponsor from the event
     *
     * @param eventId the UUID of the Event
     * @param sponsorId the UUID of the Sponsor
     * @return {@link Boolean#TRUE} if the action succeeded else
     * {@link Boolean#FALSE}
     */
    Mono<Boolean> removeSponsor(UUID eventId, UUID sponsorId);

    /**
     * This method is used to retrieve ALL {@link EventStream}'s of an Event
     *
     * @param eventId the UUID of the Event
     *
     * @return ALL the events that a streamable event emitted
     */
    Flux<EventStream> getEventStreams(UUID eventId);

}
