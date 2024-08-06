package com.github.evaggelos99.ems.event.api;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.github.evaggelos99.ems.common.api.domainobjects.EventType;
import com.github.evaggelos99.ems.common.api.domainobjects.json.DurationDeserializer;
import com.github.evaggelos99.ems.common.api.domainobjects.json.DurationSerialzer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * Event Entity object
 *
 * @author Evangelos Georgiou
 */
public record EventDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid, //
		@Null @Schema(hidden = true) Instant createdAt, @Null @Schema(hidden = true) Instant lastUpdated, //
		@NotBlank @Schema(example = "in voluptate velit", description = "Name of the Event") String name, //
		@NotBlank @Schema(example = "anim id est laborum", description = "The place of the Event") String place, //
		@NotNull @Schema(example = "OTHER", description = "The type of the Event") EventType eventType, //
		@Schema(description = "The list of attendees") List<UUID> attendeesIds, //
		@NotNull @Schema(description = "The organizer of the event", example = "61ee265a-f3d8-400a-8ae4-5e806b3eba92") UUID organizerId, //
		@NotNull @Schema(example = "500", description = "The limit people the event can hold") Integer limitOfPeople, //
		@Schema(description = "The sponsors of the event") List<UUID> sponsorsIds, //
		@NotNull @Schema(description = "The start time of the Event") LocalDateTime startTimeOfEvent, //
		@NotNull @Schema(description = "The duration of the Event", example = "PT5H") //
		@JsonDeserialize(using = DurationDeserializer.class) //
		@JsonSerialize(using = DurationSerialzer.class) Duration duration) {
}
