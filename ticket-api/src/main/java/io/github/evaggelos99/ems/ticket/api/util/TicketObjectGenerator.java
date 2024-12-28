package io.github.evaggelos99.ems.ticket.api.util;

import io.github.evaggelos99.ems.common.api.domainobjects.SeatingInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.TicketType;
import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;

import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class TicketObjectGenerator {

    private static final List<TicketType> ALL_TICKET_TYPES = List.of(TicketType.values());
    private static final Random RANDOM = new Random();

    private TicketObjectGenerator() {

    }

    public static TicketDto generateTicketDto(final UUID ticketId, final UUID eventId) {

        final TicketType randomTicketType = ALL_TICKET_TYPES.get(RANDOM.nextInt(ALL_TICKET_TYPES.size()));

        final Instant timestamp = Instant.now();

        return new TicketDto(ticketId != null ? ticketId : UUID.randomUUID(), timestamp, timestamp, eventId,
                randomTicketType, RANDOM.nextInt(500), RANDOM.nextBoolean(), generateSeatingInformation());

    }

    public static Ticket generateTicket(final UUID eventId) {

        final TicketType randomTicketType = ALL_TICKET_TYPES.get(RANDOM.nextInt(ALL_TICKET_TYPES.size()));

        final Instant now = Instant.now();
        return new Ticket(UUID.randomUUID(), now, now, eventId, randomTicketType, RANDOM.nextInt(500),
                RANDOM.nextBoolean(), generateSeatingInformation());

    }

    public static SeatingInformation generateSeatingInformation() {

        return new SeatingInformation(UUID.randomUUID().toString(), UUID.randomUUID().toString());

    }

    public static TicketDto generateTicketDtoWithoutTimestamps(final UUID ticketId, final UUID eventId) {

        final TicketType randomTicketType = ALL_TICKET_TYPES.get(RANDOM.nextInt(ALL_TICKET_TYPES.size()));

        final Instant timestamp = null;

        return new TicketDto(ticketId != null ? ticketId : UUID.randomUUID(), timestamp, timestamp, eventId,
                randomTicketType, RANDOM.nextInt(500), RANDOM.nextBoolean(), generateSeatingInformation());
    }
}
