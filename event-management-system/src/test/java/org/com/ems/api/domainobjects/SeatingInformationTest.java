package org.com.ems.api.domainobjects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.com.ems.api.domainobjects.SeatingInformation;
import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class SeatingInformationTest {

	@Test
	void testEquals() {

		EqualsVerifier.forClass(SeatingInformation.class).verify();
	}

	@Test
	void testHashCode() {

		final var lhs = new SeatingInformation("AB", "NORTH");
		final var rhs = new SeatingInformation("AB", "NORTH");

		assertEquals(lhs, rhs);
		assertEquals(lhs.hashCode(), rhs.hashCode());
	}

}
