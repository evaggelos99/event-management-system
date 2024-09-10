package io.github.evaggelos99.ems.attendee.api.converters;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;

@Component
public class AttendeeDtoToAttendeeConverter implements Function<AttendeeDto, Attendee> {

	@Override
	public Attendee apply(final AttendeeDto attendeeDto) {

		return new Attendee(attendeeDto.uuid(), attendeeDto.createdAt(), attendeeDto.lastUpdated(),
				attendeeDto.firstName(), attendeeDto.lastName(), attendeeDto.ticketIDs());

	}

}
