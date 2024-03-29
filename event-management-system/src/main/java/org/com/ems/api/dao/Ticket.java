package org.com.ems.api.dao;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public final class Ticket extends AbstractDAO {

	private static final long serialVersionUID = 7755157879519770362L;

	@Nullable
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
	@Embedded
	private SeatingInformation seatInfo;

	protected Ticket() {

	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.attendeeID)
				.append(this.eventID).append(this.ticketType).append(this.price).append(this.transferable)
				.append(this.seatInfo).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.uuid).append(this.attendeeID).append(this.eventID)
				.append(this.ticketType).append(this.price).append(this.transferable).append(this.seatInfo).build();
	}

	@Override
	public boolean equals(final Object object) {

		if (this == object) {
			return true;
		}

		if (object == null) {
			return false;
		}

		if (this.getClass() != object.getClass()) {
			return false;
		}

		final var rhs = (Ticket) object;

		return new EqualsBuilder().append(this.uuid, rhs.uuid).append(this.attendeeID, rhs.attendeeID)
				.append(this.eventID, rhs.eventID).append(this.ticketType, rhs.ticketType).append(this.price, rhs.price)
				.append(this.transferable, rhs.transferable).append(this.seatInfo, rhs.seatInfo).build();
	}

	public Optional<UUID> getAttendeeID() {
		return Optional.ofNullable(this.attendeeID);
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

	public Boolean isTransferable() {
		return this.transferable;
	}

	public SeatingInformation getSeatInfo() {
		return this.seatInfo;
	}

}
