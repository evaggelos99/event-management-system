package org.com.ems.api.dao;

import java.util.Collection;
import java.util.Optional;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public final class Organizer extends AbstractDAO {

	private static final long serialVersionUID = -43707763031653832L;

	@NotNull
	@NotBlank
	private String name;
	// TODO ContactInformation object entity
	// TODO website string
	@Nullable
	private String description;
	@NotNull
	private Collection<EventType> eventTypes;

	protected Organizer() {

	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.name)
				.append(this.description).append(this.eventTypes).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.uuid).append(this.name).append(this.description)
				.append(this.eventTypes).build();
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

		final var rhs = (Organizer) object;

		return new EqualsBuilder().append(this.uuid, rhs.uuid).append(this.name, rhs.name)
				.append(this.description, rhs.description).append(this.eventTypes, rhs.eventTypes).build();
	}

	public String getName() {
		return this.name;
	}

	public Optional<String> getDescription() {
		return Optional.ofNullable(this.description);
	}

	public Collection<EventType> getEventTypes() {
		return this.eventTypes;
	}

}
