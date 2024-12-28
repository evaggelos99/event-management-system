package io.github.evaggelos99.ems.attendee.api.converters;

import io.github.evaggelos99.ems.attendee.api.Attendee;
import io.github.evaggelos99.ems.attendee.api.AttendeeDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class AttendeeDtoToAttendeeConverter implements Function<AttendeeDto, Attendee> {

    @Override
    public Attendee apply(final AttendeeDto attendeeDto) {

        return new Attendee(attendeeDto.uuid(), attendeeDto.createdAt(), attendeeDto.lastUpdated(),
                attendeeDto.firstName(), attendeeDto.lastName(), attendeeDto.ticketIDs());
    }

}
