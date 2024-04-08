package org.com.ems.api.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class SponsorTest {

	String[] ignoredFields = new String[] { "timestamp", "updatedOn" };

	@Test
	void test() {

		EqualsVerifier.simple().forClass(Sponsor.class).withIgnoredFields(this.ignoredFields).verify();
	}

	@Test
	void testHashCode() {

		final UUID randomUUID = UUID.randomUUID();

		final var lhs = new Sponsor(randomUUID, "name", "website", 50_000, new ContactInformation("example@domain.com",
				70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));

		final var rhs = new Sponsor(randomUUID, "name", "website", 50_000, new ContactInformation("example@domain.com",
				70493729392L, "308 Negra Arroyo Lane, Albuquerque, New Mexico."));

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
