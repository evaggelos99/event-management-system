package io.github.evaggelos99.ems.sponsor.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class SponsorTest {

    @Test
    void test() {

        EqualsVerifier.simple().forClass(Sponsor.class).withIgnoredFields("createdAt", "lastUpdated").verify();

    }

}
