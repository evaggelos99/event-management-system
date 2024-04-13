package org.com.ems.api.domainobjects;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Organizer Entity object
 *
 * @author Evangelos Georgiou
 */
@Entity
public final class Organizer extends AbstractDomainObject {

	private static final long serialVersionUID = -43707763031653832L;

	@NotNull
	@NotBlank
	@Column(unique = true)
	@Schema(example = "SpecialWeddings", description = "The limit of the event")
	private String name;
	@NotNull
	@NotBlank
	@Column(unique = true) // TODO add regex validation
	@Schema(example = "www.google.com", description = "The website of the organizer")
	private String website;
	@Nullable
	@Schema(example = "Organizer specialized in weddings taking place in the country or even outside!", description = "The description of the organizer")
	private String description;
	@NotNull
	@Schema(description = "The event types of the organizer plans")
	private Collection<EventType> eventTypes;
	@NotNull
	@Schema(description = "The contact information of the Organizer")
	private ContactInformation contactInformation;

	protected Organizer() {

	}

	public Organizer(final UUID uuid, @NotNull @NotBlank final String name, @NotNull @NotBlank final String website,
			final String description, @NotNull final Collection<EventType> eventTypes,
			@NotNull final ContactInformation contactInformation) {

		super(uuid);
		this.name = requireNonNull(name);
		this.website = requireNonNull(website);
		this.description = description;
		this.eventTypes = requireNonNull(new ArrayList<>(eventTypes));
		this.contactInformation = requireNonNull(contactInformation);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.name)
				.append(this.description).append(this.eventTypes).append(this.website).append(this.contactInformation)
				.build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.name).append(this.description).append(this.eventTypes)
				.append(this.website).append(this.contactInformation).build();
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
				.append(this.eventTypes, rhs.eventTypes).append(this.website, rhs.website)
				.append(this.contactInformation, rhs.contactInformation).build();
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

	public ContactInformation getContactInformation() {
		return this.contactInformation;
	}

}
