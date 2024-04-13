package org.com.ems.api.domainobjects;

import static java.util.Objects.requireNonNull;

import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.com.ems.api.domainobjects.validators.constraints.NotNegative;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

/**
 * Sponsor Entity object
 *
 * @author Evangelos Georgiou
 */
@Entity
public final class Sponsor extends AbstractDomainObject {

	private static final long serialVersionUID = -6141027675140387427L;

	@NotNull
	@Schema(example = "RedBull", description = "Name of the Sponsor")
	private String name;
	@NotNull
	@Schema(example = "www.redbull.com", description = "Website of the Sponsor")
	private String website;
	@NotNull
	@NotNegative
	@Schema(example = "85000", description = "How much money the sponsor gave")
	private Integer financialContribution;
	@NotNull
	@Schema(description = "The contact information of the Sponsor")
	private ContactInformation contactInformation;

	protected Sponsor() {

	}

	public Sponsor(final UUID uuid, @NotNull final String name, @NotNull final String website,
			@NotNull final Integer financialContribution, @NotNull final ContactInformation contactInformation) {

		super(uuid);
		this.name = requireNonNull(name);
		this.website = requireNonNull(website);
		this.financialContribution = requireNonNull(financialContribution);
		this.contactInformation = requireNonNull(contactInformation);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.uuid).append(this.name)
				.append(this.website).append(this.financialContribution).append(this.contactInformation).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.name).append(this.website).append(this.financialContribution)
				.append(this.contactInformation).build();
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

		final var rhs = (Sponsor) object;

		return new EqualsBuilder().append(this.name, rhs.name).append(this.website, rhs.website)
				.append(this.financialContribution, rhs.financialContribution)
				.append(this.contactInformation, rhs.contactInformation).build();
	}

	public String getName() {
		return this.name;
	}

	public String getWebsite() {
		return this.website;
	}

	public Integer getFinancialContribution() {
		return this.financialContribution;
	}

	public ContactInformation getContactInformation() {
		return this.contactInformation;
	}

}
