package org.com.ems.api.dto;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.EventType;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Organizer Entity object
 *
 * @author Evangelos Georgiou
 */
public record OrganizerDto(@Schema(hidden = true) UUID uuid, @Schema(hidden = true) Timestamp lastUpdated,
	@Schema(example = "SpecialWeddings", description = "The limit of the event") String name,
	@Schema(example = "www.google.com", description = "The website of the organizer") String website,
	@Schema(example = "Organizer specialized in weddings taking place in the country or even outside!", description = "The description of the organizer") String description,
	@Schema(description = "The event types that organizer has specializes in") List<EventType> eventTypes,
	@Schema(description = "The contact information of the Organizer") ContactInformation contactInformation) {

}
