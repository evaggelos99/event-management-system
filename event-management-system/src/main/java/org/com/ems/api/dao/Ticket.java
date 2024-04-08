package org.com.ems.api.dao;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.com.ems.api.dao.validators.constraints.NotNegative;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

@Entity
public final class Ticket extends AbstractDAO {

	private static final long serialVersionUID = 7755157879519770362L;

	@Nullable
	@Schema(example = "034ce7c5-ccf2-417c-886b-71305349ac80", description = "The uuid of the Attendee")
	private UUID attendeeID;
	@NotNull
	@Schema(example = "61ee465a-f3d8-400a-8ae4-5e806b3eba92", description = "The uuid of the event")
	private UUID eventID;
	@NotNull
	private TicketType ticketType;
	@NotNull
	@Schema(example = "150", description = "The price of the ticket")
	@NotNegative(message = "cannot be negative")
	private Integer price;
	@NotNull
	@Schema(example = "true", description = "The ticket is transferable")
	private Boolean transferable;
	@NotNull
	@Embedded
	@Schema(description = "The SeatingInformation of the ticket")
	private SeatingInformation seatInfo;
	// TODO create QR code referenceing this ticket

	protected Ticket() {

	}

	public Ticket(final UUID uuid, final UUID attendeeID, @NotNull final UUID eventID,
			@NotNull final TicketType ticketType, @NotNull final Integer price, @NotNull final Boolean transferable,
			@NotNull final SeatingInformation seatInfo) {

		super(uuid);
		this.attendeeID = attendeeID;
		this.eventID = requireNonNull(eventID);
		this.ticketType = requireNonNull(ticketType);
		this.price = requireNonNull(price);
		this.transferable = requireNonNull(transferable);
		this.seatInfo = requireNonNull(seatInfo);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.attendeeID)
				.append(this.eventID).append(this.ticketType).append(this.price).append(this.transferable)
				.append(this.seatInfo).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.attendeeID).append(this.eventID).append(this.ticketType)
				.append(this.price).append(this.transferable).append(this.seatInfo).build();
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

		return new EqualsBuilder().append(this.attendeeID, rhs.attendeeID).append(this.eventID, rhs.eventID)
				.append(this.ticketType, rhs.ticketType).append(this.price, rhs.price)
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
