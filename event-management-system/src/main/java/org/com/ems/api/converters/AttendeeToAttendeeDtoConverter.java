package org.com.ems.api.converters;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.function.Function;

import org.com.ems.api.domainobjects.Attendee;
import org.com.ems.api.dto.AttendeeDto;
import org.springframework.stereotype.Component;

@Component
public class AttendeeToAttendeeDtoConverter implements Function<Attendee, AttendeeDto> {

    @Override
    public AttendeeDto apply(final Attendee attendee) {

	return new AttendeeDto(attendee.getUuid(), this.convertToTimeStamp(attendee.getLastUpdated()),
		attendee.getFirstName(), attendee.getLastName(), attendee.getTicketIDs());

    }

    private Timestamp convertToTimeStamp(final Instant lastUpdated) {

	return Timestamp.from(lastUpdated);

    }

}