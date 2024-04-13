package org.com.ems.api.domainobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.com.ems.api.domainobjects.ContactInformation;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class ContactInformationTest {

	@Test
	void testEquals() {

		EqualsVerifier.forClass(ContactInformation.class).verify();
	}

	@Test
	void testHashCode() {

		final var lhs = new ContactInformation("example@domain.com", 70493729392L,
				"308 Negra Arroyo Lane, Albuquerque, New Mexico.");
		final var rhs = new ContactInformation("example@domain.com", 70493729392L,
				"308 Negra Arroyo Lane, Albuquerque, New Mexico.");

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}
}
