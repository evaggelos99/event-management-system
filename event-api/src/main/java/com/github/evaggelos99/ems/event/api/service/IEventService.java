package com.github.evaggelos99.ems.event.api.service;

import java.util.UUID;

import com.github.evaggelos99.ems.common.api.service.IService;
import com.github.evaggelos99.ems.event.api.Event;
import com.github.evaggelos99.ems.event.api.EventDto;

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
	Mono<Boolean> addAttendee(UUID eventId, UUID attendeeId);

}
