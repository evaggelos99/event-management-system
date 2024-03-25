package org.com.ems.api.dao;

import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public final class Event extends AbstractDAO {

	private static final long serialVersionUID = -8863335953855552553L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID UUID;
	@NotNull
	private String name;
	@NotNull
	private String place;
	@NotNull
	private EventType eventType;
	@OneToMany(cascade = CascadeType.ALL)
	@NotNull
	private List<Attendee> attendees;
	@OneToOne(cascade = CascadeType.ALL)
	@NotNull
	private Organizer organizer;
	@NotNull
	private Integer limitOfPeople;

	protected Event() {

	}

	@Override
	public String toString() {

		return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE).append(UUID).append(name).append(place)
				.append(eventType).append(attendees).append(organizer).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(UUID).append(name).append(place).append(eventType).append(attendees)
				.append(organizer).build();
	}

	@Override
	public boolean equals(Object object) {

		if (this == object) {
			return true;
		}

		if (object == null) {
			return false;
		}

		if (getClass() != object.getClass()) {
			return false;
		}

		var rhs = (Event) object;

		return new EqualsBuilder().append(this.UUID, rhs.UUID).append(this.name, rhs.name).append(this.place, rhs.place)
				.append(this.eventType, rhs.eventType).append(this.attendees, rhs.attendees)
				.append(this.organizer, rhs.organizer).build();
	}

	public UUID getUUID() {
		return UUID;
	}

	public String getName() {
		return name;
	}

	public String getPlace() {
		return place;
	}

	public EventType getEventType() {
		return eventType;
	}

	public List<Attendee> getAttendees() {
		return attendees;
	}

	public Organizer getOrganizer() {
		return organizer;
	}

	public Integer getLimitOfPeople() {
		return limitOfPeople;
	}

}