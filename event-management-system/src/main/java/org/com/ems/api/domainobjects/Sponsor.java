package org.com.ems.api.domainobjects;

import org.com.ems.api.domainobjects.validators.constraints.NotNegative;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Sponsor Entity object
 *
 * @author Evangelos Georgiou
 */
@Builder
@NoArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true, doNotUseGetters = true)
@ToString
@Entity
public class Sponsor extends AbstractDomainObject {

	private static final long serialVersionUID = -492542139717101362L;

	@NotNull
	private String name;
	@NotNull
	private String website;
	@NotNull
	@NotNegative
	private Integer financialContribution;
	@NotNull
	private ContactInformation contactInformation;

	public Sponsor(@NotNull final String name, @NotNull final String website,
			@NotNull @NotNegative final Integer financialContribution,
			@NotNull final ContactInformation contactInformation) {

		super();
		this.name = name;
		this.website = website;
		this.financialContribution = financialContribution;
		this.contactInformation = contactInformation;
	}

}
