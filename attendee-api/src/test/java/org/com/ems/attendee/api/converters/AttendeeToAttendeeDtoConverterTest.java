package org.com.ems.attendee.api.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.com.ems.attendee.api.Attendee;
import org.com.ems.attendee.api.AttendeeDto;
import org.com.ems.attendee.api.util.AttendeeObjectGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttendeeToAttendeeDtoConverterTest {

    AttendeeToAttendeeDtoConverter converter;

    @BeforeEach
    void setUp() {

	this.converter = new AttendeeToAttendeeDtoConverter();

    }

    @Test
    void test() {

	final Attendee expected = AttendeeObjectGenerator.generateAttendee();
	final AttendeeDto actual = this.converter.apply(expected);

	assertEquals(expected.getUuid(), actual.uuid());
	assertEquals(expected.getFirstName(), actual.firstName());
	assertEquals(expected.getTicketIDs(), actual.ticketIDs());

    }

}
