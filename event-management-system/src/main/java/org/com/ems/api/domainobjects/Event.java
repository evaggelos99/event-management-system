package org.com.ems.api.domainobjects;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.com.ems.api.domainobjects.json.DurationDeserialzer;
import org.com.ems.api.domainobjects.json.DurationSerialzer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Event Entity object
 *
 * @author Evangelos Georgiou
 */
@Builder
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString
@Entity
public class Event extends AbstractDomainObject {

	private static final long serialVersionUID = -2971450926959811049L;

	@NotNull
	private String name;
	@NotNull
	private String place;
	@NotNull
	private EventType eventType;
	// ManyToMany
	@NotNull
	private List<UUID> attendeesIDs;
	// ManyToOne
	@NotNull
	private UUID organizerID;
	@NotNull
	private Integer limitOfPeople;
	// ManyToOne
	@Nullable
	private UUID sponsorID;
	@NotNull
	private ZonedDateTime startTimeOfEvent;
	@NotNull
	@JsonDeserialize(using = DurationDeserialzer.class)
	@JsonSerialize(using = DurationSerialzer.class)
	private Duration durationOfEvent;

	public Event(@NotNull final String name, @NotNull final String place, @NotNull final EventType eventType,
			@NotNull final List<UUID> attendeesIDs, @NotNull final UUID organizerID,
			@NotNull final Integer limitOfPeople, final UUID sponsorID, @NotNull final ZonedDateTime startTimeOfEvent,
			@NotNull final Duration durationOfEvent) {

		super();
		this.name = name;
		this.place = place;
		this.eventType = eventType;
		this.attendeesIDs = attendeesIDs;
		this.organizerID = organizerID;
		this.limitOfPeople = limitOfPeople;
		this.sponsorID = sponsorID;
		this.startTimeOfEvent = startTimeOfEvent;
		this.durationOfEvent = durationOfEvent;
	}

}