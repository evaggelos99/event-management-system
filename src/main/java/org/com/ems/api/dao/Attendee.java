package org.com.ems.api.dao;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

@Entity
public final class Attendee extends AbstractDAO {

	private static final long serialVersionUID = -6594393457575545824L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID uuid;
	@NotNull
	private String firstName;
	@NotNull
	private String lastName;
	@OneToOne(cascade = CascadeType.ALL)
	@Null
	private Ticket ticket;

	protected Attendee() {

	}

	@Override
	public String toString() {

		return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE).append(uuid).append(firstName).append(lastName)
				.append(ticket).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(uuid).append(firstName).append(lastName).append(ticket).build();
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

		var rhs = (Attendee) object;

		return new EqualsBuilder().append(this.uuid, rhs.uuid).append(this.firstName, rhs.firstName)
				.append(this.lastName, rhs.lastName).append(this.ticket, rhs.ticket).build();
	}

	public UUID getUuid() {
		return uuid;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Optional<Ticket> getTicket() {
		return Optional.ofNullable(ticket);
	}

}
