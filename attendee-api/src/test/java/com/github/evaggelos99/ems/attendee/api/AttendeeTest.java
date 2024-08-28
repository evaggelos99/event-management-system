package com.github.evaggelos99.ems.attendee.api;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class AttendeeTest {

	@Test
	void equalsAndHashcode() {

		EqualsVerifier.simple().forClass(Attendee.class).withIgnoredFields("createdAt", "lastUpdated").verify();

	}

}
