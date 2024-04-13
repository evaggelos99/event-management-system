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
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@NotNull
	@Schema(description = "a list of attendees")
	private List<Attendee> attendees;
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@NotNull
	@Schema(description = "The organizer of the event")
	private Organizer organizer;
	@NotNull
	@Schema(example = "580", description = "The limit of the event")
	private Integer limitOfPeople;
	@Nullable
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@Schema(description = "The sponsor of the event")
	private Sponsor sponsor;
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
			@NotNull final EventType eventType, @NotNull final List<Attendee> attendees,
			@NotNull final Organizer organizer, @NotNull final Integer limitOfPeople, @Nullable final Sponsor sponsor,
			@NotNull final ZonedDateTime startTimeOfEvent, @NotNull final Duration durationOfEvent) {

		super(uuid);
		this.name = requireNonNull(name);
		this.place = requireNonNull(place);
		this.eventType = requireNonNull(eventType);
		this.attendees = requireNonNull(attendees);
		this.organizer = requireNonNull(organizer);
		this.limitOfPeople = requireNonNull(limitOfPeople);
		this.sponsor = requireNonNull(sponsor);
		this.startTimeOfEvent = requireNonNull(startTimeOfEvent);
		this.durationOfEvent = requireNonNull(durationOfEvent);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.name)
				.append(this.place).append(this.eventType).append(this.attendees).append(this.organizer)
				.append(this.limitOfPeople).append(this.sponsor).append(this.startTimeOfEvent)
				.append(this.durationOfEvent).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.name).append(this.place).append(this.eventType).append(this.attendees)
				.append(this.organizer).append(this.limitOfPeople).append(this.sponsor).append(this.startTimeOfEvent)
				.append(this.durationOfEvent).build();
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
				.append(this.eventType, rhs.eventType).append(this.attendees, rhs.attendees)
				.append(this.organizer, rhs.organizer).append(this.limitOfPeople, rhs.limitOfPeople)
				.append(this.sponsor, rhs.sponsor).append(this.startTimeOfEvent, rhs.startTimeOfEvent)
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

	public List<Attendee> getAttendees() {
		return this.attendees;
	}

	public Organizer getOrganizer() {
		return this.organizer;
	}

	public Integer getLimitOfPeople() {
		return this.limitOfPeople;
	}

	public Sponsor getSponsor() {
		return this.sponsor;
	}

	public ZonedDateTime getStartTimeOfEvent() {
		return this.startTimeOfEvent;
	}

	public Duration getDurationOfEvent() {
		return this.durationOfEvent;
	}

}