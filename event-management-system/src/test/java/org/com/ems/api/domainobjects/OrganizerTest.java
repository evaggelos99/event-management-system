package org.com.ems.api.domainobjects;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class OrganizerTest {

	String[] ignoredFields = new String[] { "updatedTimestamp" };

	@Test
	void testEquals() {

		EqualsVerifier.forClass(Organizer.class).withIgnoredFields(this.ignoredFields).verify();
	}

}
