package org.com.ems.api.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Attendee Entity object
 *
 * @author Evangelos Georgiou
 */
public record AttendeeDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid,
	@Schema(hidden = true) Timestamp lastUpdated,
	@Schema(example = "John", description = "First name of the Attendee") String firstName,
	@Schema(example = "Smith", description = "Last name of the Attendee") String lastName,
	@Schema(description = "The UUIDs of tickets of an event belonging to an Attendee") List<UUID> ticketIDs) {

}
