package org.com.ems.services.api;

import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.dto.AttendeeDto;

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
    boolean addTicket(UUID attendeeId,
		      UUID ticketId);
}
