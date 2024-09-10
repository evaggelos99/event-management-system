package io.github.evaggelos99.ems.ticket.api;

import java.time.Instant;
import java.util.UUID;

import io.github.evaggelos99.ems.common.api.domainobjects.SeatingInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.TicketType;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.NotNegative;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * Ticket Entity object
 *
 * @author Evangelos Georgiou
 */
public record TicketDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid,
		@Null @Schema(hidden = true) Instant createdAt, @Null @Schema(hidden = true) Instant lastUpdated,
		@NotNull @Schema(example = "61ee465a-f3d8-400a-8ae4-5e806b3eba92", description = "The uuid of the event") UUID eventID,
		@NotNull @Schema(description = "What kind of ticket type it is") TicketType ticketType,
		@NotNegative @Schema(example = "150", description = "The price of the ticket") Integer price,
		@NotNull @Schema(example = "true", description = "If the ticket is transferable") Boolean transferable,
		@NotNull @Schema(description = "The SeatingInformation of the ticket") SeatingInformation seatInformation) {
}
