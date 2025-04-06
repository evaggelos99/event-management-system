package io.github.evaggelos99.ems.ticket.api;

import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.SeatingInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.TicketType;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.NotNegative;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.UUID;

public final class Ticket extends AbstractDomainObject {

    private final UUID eventID;
    private final TicketType ticketType;
    private final Integer price;
    private final Boolean transferable;
    private final SeatingInformation seatingInformation;

    public Ticket(final UUID uuid, final Instant createdAt, final Instant lastUpdated, @NotNull final UUID eventID,
                  @NotNull final TicketType ticketType,
                  @NotNull @NotNegative(message = "cannot be negative") final Integer price,
                  @NotNull final Boolean transferable, @NotNull final SeatingInformation seatingInformation) {

        super(uuid, createdAt, lastUpdated);
        this.eventID = eventID;
        this.ticketType = ticketType;
        this.price = price;
        this.transferable = transferable;
        this.seatingInformation = seatingInformation;
    }

    public UUID getEventID() {

        return eventID;
    }

    public TicketType getTicketType() {

        return ticketType;
    }

    public Integer getPrice() {

        return price;
    }

    public Boolean getTransferable() {

        return transferable;
    }

    public SeatingInformation getSeatingInformation() {

        return seatingInformation;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final Ticket that = (Ticket) o;

        return new EqualsBuilder().appendSuper(super.equals(that)).append(eventID, that.eventID)
                .append(ticketType, that.ticketType).append(price, that.price).append(transferable, that.transferable)
                .append(seatingInformation, that.seatingInformation).build();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(eventID).append(ticketType)
                .append(price).append(transferable).append(seatingInformation).build();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
                .append("eventID", eventID).append("ticketType", ticketType).append("price", price)
                .append("transferable", transferable).append("seat information", seatingInformation).toString();
    }

}
