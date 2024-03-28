package org.com.ems.api.dao;

import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

@Entity
public final class Attendee extends AbstractDAO {

	private static final long serialVersionUID = -6594393457575545824L;

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

		return new ToStringBuilder(ToStringStyle.MULTI_LINE_STYLE).append(this.getUuid()).append(this.firstName)
				.append(this.lastName).append(this.ticket).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.getUuid()).append(this.firstName).append(this.lastName)
				.append(this.ticket).build();
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

		final var rhs = (Attendee) object;

		return new EqualsBuilder().append(this.getUuid(), rhs.getUuid()).append(this.firstName, rhs.firstName)
				.append(this.lastName, rhs.lastName).append(this.ticket, rhs.ticket).build();
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public Optional<Ticket> getTicket() {
		return Optional.ofNullable(this.ticket);
	}

}
