package org.com.ems.event.api;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.com.ems.common.api.domainobjects.EventType;
import org.com.ems.common.api.domainobjects.json.DurationDeserialzer;
import org.com.ems.common.api.domainobjects.json.DurationSerialzer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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
	@Null @Schema(hidden = true) Timestamp createdAt, @Null @Schema(hidden = true) Timestamp lastUpdated, //
	@NotBlank @Schema(example = "Wedding of Maria and Andreas", description = "Name of the Event") String name, //
	@NotBlank @Schema(example = "Place of Interest", description = "The place of the Event") String place, //
	@NotNull @Schema(example = "WEDDING", description = "The type of the Event") EventType eventType, //
	@Schema(description = "The list of attendees") List<UUID> attendeesIds, //
	@NotNull @Schema(description = "The organizer of the event", example = "61ee265a-f3d8-400a-8ae4-5e806b3eba92") UUID organizerId, //
	@NotNull @Schema(example = "580", description = "The limit people the event can hold") Integer limitOfPeople, //
	@Schema(description = "The sponsors of the event") List<UUID> sponsorsIds, //
	@NotNull @Schema(description = "The start time of the Event") LocalDateTime startTimeOfEvent, //
	@NotNull @Schema(description = "The duration of the Event", example = "PT5H") //
	@JsonDeserialize(using = DurationDeserialzer.class) //
	@JsonSerialize(using = DurationSerialzer.class) Duration duration) {

}
