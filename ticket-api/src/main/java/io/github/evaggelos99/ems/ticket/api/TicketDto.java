package io.github.evaggelos99.ems.ticket.api;

import io.github.evaggelos99.ems.common.api.domainobjects.SeatingInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.TicketType;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.NotNegative;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Ticket Entity object
 *
 * @author Evangelos Georgiou
 */
public record TicketDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid,
                        @Null @Schema(hidden = true) OffsetDateTime createdAt,
                        @Null @Schema(hidden = true) OffsetDateTime lastUpdated,
                        @NotNull @Schema(example = "61ee465a-f3d8-400a-8ae4-5e806b3eba92", description = "The uuid of the event") UUID eventID,
                        @NotNull @Schema(description = "What kind of ticket type it is") TicketType ticketType,
                        @NotNegative @Schema(example = "150", description = "The price of the ticket") Integer price,
                        @NotNull @Schema(example = "true", description = "If the ticket is transferable") Boolean transferable,
                        @NotNull @Schema(description = "The SeatingInformation of the ticket") SeatingInformation seatInformation,
                        @AssertFalse @Schema(description = "The SeatingInformation of the ticket") Boolean used) {

    public static Builder builder() {

        return new Builder();
    }

    public static Builder from(final TicketDto obj) {

        return new Builder()
                .uuid(obj.uuid)
                .createdAt(obj.createdAt)
                .lastUpdated(obj.lastUpdated)
                .eventID(obj.eventID)
                .ticketType(obj.ticketType)
                .price(obj.price)
                .transferable(obj.transferable)
                .seatInformation(obj.seatInformation)
                .used(obj.used);
    }

    public static final class Builder {

        private UUID uuid;
        private OffsetDateTime createdAt;
        private OffsetDateTime lastUpdated;
        private UUID eventID;
        private TicketType ticketType;
        private Integer price;
        private Boolean transferable;
        private SeatingInformation seatInformation;
        private Boolean used;

        private Builder() {

        }

        public TicketDto build() {

            return new TicketDto(uuid, createdAt, lastUpdated, eventID, ticketType, price, transferable, seatInformation, used);
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

        public Builder eventID(final UUID eventID) {
            this.eventID = eventID;
            return this;
        }

        public Builder ticketType(final TicketType ticketType) {
            this.ticketType = ticketType;
            return this;
        }

        public Builder price(final Integer price) {
            this.price = price;
            return this;
        }

        public Builder transferable(final Boolean transferable) {
            this.transferable = transferable;
            return this;
        }

        public Builder seatInformation(final SeatingInformation seatInformation) {
            this.seatInformation = seatInformation;
            return this;
        }

        public Builder used(final boolean used) {
            this.used = used;
            return this;
        }
    }

}
