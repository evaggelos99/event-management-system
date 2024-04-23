package org.com.ems.api.domainobjects;

import java.util.UUID;

import org.com.ems.api.domainobjects.validators.constraints.NotNegative;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Ticket Entity object
 *
 * @author Evangelos Georgiou
 */
@Builder
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString
@Entity
public class Ticket extends AbstractDomainObject {

	private static final long serialVersionUID = -8096585962117403667L;

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
	@Embedded
	private SeatingInformation seatInfo;

	// TODO create QR code referenceing this ticket
	public Ticket(@NotNull final UUID eventID, @NotNull final TicketType ticketType,
			@NotNull @NotNegative(message = "cannot be negative") final Integer price,
			@NotNull final Boolean transferable, @NotNull final SeatingInformation seatInfo) {

		super();
		this.eventID = eventID;
		this.ticketType = ticketType;
		this.price = price;
		this.transferable = transferable;
		this.seatInfo = seatInfo;
	}

}
