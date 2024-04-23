package org.com.ems.api.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Attendee Entity object
 *
 * @author Evangelos Georgiou
 */
public record AttendeeDto(@Schema(hidden = true) UUID uuid, @Schema(hidden = true) Instant updatedTimestamp,
		@Schema(example = "John", description = "First name of the Attendee") String firstName,
		@Schema(example = "Smith", description = "Last name of the Attendee") String lastName,
		@Schema(description = "The UUIDs of tickets of an event belonging to an Attendee") List<UUID> ticketsIDs) {

}
