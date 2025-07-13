package io.github.evaggelos99.scheduler.service.model;

import java.util.UUID;

public record TicketInfo(UUID ticketId, UUID eventId, String eventName,
                         String eventPlace, String attendeeEmail,
                         String firstName, String lastName) {

}