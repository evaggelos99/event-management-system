package io.github.evaggelos99.ems.ticket.api.util;

import io.github.evaggelos99.ems.common.api.domainobjects.SeatingInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.TicketType;
import io.github.evaggelos99.ems.ticket.api.Ticket;
import io.github.evaggelos99.ems.ticket.api.TicketDto;

import java.time.Instant;
import java.time.OffsetDateTime;
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
        final OffsetDateTime timestamp = OffsetDateTime.now();
        return TicketDto.builder()
                .uuid(ticketId != null ? ticketId : UUID.randomUUID())
                .createdAt(timestamp)
                .lastUpdated(timestamp)
                .eventID(eventId)
                .ticketType(randomTicketType)
                .price(RANDOM.nextInt(500))
                .transferable(RANDOM.nextBoolean())
                .seatInformation(generateSeatingInformation())
                .used(RANDOM.nextBoolean()).build();
    }

    public static SeatingInformation generateSeatingInformation() {

        return new SeatingInformation(UUID.randomUUID().toString(), UUID.randomUUID().toString());

    }

    public static Ticket generateTicket(final UUID eventId) {

        final TicketType randomTicketType = ALL_TICKET_TYPES.get(RANDOM.nextInt(ALL_TICKET_TYPES.size()));
        final OffsetDateTime now = OffsetDateTime.now();
        return new Ticket(UUID.randomUUID(), now, now, eventId, randomTicketType, RANDOM.nextInt(500),
                RANDOM.nextBoolean(), generateSeatingInformation(), RANDOM.nextBoolean());

    }

    public static TicketDto generateTicketDtoWithoutTimestamps(final UUID ticketId, final UUID eventId) {

        final TicketType randomTicketType = ALL_TICKET_TYPES.get(RANDOM.nextInt(ALL_TICKET_TYPES.size()));
        return TicketDto.builder()
                .uuid(ticketId != null ? ticketId : UUID.randomUUID())
                .eventID(eventId)
                .ticketType(randomTicketType)
                .price(RANDOM.nextInt(500))
                .transferable(RANDOM.nextBoolean())
                .seatInformation(generateSeatingInformation())
                .used(RANDOM.nextBoolean())
                .build();
    }
}
