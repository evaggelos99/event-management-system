package com.github.evaggelos99.ems.attendee.api.converters;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import com.github.evaggelos99.ems.attendee.api.Attendee;
import com.github.evaggelos99.ems.attendee.api.AttendeeDto;

@Component
public class AttendeeToAttendeeDtoConverter implements Function<Attendee, AttendeeDto> {

	@Override
	public AttendeeDto apply(final Attendee attendee) {

		return new AttendeeDto(attendee.getUuid(), attendee.getCreatedAt(), attendee.getLastUpdated(),
				attendee.getFirstName(), attendee.getLastName(), attendee.getTicketIDs());

	}

}