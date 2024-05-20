package org.com.ems.api.dto;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
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
public record EventDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid, //
	@Schema(hidden = true) Timestamp lastUpdated, //
	@Schema(example = "Wedding of Maria and Andreas", description = "Name of the Event") String denomination, //
	@Schema(example = "Place of Interest", description = "The place of the Event") String place, //
	@Schema(example = "WEDDING", description = "The type of the Event") EventType eventType, //
	@Schema(description = "The list of attendees") List<UUID> attendeesIds, //
	@Schema(description = "The organizer of the event", example = "61ee265a-f3d8-400a-8ae4-5e806b3eba92") UUID organizerId, //
	@Schema(example = "580", description = "The limit people the event can hold") Integer limitOfPeople, //
	@Schema(description = "The sponsor of the event") UUID sponsorId, //
	@Schema(description = "The start time of the Event") LocalDateTime startTimeOfEvent, //
	@Schema(description = "The duration of the Event", example = "PT5H") //
	@JsonDeserialize(using = DurationDeserialzer.class) //
	@JsonSerialize(using = DurationSerialzer.class) Duration duration) {

}
