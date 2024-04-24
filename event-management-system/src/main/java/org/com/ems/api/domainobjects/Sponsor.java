package org.com.ems.api.domainobjects;

import java.time.Instant;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.com.ems.api.domainobjects.validators.constraints.NotNegative;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;

/**
 * Sponsor Entity object
 *
 * @author Evangelos Georgiou
 */
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

	public Sponsor(final UUID uuid, final Instant lastUpdated, @NotNull final String name,
			@NotNull final String website, @NotNull @NotNegative final Integer financialContribution,
			@NotNull final ContactInformation contactInformation) {

		super(uuid, lastUpdated);
		this.name = name;
		this.website = website;
		this.financialContribution = financialContribution;
		this.contactInformation = contactInformation;
	}

	public Sponsor() {
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

	@Override
	public boolean equals(final Object o) {
		if (this == o)
			return true;
		if (o == null || this.getClass() != o.getClass())
			return false;

		final Sponsor that = (Sponsor) o;

		return new EqualsBuilder().appendSuper(super.equals(that)).append(this.name, that.name)
				.append(this.website, that.website).append(this.financialContribution, that.financialContribution)
				.append(this.contactInformation, that.contactInformation).build();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.name).append(this.website)
				.append(this.financialContribution).append(this.contactInformation).toHashCode();
	}

	@Override
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
				.append("name", this.name).append("website", this.website)
				.append("financialContribution", this.financialContribution)
				.append("contactInformation", this.contactInformation).toString();
	}

}
