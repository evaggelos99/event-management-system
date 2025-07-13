package io.github.evaggelos99.scheduler.service.model;

import java.time.Instant;
import java.util.UUID;

public record TicketDto(UUID uuid,
                        Instant createdAt,
                        Instant lastUpdated,
                        UUID eventID,
                        String ticketType,
                        Integer price,
                        boolean transferable,
                        SeatInformation seatInformation,
                        boolean used) {

    public record SeatInformation(String seat, String section) {

    }

}
