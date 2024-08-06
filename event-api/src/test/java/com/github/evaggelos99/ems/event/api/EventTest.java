package com.github.evaggelos99.ems.event.api;

import org.junit.jupiter.api.Test;

import com.github.evaggelos99.ems.event.api.Event;

import nl.jqno.equalsverifier.EqualsVerifier;

class EventTest {

    @Test
    void test() {

	EqualsVerifier.simple().forClass(Event.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
