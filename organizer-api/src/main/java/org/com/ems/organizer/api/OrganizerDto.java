package org.com.ems.organizer.api;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.com.ems.common.api.domainobjects.ContactInformation;
import org.com.ems.common.api.domainobjects.EventType;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * Organizer Entity object
 *
 * @author Evangelos Georgiou
 */
public record OrganizerDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid, //
		@Null @Schema(hidden = true) Instant createdAt, //
		@Null @Schema(hidden = true) Instant lastUpdated, //
		@NotBlank @Schema(example = "Duis aute irure", description = "The limit of the event") String name, //
		@NotBlank @Schema(example = "www.seddoeiusmod.com", description = "The website of the organizer") String website, //
		@NotNull @Schema(example = "quis nostrud exercitation", description = "The description of the organizer") String information, //
		@NotNull @Schema(description = "The event types that organizer has specializes in") List<EventType> eventTypes, //
		@NotNull @Schema(description = "The contact information of the Organizer") ContactInformation contactInformation) {
}
