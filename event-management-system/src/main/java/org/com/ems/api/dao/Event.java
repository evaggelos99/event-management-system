package org.com.ems.api.dao;

import static java.util.Objects.requireNonNull;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public final class Event extends AbstractDAO {

	private static final long serialVersionUID = -8863335953855552553L;

	@NotNull
	private String name;
	@NotNull
	private String place;
	@NotNull
	private EventType eventType;
	@OneToMany(cascade = { CascadeType.MERGE, CascadeType.PERSIST }) // CascadeType.REFRESH
	@NotNull
	private List<Attendee> attendees;
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@NotNull
	private Organizer organizer;
	@NotNull
	private Integer limitOfPeople;

	protected Event() {

	}

	public Event(@NotNull final String name, @NotNull final String place, @NotNull final EventType eventType,
			@NotNull final List<Attendee> attendees, @NotNull final Organizer organizer,
			@NotNull final Integer limitOfPeople) {

		this.name = requireNonNull(name);
		this.place = requireNonNull(place);
		this.eventType = requireNonNull(eventType);
		this.attendees = requireNonNull(attendees);
		this.organizer = requireNonNull(organizer);
		this.limitOfPeople = requireNonNull(limitOfPeople);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.name)
				.append(this.place).append(this.eventType).append(this.attendees).append(this.organizer)
				.append(this.limitOfPeople).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.name).append(this.place).append(this.eventType).append(this.attendees)
				.append(this.organizer).append(this.limitOfPeople).build();
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
				.append(this.organizer, rhs.organizer).append(this.limitOfPeople, rhs.limitOfPeople).build();
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

}