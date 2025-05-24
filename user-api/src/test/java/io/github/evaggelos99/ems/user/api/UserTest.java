package io.github.evaggelos99.ems.user.api;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class UserTest {

    @Test
    void test() {

        EqualsVerifier.simple().forClass(User.class).withIgnoredFields("createdAt", "lastUpdated").verify();
    }

}
