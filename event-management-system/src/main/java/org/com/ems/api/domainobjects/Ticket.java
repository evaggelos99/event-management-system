package org.com.ems.api.domainobjects;

import java.time.Instant;
import java.util.UUID;

import org.com.ems.api.domainobjects.validators.constraints.NotNegative;

import jakarta.validation.constraints.NotNull;

/**
 * Ticket Entity object
 *
 * @author Evangelos Georgiou
 */
public class Ticket extends AbstractDomainObject {

    @NotNull
    private UUID eventID;
    @NotNull
    private TicketType ticketType;
    @NotNull
    @NotNegative(message = "cannot be negative")
    private Integer price;
    @NotNull
    private Boolean transferable;
    @NotNull
    private SeatingInformation seatInfo;
    // TODO create QR code referenceing this ticket

    public Ticket(final UUID uuid,
		  final Instant lastUpdated,
		  @NotNull final UUID eventID,
		  @NotNull final TicketType ticketType,
		  @NotNull @NotNegative(message = "cannot be negative") final Integer price,
		  @NotNull final Boolean transferable,
		  @NotNull final SeatingInformation seatInfo) {

	super(uuid, lastUpdated);
	this.eventID = eventID;
	this.ticketType = ticketType;
	this.price = price;
	this.transferable = transferable;
	this.seatInfo = seatInfo;

    }

    public Ticket() {

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

    public SeatingInformation getSeatInfo() {

	return this.seatInfo;

    }

}
