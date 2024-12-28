package io.github.evaggelos99.ems.organizer.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class OrganizerTest {

    @Test
    void test() {

        EqualsVerifier.simple().forClass(Organizer.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
