package org.com.ems.attendee.api.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.com.ems.attendee.api.Attendee;
import org.com.ems.attendee.api.AttendeeDto;
import org.com.ems.attendee.api.util.AttendeeObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttendeeDtoToAttendeeConverterTest {

    AttendeeDtoToAttendeeConverter converter;

    @BeforeEach
    void setUp() {

	this.converter = new AttendeeDtoToAttendeeConverter();

    }

    @Test
    void apply_givenRandomAttendeeDtoObject_expectFieldsToMatchTheGivenObject() {

	final AttendeeDto expectAttendeeDto = AttendeeObjectGenerator.generateAttendeeDto();
	final Attendee actual = this.converter.apply(expectAttendeeDto);

	assertEquals(expectAttendeeDto.uuid(), actual.getUuid());
	assertEquals(expectAttendeeDto.firstName(), actual.getFirstName());
	assertEquals(expectAttendeeDto.ticketIDs(), actual.getTicketIDs());

    }

}
