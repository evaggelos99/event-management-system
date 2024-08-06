package com.github.evaggelos99.ems.event.api;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import com.github.evaggelos99.ems.common.api.domainobjects.EventType;

import jakarta.validation.constraints.NotNull;

public final class Event extends AbstractDomainObject {

    private final String name;
    private final String place;
    private final EventType eventType;
    private final List<UUID> attendeesIds;
    private final UUID organizerId;
    private final Integer limitOfPeople;
    private final List<UUID> sponsorsIds;
    private final LocalDateTime startTime;
    private final Duration duration;

    public Event(final UUID uuid,
		 final Instant createdAt,
		 final Instant lastUpdated,
		 @NotNull final String name,
		 @NotNull final String place,
		 @NotNull final EventType eventType,
		 @NotNull final List<UUID> attendeesIds,
		 @NotNull final UUID organizerId,
		 @NotNull final Integer limitOfPeople,
		 final List<UUID> sponsorsIds,
		 @NotNull final LocalDateTime startTime,
		 @NotNull final Duration duration) {

	super(uuid, createdAt, lastUpdated);
	this.name = name;
	this.place = place;
	this.eventType = eventType;
	this.attendeesIds = attendeesIds;
	this.organizerId = organizerId;
	this.limitOfPeople = limitOfPeople;
	this.sponsorsIds = sponsorsIds;
	this.startTime = startTime;
	this.duration = duration;

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

	return this.attendeesIds;

    }

    public UUID getOrganizerID() {

	return this.organizerId;

    }

    public Integer getLimitOfPeople() {

	return this.limitOfPeople;

    }

    public List<UUID> getSponsorsIds() {

	return this.sponsorsIds;

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

	return new EqualsBuilder().appendSuper(super.equals(that)).append(this.name, that.name)
		.append(this.place, that.place).append(this.eventType, that.eventType)
		.append(this.attendeesIds, that.attendeesIds).append(this.organizerId, that.organizerId)
		.append(this.limitOfPeople, that.limitOfPeople).append(this.sponsorsIds, that.sponsorsIds)
		.append(this.startTime, that.startTime).append(this.duration, that.duration).build();

    }

    @Override
    public int hashCode() {

	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.name).append(this.place)
		.append(this.eventType).append(this.attendeesIds).append(this.organizerId).append(this.limitOfPeople)
		.append(this.sponsorsIds).append(this.startTime).append(this.duration).build();

    }

    @Override
    public String toString() {

	return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
		.append("name", this.name).append("place", this.place).append("eventType", this.eventType)
		.append("attendeesIDs", this.attendeesIds).append("organizerID", this.organizerId)
		.append("limitOfPeople", this.limitOfPeople).append("sponsorID", this.sponsorsIds)
		.append("startTime", this.startTime).append("duration", this.duration).toString();

    }

}