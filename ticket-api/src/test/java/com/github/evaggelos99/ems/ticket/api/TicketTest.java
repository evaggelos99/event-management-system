package com.github.evaggelos99.ems.ticket.api;

import org.junit.jupiter.api.Test;

import com.github.evaggelos99.ems.ticket.api.Ticket;

import nl.jqno.equalsverifier.EqualsVerifier;

class TicketTest {

    @Test
    void test() {

	EqualsVerifier.simple().forClass(Ticket.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
