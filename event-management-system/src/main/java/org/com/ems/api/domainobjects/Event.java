package org.com.ems.api.domainobjects;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.validation.constraints.NotNull;

public final class Event extends AbstractDomainObject {

    private final String denomination;
    private final String place;
    private final EventType eventType;
    private final List<UUID> attendeesIds;
    private final UUID organizerId;
    private final Integer limitOfPeople;
    private final UUID sponsorId;
    private final LocalDateTime startTime;
    private final Duration duration;

    public Event(final UUID uuid,
		 final Instant createdAt,
		 final Instant lastUpdated,
		 @NotNull final String denomination,
		 @NotNull final String place,
		 @NotNull final EventType eventType,
		 @NotNull final List<UUID> attendeesIds,
		 @NotNull final UUID organizerId,
		 @NotNull final Integer limitOfPeople,
		 final UUID sponsorId,
		 @NotNull final LocalDateTime startTime,
		 @NotNull final Duration duration) {

	super(uuid, createdAt, lastUpdated);
	this.denomination = denomination;
	this.place = place;
	this.eventType = eventType;
	this.attendeesIds = attendeesIds;
	this.organizerId = organizerId;
	this.limitOfPeople = limitOfPeople;
	this.sponsorId = sponsorId;
	this.startTime = startTime;
	this.duration = duration;

    }

    public String getDenomination() {

	return this.denomination;

    }

    public String getPlace() {

	return this.place;

    }

    public EventType getEventType() {

	return this.eventType;

    }

    public List<UUID> getAttendeesIDs() {

	return this.attendeesIds;

    }

    public UUID getOrganizerID() {

	return this.organizerId;

    }

    public Integer getLimitOfPeople() {

	return this.limitOfPeople;

    }

    public UUID getSponsorID() {

	return this.sponsorId;

    }

    public LocalDateTime getStartTime() {

	return this.startTime;

    }

    public Duration getDuration() {

	return this.duration;

    }

    @Override
    public boolean equals(final Object o) {

	if (this == o)
	    return true;
	if (o == null || this.getClass() != o.getClass())
	    return false;

	final Event that = (Event) o;

	return new EqualsBuilder().appendSuper(super.equals(that)).append(this.denomination, that.denomination)
		.append(this.place, that.place).append(this.eventType, that.eventType)
		.append(this.attendeesIds, that.attendeesIds).append(this.organizerId, that.organizerId)
		.append(this.limitOfPeople, that.limitOfPeople).append(this.sponsorId, that.sponsorId)
		.append(this.startTime, that.startTime).append(this.duration, that.duration).build();

    }

    @Override
    public int hashCode() {

	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.denomination).append(this.place)
		.append(this.eventType).append(this.attendeesIds).append(this.organizerId).append(this.limitOfPeople)
		.append(this.sponsorId).append(this.startTime).append(this.duration).build();

    }

    @Override
    public String toString() {

	return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
		.append("denomination", this.denomination).append("place", this.place)
		.append("eventType", this.eventType).append("attendeesIDs", this.attendeesIds)
		.append("organizerID", this.organizerId).append("limitOfPeople", this.limitOfPeople)
		.append("sponsorID", this.sponsorId).append("startTime", this.startTime)
		.append("duration", this.duration).toString();

    }

}