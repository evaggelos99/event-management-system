package io.github.evaggelos99.ems.attendee.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class AttendeeTest {

    @Test
    void equalsAndHashcode() {

        EqualsVerifier.simple().forClass(Attendee.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
