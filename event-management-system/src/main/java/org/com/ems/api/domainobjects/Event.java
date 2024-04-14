package org.com.ems.api.domainobjects;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.com.ems.api.domainobjects.json.DurationDeserialzer;
import org.com.ems.api.domainobjects.json.DurationSerialzer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

/**
 * Event Entity object
 *
 * @author Evangelos Georgiou
 */
@Entity
public final class Event extends AbstractDomainObject {

	private static final long serialVersionUID = 9091508417391062479L;

	@NotNull
	@Schema(example = "Wedding of Maria and Andreas", description = "Name of the Event")
	private String name;
	@NotNull
	@Schema(example = "Place of Interest", description = "The place of the Event")
	private String place;
	@NotNull
	@Schema(example = "WEDDING", description = "The type of the Event")
	private EventType eventType;
	// ManyToMany
	@NotNull
	@Schema(description = "a list of attendees")
	private List<UUID> attendeesIDs;
	// ManyToOne
	@NotNull
	@Schema(description = "The organizer of the event", example = "61ee265a-f3d8-400a-8ae4-5e806b3eba92")
	private UUID organizerID;
	@NotNull
	@Schema(example = "580", description = "The limit of the event")
	private Integer limitOfPeople;
	// ManyToOne
	@Nullable
	@Schema(description = "The sponsor of the event")
	private UUID sponsorID;
	@NotNull
	@Schema(description = "The start time of the Event")
	private ZonedDateTime startTimeOfEvent;
	@NotNull
	@Schema(description = "The duration of the Event", example = "PT5H")
	@JsonDeserialize(using = DurationDeserialzer.class)
	@JsonSerialize(using = DurationSerialzer.class)
	private Duration durationOfEvent;

	protected Event() {

	}

	public Event(final UUID uuid, @NotNull final String name, @NotNull final String place,
			@NotNull final EventType eventType, @NotNull final List<UUID> attendeesIDs, @NotNull final UUID organizerID,
			@NotNull final Integer limitOfPeople, @Nullable final UUID sponsorID,
			@NotNull final ZonedDateTime startTimeOfEvent, @NotNull final Duration durationOfEvent) {

		super(uuid);
		this.name = requireNonNull(name);
		this.place = requireNonNull(place);
		this.eventType = requireNonNull(eventType);
		this.attendeesIDs = requireNonNull(attendeesIDs);
		this.organizerID = requireNonNull(organizerID);
		this.limitOfPeople = requireNonNull(limitOfPeople);
		this.sponsorID = requireNonNull(sponsorID);
		this.startTimeOfEvent = requireNonNull(startTimeOfEvent);
		this.durationOfEvent = requireNonNull(durationOfEvent);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.name)
				.append(this.place).append(this.eventType).append(this.attendeesIDs).append(this.organizerID)
				.append(this.limitOfPeople).append(this.sponsorID).append(this.startTimeOfEvent)
				.append(this.durationOfEvent).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.name).append(this.place).append(this.eventType)
				.append(this.attendeesIDs).append(this.organizerID).append(this.limitOfPeople).append(this.sponsorID)
				.append(this.startTimeOfEvent).append(this.durationOfEvent).build();
	}

	@Override
	public boolean equals(final Object object) {

		if (this == object) {
			return true;
		}

		if (object == null) {
			return false;
		}

		if (this.getClass() != object.getClass()) {
			return false;
		}

		final var rhs = (Event) object;

		return new EqualsBuilder().append(this.name, rhs.name).append(this.place, rhs.place)
				.append(this.eventType, rhs.eventType).append(this.attendeesIDs, rhs.attendeesIDs)
				.append(this.organizerID, rhs.organizerID).append(this.limitOfPeople, rhs.limitOfPeople)
				.append(this.sponsorID, rhs.sponsorID).append(this.startTimeOfEvent, rhs.startTimeOfEvent)
				.append(this.durationOfEvent, rhs.durationOfEvent).build();
	}

	public String getName() {
		return this.name;
	}

	public String getPlace() {
		return this.place;
	}

	public EventType getEventType() {
		return this.eventType;
	}

	public List<UUID> getAttendeesIDs() {
		return this.attendeesIDs;
	}

	public UUID getOrganizerID() {
		return this.organizerID;
	}

	public Integer getLimitOfPeople() {
		return this.limitOfPeople;
	}

	public UUID getSponsorID() {
		return this.sponsorID;
	}

	public ZonedDateTime getStartTimeOfEvent() {
		return this.startTimeOfEvent;
	}

	public Duration getDurationOfEvent() {
		return this.durationOfEvent;
	}

}