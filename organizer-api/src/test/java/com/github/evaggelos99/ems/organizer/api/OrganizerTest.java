package com.github.evaggelos99.ems.organizer.api;

import org.junit.jupiter.api.Test;

import com.github.evaggelos99.ems.organizer.api.Organizer;

import nl.jqno.equalsverifier.EqualsVerifier;

class OrganizerTest {

    @Test
    void test() {

	EqualsVerifier.simple().forClass(Organizer.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
