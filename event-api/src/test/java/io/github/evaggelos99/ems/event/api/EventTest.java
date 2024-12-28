package io.github.evaggelos99.ems.event.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class EventTest {

    @Test
    void test() {

        EqualsVerifier.simple().forClass(Event.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
