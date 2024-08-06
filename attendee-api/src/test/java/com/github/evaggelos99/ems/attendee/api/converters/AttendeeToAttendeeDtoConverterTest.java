package com.github.evaggelos99.ems.attendee.api.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.evaggelos99.ems.attendee.api.Attendee;
import com.github.evaggelos99.ems.attendee.api.AttendeeDto;
import com.github.evaggelos99.ems.attendee.api.converters.AttendeeToAttendeeDtoConverter;
import com.github.evaggelos99.ems.attendee.api.util.AttendeeObjectGenerator;

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
