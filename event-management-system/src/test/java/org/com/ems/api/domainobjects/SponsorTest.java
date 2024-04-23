package org.com.ems.api.domainobjects;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class SponsorTest {

	String[] ignoredFields = new String[] { "updatedTimestamp" };

	@Test
	void testEquals() {

		EqualsVerifier.simple().forClass(Sponsor.class).withIgnoredFields(this.ignoredFields).verify();
	}

}
