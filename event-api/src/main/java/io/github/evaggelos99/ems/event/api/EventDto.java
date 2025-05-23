package io.github.evaggelos99.ems.event.api;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.common.api.domainobjects.json.DurationDeserializer;
import io.github.evaggelos99.ems.common.api.domainobjects.json.DurationSerialzer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Event Entity object
 *
 * @author Evangelos Georgiou
 */
public record EventDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid,
                       @Null @Schema(hidden = true) OffsetDateTime createdAt, @Null @Schema(hidden = true) OffsetDateTime lastUpdated,
                       @NotBlank @Schema(example = "in voluptate velit", description = "Name of the Event") String name,
                       @NotBlank @Schema(example = "anim id est laborum", description = "The place of the Event") String place,
                       @NotNull @Schema(example = "OTHER", description = "The type of the Event") EventType eventType,
                       @Schema(description = "The list of attendees") List<UUID> attendeesIds,
                       @NotNull @Schema(description = "The organizer of the event", example = "61ee265a-f3d8-400a-8ae4-5e806b3eba92") UUID organizerId,
                       @NotNull @Schema(example = "500", description = "The limit people the event can hold") Integer limitOfPeople,
                       @Schema(description = "The sponsors of the event") List<UUID> sponsorsIds,
                       @NotNull @Schema(example = "false", description = "If the event can be streamed.") boolean streamable,
                       @NotNull @Schema(description = "The start time of the Event") LocalDateTime startTimeOfEvent,
                       @NotNull @Schema(description = "The duration of the Event", example = "PT5H")
                       @JsonDeserialize(using = DurationDeserializer.class)
                       @JsonSerialize(using = DurationSerialzer.class) Duration duration) {

    public static Builder builder() {

        return new Builder();
    }

    public static final class Builder {

        private UUID uuid;
        private OffsetDateTime createdAt;
        private OffsetDateTime lastUpdated;
        private String name;
        private String place;
        private EventType eventType;
        private List<UUID> attendeesIds;
        private UUID organizerId;
        private Integer limitOfPeople;
        private boolean streamable;
        private List<UUID> sponsorsIds;
        private LocalDateTime startTimeOfEvent;
        private Duration duration;

        private Builder() {

        }

        public EventDto build() {

            return new EventDto(uuid, createdAt, lastUpdated, name, place, eventType, attendeesIds, organizerId, limitOfPeople, sponsorsIds,streamable, startTimeOfEvent, duration);
        }

        public Builder uuid(final UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public Builder createdAt(final OffsetDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder lastUpdated(final OffsetDateTime lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public Builder name(final String name) {
            this.name = name;
            return this;
        }

        public Builder place(final String place) {
            this.place = place;
            return this;
        }

        public Builder eventType(final EventType eventType) {
            this.eventType = eventType;
            return this;
        }

        public Builder attendeesIds(final List<UUID> attendeesIds) {
            this.attendeesIds = attendeesIds;
            return this;
        }

        public Builder organizerId(final UUID organizerId) {
            this.organizerId = organizerId;
            return this;
        }

        public Builder limitOfPeople(final Integer limitOfPeople) {
            this.limitOfPeople = limitOfPeople;
            return this;
        }

        public Builder streamable(final boolean streamable) {
            this.streamable = streamable;
            return this;
        }

        public Builder sponsorsIds(final List<UUID> sponsorsIds) {
            this.sponsorsIds = sponsorsIds;
            return this;
        }

        public Builder startTimeOfEvent(final LocalDateTime startTimeOfEvent) {
            this.startTimeOfEvent = startTimeOfEvent;
            return this;
        }

        public Builder duration(final Duration duration) {
            this.duration = duration;
            return this;
        }

        public Builder from(final EventDto eventDto) {
            this.uuid = eventDto.uuid();
            this.createdAt = eventDto.createdAt();
            this.lastUpdated = eventDto.lastUpdated();
            this.name = eventDto.name();
            this.place = eventDto.place();
            this.eventType = eventDto.eventType();
            this.attendeesIds = eventDto.attendeesIds();
            this.organizerId = eventDto.organizerId();
            this.limitOfPeople = eventDto.limitOfPeople();
            this.streamable = eventDto.streamable();
            this.sponsorsIds = eventDto.sponsorsIds();
            this.startTimeOfEvent = eventDto.startTimeOfEvent();
            this.duration = eventDto.duration();
            return this;
        }

    }

}
