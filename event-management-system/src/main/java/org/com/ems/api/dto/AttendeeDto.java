package org.com.ems.api.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

/**
 * Attendee Entity object
 *
 * @author Evangelos Georgiou
 */
public record AttendeeDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid,
	@Null @Schema(hidden = true) Timestamp createdAt, //
	@Null @Schema(hidden = true) Timestamp lastUpdated,
	@NotBlank @Schema(example = "John", description = "First name of the Attendee") String firstName,
	@NotBlank @Schema(example = "Smith", description = "Last name of the Attendee") String lastName,
	@Schema(description = "The UUIDs of tickets of an event belonging to an Attendee") List<UUID> ticketIDs) {

}
