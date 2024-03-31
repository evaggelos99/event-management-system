package org.com.ems.api.dao;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public final class Organizer extends AbstractDAO {

	private static final long serialVersionUID = -43707763031653832L;

	@NotNull
	@NotBlank
	@Column(unique = true)
	private String name;
	// TODO ContactInformation object entity
	@NotNull
	@NotBlank
	@Column(unique = true) // TODO add regex validation
	private String website;
	@Nullable
	private String description;
	@NotNull
	private Collection<EventType> eventTypes;

	protected Organizer() {

	}

	public Organizer(final UUID uuid, @NotNull @NotBlank final String name, @NotNull @NotBlank final String website,
			final String description, @NotNull final Collection<EventType> eventTypes) {

		super(uuid);
		this.name = requireNonNull(name);
		this.website = requireNonNull(website);
		this.description = description;
		this.eventTypes = Objects.requireNonNull(new ArrayList<>(eventTypes));
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.name)
				.append(this.description).append(this.eventTypes).append(this.website).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.name).append(this.description).append(this.eventTypes)
				.append(this.website).build();
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

		return new EqualsBuilder().append(this.name, rhs.name).append(this.description, rhs.description)
				.append(this.eventTypes, rhs.eventTypes).append(this.website, rhs.website).build();
	}

	public String getName() {
		return this.name;
	}

	public Optional<String> getDescription() {
		return Optional.ofNullable(this.description);
	}

	public Collection<EventType> getEventTypes() {
		return Collections.unmodifiableCollection(this.eventTypes);
	}

	public String getWebsite() {
		return this.website;
	}

}
