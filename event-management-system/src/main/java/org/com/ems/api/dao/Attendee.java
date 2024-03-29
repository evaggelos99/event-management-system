package org.com.ems.api.dao;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.annotation.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.validation.constraints.NotNull;

@Entity
public final class Attendee extends AbstractDAO {

	private static final long serialVersionUID = -6594393457575545824L;

	@NotNull
	@Column(name = "firstName", unique = false, nullable = false, insertable = true, updatable = true)
	private String firstName;
	@NotNull
	@Column(name = "lastName", unique = false, nullable = false, insertable = true, updatable = true)
	private String lastName;
	@OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	@Nullable
	@PrimaryKeyJoinColumn
	private Ticket ticket;

	protected Attendee() {

	}

	public Attendee(@NotNull final String firstName, @NotNull final String lastName, final Ticket ticket) {

		this.firstName = requireNonNull(firstName);
		this.lastName = requireNonNull(lastName);
		this.ticket = ticket;
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.firstName)
				.append(this.lastName).append(this.ticket).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.firstName).append(this.lastName).append(this.ticket).build();
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

		return new EqualsBuilder().append(this.firstName, rhs.firstName).append(this.lastName, rhs.lastName)
				.append(this.ticket, rhs.ticket).build();
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
