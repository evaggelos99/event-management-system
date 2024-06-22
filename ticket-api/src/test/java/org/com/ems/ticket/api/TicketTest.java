package org.com.ems.ticket.api;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TicketTest {

    @Test
    void test() {

	EqualsVerifier.simple().forClass(Ticket.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
