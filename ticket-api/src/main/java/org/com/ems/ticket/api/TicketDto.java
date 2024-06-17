package org.com.ems.ticket.api;

import java.sql.Timestamp;
import java.util.UUID;

import org.com.ems.common.api.domainobjects.SeatingInformation;
import org.com.ems.common.api.domainobjects.TicketType;
import org.com.ems.common.api.domainobjects.validators.constraints.NotNegative;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * Ticket Entity object
 *
 * @author Evangelos Georgiou
 */

public record TicketDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid,
	@Null @Schema(hidden = true) Timestamp createdAt, @Null @Schema(hidden = true) Timestamp lastUpdated,
	@NotNull @Schema(example = "61ee465a-f3d8-400a-8ae4-5e806b3eba92", description = "The uuid of the event") UUID eventID,
	@NotNull @Schema(description = "What kind of ticket type it is") TicketType ticketType,
	@NotNegative @Schema(example = "150", description = "The price of the ticket") Integer price,
	@NotNull @Schema(example = "true", description = "If the ticket is transferable") Boolean transferable,
	@NotNull @Schema(description = "The SeatingInformation of the ticket") SeatingInformation seatInformation) {

}
