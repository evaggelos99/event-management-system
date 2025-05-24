package io.github.evaggelos99.ems.organizer.api;

import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Organizer Entity object
 *
 * @author Evangelos Georgiou
 */
public record OrganizerDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid, //
                           @Null @Schema(hidden = true) OffsetDateTime createdAt, //
                           @Null @Schema(hidden = true) OffsetDateTime lastUpdated, //
                           @NotBlank @Schema(example = "Duis aute irure", description = "The limit of the event") String name,
                           @NotBlank @Schema(example = "www.seddoeiusmod.com", description = "The website of the organizer") String website,
                           @NotNull @Schema(example = "quis nostrud exercitation", description = "The description of the organizer") String information,
                           @NotNull @Schema(description = "The event types that organizer has specializes in") List<EventType> eventTypes,
                           @NotNull @Schema(description = "The contact information of the Organizer") ContactInformation contactInformation) {

    public static Builder builder() {

        return new Builder();
    }

    public static final class Builder {

        private UUID uuid;
        private OffsetDateTime createdAt;
        private OffsetDateTime lastUpdated;
        private String name;
        private String website;
        private String information;
        private List<EventType> eventTypes;
        private ContactInformation contactInformation;

        public OrganizerDto build() {

            return new OrganizerDto(uuid, createdAt, lastUpdated, name, website, information, eventTypes, contactInformation);
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

        public Builder website(final String website) {
            this.website = website;
            return this;
        }

        public Builder information(final String information) {
            this.information = information;
            return this;
        }

        public Builder eventTypes(final List<EventType> eventTypes) {
            this.eventTypes = eventTypes;
            return this;
        }

        public Builder contactInformation(final ContactInformation contactInformation) {
            this.contactInformation = contactInformation;
            return this;
        }
    }

}