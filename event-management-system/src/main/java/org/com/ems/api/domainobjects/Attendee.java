package org.com.ems.api.domainobjects;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

/**
 * Attendee Entity object
 *
 * @author Evangelos Georgiou
 */
@Entity
public final class Attendee extends AbstractDomainObject {

	private static final long serialVersionUID = -7547610739255195353L;

	@NotNull
	@Column(name = "firstName", unique = false, nullable = false, insertable = true, updatable = true)
	@Schema(example = "John", description = "First name of the Attendee")
	private String firstName;
	@NotNull
	@Column(name = "lastName", unique = false, nullable = false, insertable = true, updatable = true)
	@Schema(example = "Smith", description = "Last name of the Attendee")
	private String lastName;
	// OneToMany
	@Schema(description = "The UUIDs of tickets of an event belonging to an Attendee")
	private List<UUID> ticketsIDs;

	protected Attendee() {

	}

	public Attendee(final UUID uuid, @NotNull final String firstName, @NotNull final String lastName,
			final List<UUID> ticketsIDs) {

		super(uuid);
		this.firstName = requireNonNull(firstName);
		this.lastName = requireNonNull(lastName);
		this.ticketsIDs = ticketsIDs;
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.firstName)
				.append(this.lastName).append(this.ticketsIDs).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.firstName).append(this.lastName).append(this.ticketsIDs).build();
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
				.append(this.ticketsIDs, rhs.ticketsIDs).build();
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public Optional<List<UUID>> getTicketsIDs() {
		return Optional.ofNullable(this.ticketsIDs);
	}

}
