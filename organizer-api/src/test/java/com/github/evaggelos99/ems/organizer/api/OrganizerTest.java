package com.github.evaggelos99.ems.organizer.api;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class OrganizerTest {

	@Test
	void test() {

		EqualsVerifier.simple().forClass(Organizer.class).withIgnoredFields("createdAt", "lastUpdated").verify();

	}

}
