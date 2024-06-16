package org.com.ems.services.api;

import java.util.UUID;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.dto.EventDto;
import org.com.ems.services.impl.AttendeeService;

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
