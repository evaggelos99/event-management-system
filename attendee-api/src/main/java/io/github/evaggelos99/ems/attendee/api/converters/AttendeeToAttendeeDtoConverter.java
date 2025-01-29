package io.github.evaggelos99.ems.attendee.api.converters;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AttendeeToAttendeeDtoConverter implements Function<Attendee, AttendeeDto> {

    @Override
    public AttendeeDto apply(final Attendee attendee) {

        return AttendeeDto.builder()
                .uuid(attendee.getUuid())
                .createdAt(attendee.getCreatedAt())
                .lastUpdated(attendee.getLastUpdated())
                .firstName(attendee.getFirstName())
                .lastName(attendee.getLastName())
                .ticketIDs(attendee.getTicketIDs())
                .build();
    }

}