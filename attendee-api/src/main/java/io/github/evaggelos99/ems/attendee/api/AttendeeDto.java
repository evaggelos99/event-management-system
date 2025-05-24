package io.github.evaggelos99.ems.attendee.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Attendee Entity object
 *
 * @author Evangelos Georgiou
 */
public record AttendeeDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid,
                          @Null @Schema(hidden = true) OffsetDateTime createdAt, //
                          @Null @Schema(hidden = true) OffsetDateTime lastUpdated,
                          @NotBlank @Schema(example = "Lorem ipsum dolor sit amet", description = "First name of the Attendee") String firstName,
                          @NotBlank @Schema(example = "aliquip ex ea commodo consequat", description = "Last name of the Attendee") String lastName,
                          @Schema(description = "The UUIDs of tickets of an event belonging to an Attendee") List<UUID> ticketIDs) {




    public static Builder builder() {

        return new Builder();
    }

    public static final class Builder {

        private UUID uuid;
        private OffsetDateTime createdAt;
        private OffsetDateTime lastUpdated;
        private String firstName;
        private String lastName;
        private List<UUID> ticketIDs;

        private Builder() {

        }

        public AttendeeDto build() {

            return new AttendeeDto(uuid,createdAt,lastUpdated,firstName,lastName,ticketIDs);
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

        public Builder firstName(final String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(final String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder ticketIDs(final List<UUID> ticketIDs) {
            this.ticketIDs = ticketIDs;
            return this;
        }
    }
}
