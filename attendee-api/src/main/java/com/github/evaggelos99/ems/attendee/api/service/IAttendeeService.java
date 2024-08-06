package com.github.evaggelos99.ems.attendee.api.service;

import java.util.UUID;

import com.github.evaggelos99.ems.attendee.api.Attendee;
import com.github.evaggelos99.ems.attendee.api.AttendeeDto;
import com.github.evaggelos99.ems.common.api.service.IService;

import reactor.core.publisher.Mono;

public interface IAttendeeService extends IService<Attendee, AttendeeDto> {

    /**
     * This method adds a ticket to the Attendee, cascades and adds the attendee to
     * the {@link Event} as well.
     *
     * @param attendeeId
     * @param ticketId
     *
     * @return {@link Boolean#TRUE} if the action succeeded else
     *         {@link Boolean#FALSE}
     */
    Mono<Boolean> addTicket(UUID attendeeId,
			    UUID ticketId);
}
