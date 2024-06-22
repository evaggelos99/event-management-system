package org.com.ems.ticket.api;

import java.time.Instant;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.com.ems.common.api.domainobjects.AbstractDomainObject;
import org.com.ems.common.api.domainobjects.SeatingInformation;
import org.com.ems.common.api.domainobjects.TicketType;
import org.com.ems.common.api.domainobjects.validators.constraints.NotNegative;

import jakarta.validation.constraints.NotNull;

public final class Ticket extends AbstractDomainObject {

    private final UUID eventID;
    private final TicketType ticketType;
    private final Integer price;
    private final Boolean transferable;
    private final SeatingInformation seatingInformation;
    // TODO create QR code referenceing this ticket

    public Ticket(final UUID uuid,
		  final Instant createdAt,
		  final Instant lastUpdated,
		  @NotNull final UUID eventID,
		  @NotNull final TicketType ticketType,
		  @NotNull @NotNegative(message = "cannot be negative") final Integer price,
		  @NotNull final Boolean transferable,
		  @NotNull final SeatingInformation seatInfo) {

	super(uuid, createdAt, lastUpdated);
	this.eventID = eventID;
	this.ticketType = ticketType;
	this.price = price;
	this.transferable = transferable;
	this.seatingInformation = seatInfo;

    }

    public UUID getEventID() {

	return this.eventID;

    }

    public TicketType getTicketType() {

	return this.ticketType;

    }

    public Integer getPrice() {

	return this.price;

    }

    public Boolean getTransferable() {

	return this.transferable;

    }

    public SeatingInformation getSeatingInformation() {

	return this.seatingInformation;

    }

    @Override
    public boolean equals(final Object o) {

	if (this == o)
	    return true;
	if (o == null || this.getClass() != o.getClass())
	    return false;

	final Ticket that = (Ticket) o;

	return new EqualsBuilder().appendSuper(super.equals(that)).append(this.eventID, that.eventID)
		.append(this.ticketType, that.ticketType).append(this.price, that.price)
		.append(this.transferable, that.transferable).append(this.seatingInformation, that.seatingInformation).build();

    }

    @Override
    public int hashCode() {

	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.eventID).append(this.ticketType)
		.append(this.price).append(this.transferable).append(this.seatingInformation).build();

    }

    @Override
    public String toString() {

	return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
		.append("eventID", this.eventID).append("ticketType", this.ticketType).append("price", this.price)
		.append("transferable", this.transferable).append("seat information", this.seatingInformation).toString();

    }

}
