package org.com.ems.event.api;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class EventTest {

    @Test
    void test() {

	EqualsVerifier.simple().forClass(Event.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
