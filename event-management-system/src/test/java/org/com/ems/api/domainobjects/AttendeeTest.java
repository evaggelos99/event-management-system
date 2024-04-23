package org.com.ems.api.domainobjects;

import java.util.List;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class AttendeeTest {

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Attendee.class).verify();
	}

	@Test
	void testEquals2() {

		final Attendee attendee = Attendee.builder().firstName("firstname").lastName("lastname").ticketsIDs(List.of())
				.build();

		final Attendee attendee2 = Attendee.builder().firstName("firstname").lastName("lastname").ticketsIDs(List.of())
				.build();

		System.out.println(attendee.equals(attendee2));
	}

}
