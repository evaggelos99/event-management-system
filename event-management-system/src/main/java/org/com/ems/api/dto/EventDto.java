package org.com.ems.api.dto;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.domainobjects.json.DurationDeserialzer;
import org.com.ems.api.domainobjects.json.DurationSerialzer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Event Entity object
 *
 * @author Evangelos Georgiou
 */
public record EventDto(@Schema(hidden = true) UUID uuid, @Schema(hidden = true) Instant updatedTimestamp,
		@Schema(example = "Wedding of Maria and Andreas", description = "Name of the Event") String name,
		@Schema(example = "Place of Interest", description = "The place of the Event") String place,
		@Schema(example = "WEDDING", description = "The type of the Event") EventType eventType,
		@Schema(description = "The list of attendees") List<UUID> attendeesIDs,
		@Schema(description = "The organizer of the event", example = "61ee265a-f3d8-400a-8ae4-5e806b3eba92") UUID organizerID,
		@Schema(example = "580", description = "The limit people the event can hold") Integer limitOfPeople,
		@Schema(description = "The sponsor of the event") UUID sponsorID,
		@Schema(description = "The start time of the Event") ZonedDateTime startTimeOfEvent,
		@Schema(description = "The duration of the Event", example = "PT5H") @JsonDeserialize(using = DurationDeserialzer.class) @JsonSerialize(using = DurationSerialzer.class) Duration durationOfEvent) {

}