package org.com.ems.api.dto;

import java.sql.Timestamp;
import java.util.UUID;

import org.com.ems.api.domainobjects.SeatingInformation;
import org.com.ems.api.domainobjects.TicketType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Ticket Entity object
 *
 * @author Evangelos Georgiou
 */

public record TicketDto(@Schema(hidden = true) UUID uuid, @Schema(hidden = true) Timestamp lastUpdated,
	@Schema(example = "61ee465a-f3d8-400a-8ae4-5e806b3eba92", description = "The uuid of the event") UUID eventID,
	@Schema(description = "What kind of ticket type it is") TicketType ticketType,
	@Schema(example = "150", description = "The price of the ticket") Integer price,
	@Schema(example = "true", description = "If the ticket is transferable") Boolean transferable,
	@Schema(description = "The SeatingInformation of the ticket") SeatingInformation seatInfo) {

}
