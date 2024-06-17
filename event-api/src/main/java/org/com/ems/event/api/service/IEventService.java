package org.com.ems.event.api.service;

import java.util.UUID;

import org.com.ems.common.api.service.IService;
import org.com.ems.event.api.Event;
import org.com.ems.event.api.EventDto;

import reactor.core.publisher.Mono;

public interface IEventService extends IService<Event, EventDto> {

    /**
     * This method is called only from {@link AttendeeService} to add it self to the
     * event.
     *
     * @param eventId
     * @param attendeeId
     *
     * @return {@link Boolean#TRUE} if the action succeeded else
     *         {@link Boolean#FALSE}
     */
    Mono<Boolean> addAttendee(UUID eventId,
			      UUID attendeeId);

}
