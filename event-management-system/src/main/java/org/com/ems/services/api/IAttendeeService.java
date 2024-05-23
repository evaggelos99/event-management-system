package org.com.ems.services.api;

import java.util.UUID;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.dto.AttendeeDto;

public interface IAttendeeService extends IService<Attendee, AttendeeDto> {

    boolean addTicket(UUID attendeeId,
		      UUID ticketId);
}
