package io.github.evaggelos99.ems.ticket.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class TicketTest {

    @Test
    void test() {

        EqualsVerifier.simple().forClass(Ticket.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
