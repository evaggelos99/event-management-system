package org.com.ems.api.domainobjects;

import java.util.Collection;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Organizer Entity object
 *
 * @author Evangelos Georgiou
 */
@Builder
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString(callSuper = true)
@Entity
public class Organizer extends AbstractDomainObject {

	private static final long serialVersionUID = -757558119640858388L;

	@NotNull
	@NotBlank
	@Column(unique = true)
	private String name;
	@NotNull
	@NotBlank
	@Column(unique = true) // TODO add regex validation
	private String website;
	@Nullable
	private String description;
	@NotNull
	private Collection<EventType> eventTypes;
	@NotNull
	private ContactInformation contactInformation;

	public Organizer(@NotNull @NotBlank final String name, @NotNull @NotBlank final String website,
			final String description, @NotNull final Collection<EventType> eventTypes,
			@NotNull final ContactInformation contactInformation) {

		super();
		this.name = name;
		this.website = website;
		this.description = description;
		this.eventTypes = eventTypes;
		this.contactInformation = contactInformation;
	}

}
