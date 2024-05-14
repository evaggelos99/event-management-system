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

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

/**
 * Event Entity object
 *
 * @author Evangelos Georgiou
 */
public class Event extends AbstractDomainObject {

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
    private LocalDateTime startTime;
    @NotNull
    private Duration duration;

    public Event(final UUID uuid,
		 final Instant lastUpdated,
		 @NotNull final String name,
		 @NotNull final String place,
		 @NotNull final EventType eventType,
		 @NotNull final List<UUID> attendeesIDs,
		 @NotNull final UUID organizerID,
		 @NotNull final Integer limitOfPeople,
		 final UUID sponsorID,
		 @NotNull final LocalDateTime startTime,
		 @NotNull final Duration duration) {

	super(uuid, lastUpdated);
	this.name = name;
	this.place = place;
	this.eventType = eventType;
	this.attendeesIDs = attendeesIDs;
	this.organizerID = organizerID;
	this.limitOfPeople = limitOfPeople;
	this.sponsorID = sponsorID;
	this.startTime = startTime;
	this.duration = duration;

    }

    public Event() {

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
		.append(this.attendeesIDs, that.attendeesIDs).append(this.organizerID, that.organizerID)
		.append(this.limitOfPeople, that.limitOfPeople).append(this.sponsorID, that.sponsorID)
		.append(this.startTime, that.startTime).append(this.duration, that.duration).build();

    }

    @Override
    public int hashCode() {

	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.name).append(this.place)
		.append(this.eventType).append(this.attendeesIDs).append(this.organizerID).append(this.limitOfPeople)
		.append(this.sponsorID).append(this.startTime).append(this.duration).build();

    }

    @Override
    public String toString() {

	return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
		.append("name", this.name).append("place", this.place).append("eventType", this.eventType)
		.append("attendeesIDs", this.attendeesIDs).append("organizerID", this.organizerID)
		.append("limitOfPeople", this.limitOfPeople).append("sponsorID", this.sponsorID)
		.append("startTime", this.startTime).append("duration", this.duration).toString();

    }

}