package io.github.evaggelos99.ems.event.api;

import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public final class Event extends AbstractDomainObject {

    private final String name;
    private final String place;
    private final EventType eventType;
    private final List<UUID> attendeesIds;
    private final UUID organizerId;
    private final Integer limitOfPeople;
    private final List<UUID> sponsorsIds;
    private final boolean streamable;
    private final LocalDateTime startTime;
    private final Duration duration;

    public Event(final UUID uuid, final OffsetDateTime createdAt, final OffsetDateTime lastUpdated, @NotNull final String name,
                 @NotNull final String place, @NotNull final EventType eventType, @NotNull final List<UUID> attendeesIds,
                 @NotNull final UUID organizerId, @NotNull final Integer limitOfPeople, final List<UUID> sponsorsIds,
                 @NotNull final boolean streamable, @NotNull final LocalDateTime startTime, @NotNull final Duration duration) {

        super(uuid, createdAt, lastUpdated);
        this.name = name;
        this.place = place;
        this.eventType = eventType;
        this.attendeesIds = attendeesIds;
        this.organizerId = organizerId;
        this.limitOfPeople = limitOfPeople;
        this.sponsorsIds = sponsorsIds;
        this.streamable=streamable;
        this.startTime = startTime;
        this.duration = duration;
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

    public List<UUID> getAttendeesIDs() {

        return attendeesIds;
    }

    public UUID getOrganizerID() {

        return organizerId;
    }

    public Integer getLimitOfPeople() {

        return limitOfPeople;
    }

    public List<UUID> getSponsorsIds() {

        return sponsorsIds;
    }

    public boolean isStreamable() {
        return streamable;
    }

    public LocalDateTime getStartTime() {

        return startTime;
    }

    public Duration getDuration() {

        return duration;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final Event that = (Event) o;

        return new EqualsBuilder().appendSuper(super.equals(that)).append(name, that.name).append(place, that.place)
                .append(eventType, that.eventType).append(attendeesIds, that.attendeesIds)
                .append(organizerId, that.organizerId).append(limitOfPeople, that.limitOfPeople)
                .append(sponsorsIds, that.sponsorsIds).append(startTime, that.startTime).append(duration, that.duration)
                .append(streamable, that.streamable)
                .build();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(name).append(place).append(eventType)
                .append(attendeesIds).append(organizerId).append(limitOfPeople).append(sponsorsIds).append(startTime)
                .append(duration).append(streamable).build();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
                .append("name", name).append("place", place).append("eventType", eventType)
                .append("attendeesIDs", attendeesIds).append("organizerID", organizerId)
                .append("limitOfPeople", limitOfPeople).append("sponsorID", sponsorsIds).append("startTime", startTime)
                .append("duration", duration)
                .append("streamable", streamable).toString();
    }
}