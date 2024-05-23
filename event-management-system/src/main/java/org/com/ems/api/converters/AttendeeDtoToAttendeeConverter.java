package org.com.ems.api.converters;

import java.util.function.Function;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.dto.AttendeeDto;
import org.springframework.stereotype.Component;

@Component
public class AttendeeDtoToAttendeeConverter implements Function<AttendeeDto, Attendee> {

    @Override
    public Attendee apply(final AttendeeDto attendeeDto) {

	return new Attendee(attendeeDto.uuid(), attendeeDto.createdAt().toInstant(),
		attendeeDto.lastUpdated().toInstant(), attendeeDto.firstName(), attendeeDto.lastName(),
		attendeeDto.ticketIDs());

    }

}
