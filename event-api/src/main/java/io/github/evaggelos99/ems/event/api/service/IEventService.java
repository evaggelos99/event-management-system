package io.github.evaggelos99.ems.event.api.service;

import io.github.evaggelos99.ems.common.api.service.IService;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.EventDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IEventService extends IService<Event, EventDto> {

    /**
     * This method is called only from {@link IEventService} implementor to add it self to the
     * event.
     *
     * @param eventId
     * @param attendeeId
     * @return {@link Boolean#TRUE} if the action succeeded else
     * {@link Boolean#FALSE}
     */
    Mono<Boolean> addAttendee(UUID eventId, UUID attendeeId);

}
