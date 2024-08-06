package com.github.evaggelos99.ems.ticket.api.util;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import com.github.evaggelos99.ems.common.api.domainobjects.SeatingInformation;
import com.github.evaggelos99.ems.common.api.domainobjects.TicketType;
import com.github.evaggelos99.ems.ticket.api.Ticket;
import com.github.evaggelos99.ems.ticket.api.TicketDto;

public final class TicketObjectGenerator {

    private static final List<TicketType> ALL_TICKET_TYPES = List.of(TicketType.values());
    private static final Random RANDOM = new Random();

    private TicketObjectGenerator() {

    }

    public static TicketDto generateTicketDto(final UUID eventId) {

	final TicketType randomTicketType = ALL_TICKET_TYPES.get(RANDOM.nextInt(ALL_TICKET_TYPES.size()));

	final Timestamp timestamp = Timestamp.from(Instant.now());
	return new TicketDto(UUID.randomUUID(), timestamp, timestamp, eventId, randomTicketType, RANDOM.nextInt(500),
		RANDOM.nextBoolean(), generateSeatingInformation());

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
}
