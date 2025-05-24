package io.github.evaggelos99.ems.user.api.util;

import io.github.evaggelos99.ems.common.api.domainobjects.TicketType;

import java.util.List;
import java.util.Random;

public final class UserObjectGenerator {

    private static final List<TicketType> ALL_TICKET_TYPES = List.of(TicketType.values());
    private static final Random RANDOM = new Random();

    private UserObjectGenerator() {

    }

}
