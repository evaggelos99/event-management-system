package org.com.ems.attendee.api.converters;

import java.util.function.Function;

import org.com.ems.attendee.api.Attendee;
import org.com.ems.attendee.api.AttendeeDto;
import org.springframework.stereotype.Component;

@Component
public class AttendeeToAttendeeDtoConverter implements Function<Attendee, AttendeeDto> {

	@Override
	public AttendeeDto apply(final Attendee attendee) {

		return new AttendeeDto(attendee.getUuid(), attendee.getCreatedAt(), attendee.getLastUpdated(),
				attendee.getFirstName(), attendee.getLastName(), attendee.getTicketIDs());

	}

}