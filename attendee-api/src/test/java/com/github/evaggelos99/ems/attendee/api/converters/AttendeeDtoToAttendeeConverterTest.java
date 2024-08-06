package com.github.evaggelos99.ems.attendee.api.converters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.evaggelos99.ems.attendee.api.Attendee;
import com.github.evaggelos99.ems.attendee.api.AttendeeDto;
import com.github.evaggelos99.ems.attendee.api.converters.AttendeeDtoToAttendeeConverter;
import com.github.evaggelos99.ems.attendee.api.util.AttendeeObjectGenerator;

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
