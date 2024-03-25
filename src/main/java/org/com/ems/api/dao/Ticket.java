package org.com.ems.api.dao;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

@Entity
public final class Ticket extends AbstractDAO {

	private static final long serialVersionUID = 7755157879519770362L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID UUID;
	@Null
	private UUID attendeeID; // might not have an attendee at first
	@NotNull
	private UUID eventID;
	@NotNull
	private TicketType ticketType;
	@NotNull
	private Integer price;
	@NotNull
	private Boolean transferable;
	@NotNull
	private SeatingInformation seatInfo;

	protected Ticket() {

	}

	@Override
	public String toString() {

		return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE).append(UUID).append(attendeeID).append(eventID)
				.append(ticketType).append(price).append(transferable).append(seatInfo).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(UUID).append(attendeeID).append(eventID).append(ticketType).append(price)
				.append(transferable).append(seatInfo).build();
	}

	@Override
	public boolean equals(Object object) {

		if (this == object) {
			return true;
		}

		if (object == null) {
			return false;
		}

		if (getClass() != object.getClass()) {
			return false;
		}

		var rhs = (Ticket) object;

		return new EqualsBuilder().append(this.UUID, rhs.UUID).append(this.attendeeID, rhs.attendeeID)
				.append(this.eventID, rhs.eventID).append(this.ticketType, rhs.ticketType).append(this.price, rhs.price)
				.append(this.transferable, rhs.transferable).append(this.seatInfo, rhs.seatInfo).build();
	}

	public UUID getUUID() {
		return UUID;
	}

	public Optional<UUID> getAttendeeID() {
		return Optional.ofNullable(attendeeID);
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

	public Boolean isTransferable() {
		return transferable;
	}

	public SeatingInformation getSeatInfo() {
		return seatInfo;
	}

}
