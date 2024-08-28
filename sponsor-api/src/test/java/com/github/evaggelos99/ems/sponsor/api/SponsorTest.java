package com.github.evaggelos99.ems.sponsor.api;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class SponsorTest {

	@Test
	void test() {

		EqualsVerifier.simple().forClass(Sponsor.class).withIgnoredFields("createdAt", "lastUpdated").verify();

	}

}
