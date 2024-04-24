package org.com.ems.api.domainobjects;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

/**
 * Attendee Entity object
 *
 * @author Evangelos Georgiou
 */
@Entity
public class Attendee extends AbstractDomainObject {

	private static final long serialVersionUID = 8307839595089444000L;

	@NotNull
	@Column(name = "firstName", unique = false, nullable = false, insertable = true, updatable = true)
	private String firstName;
	@NotNull
	@Column(name = "lastName", unique = false, nullable = false, insertable = true, updatable = true)
	private String lastName;
	// OneToMany
	private Collection<UUID> ticketIDs;

	public Attendee(final UUID uuid, final Instant lastUpdated, @NotNull final String firstName,
			@NotNull final String lastName, final Collection<UUID> ticketIDs) {

		super(uuid, lastUpdated);
		this.firstName = firstName;
		this.lastName = lastName;
		this.ticketIDs = ticketIDs;
	}

	public Attendee() {

	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public Collection<UUID> getTicketIDs() {
		return this.ticketIDs;
	}

	@Override
	public boolean equals(final Object o) {

		if (this == o)
			return true;
		if (o == null || this.getClass() != o.getClass())
			return false;

		final Attendee that = (Attendee) o;

		return new EqualsBuilder().appendSuper(super.equals(that)).append(this.firstName, that.firstName)
				.append(this.lastName, that.lastName).append(this.ticketIDs, that.ticketIDs).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.firstName).append(this.lastName)
				.append(this.ticketIDs).toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
				.append("firstName", this.firstName).append("lastName", this.lastName)
				.append("ticketIDs", this.ticketIDs).toString();
	}

}
