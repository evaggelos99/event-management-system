package org.com.ems.api.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.EventType;

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
	@Null @Schema(hidden = true) Timestamp createdAt, //
	@Null @Schema(hidden = true) Timestamp lastUpdated, //
	@NotBlank @Schema(example = "SpecialWeddings", description = "The limit of the event") String denomination, //
	@NotBlank @Schema(example = "www.google.com", description = "The website of the organizer") String website, //
	@NotNull @Schema(example = "Organizer specialized in weddings taking place in the country or even outside!", description = "The description of the organizer") String information, //
	@NotNull @Schema(description = "The event types that organizer has specializes in") List<EventType> eventTypes, //
	@NotNull @Schema(description = "The contact information of the Organizer") ContactInformation contactInformation) {

}
