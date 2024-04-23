package org.com.ems.api.domainobjects;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Attendee Entity object
 *
 * @author Evangelos Georgiou
 */
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString
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
	private List<UUID> ticketsIDs;

	public Attendee(@NotNull final String firstName, @NotNull final String lastName, final List<UUID> ticketsIDs) {

		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.ticketsIDs = ticketsIDs;
	}

}
