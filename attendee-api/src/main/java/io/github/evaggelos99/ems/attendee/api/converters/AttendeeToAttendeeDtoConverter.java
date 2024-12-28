package io.github.evaggelos99.ems.attendee.api.converters;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AttendeeToAttendeeDtoConverter implements Function<Attendee, AttendeeDto> {

    @Override
    public AttendeeDto apply(final Attendee attendee) {

        return new AttendeeDto(attendee.getUuid(), attendee.getCreatedAt(), attendee.getLastUpdated(),
                attendee.getFirstName(), attendee.getLastName(), attendee.getTicketIDs());

    }

}