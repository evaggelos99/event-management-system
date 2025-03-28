package io.github.evaggelos99.ems.sponsor.api;

import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.NotNegative;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.util.UUID;

public final class Sponsor extends AbstractDomainObject {

    private final String name;
    private final String website;
    private final Integer financialContribution;
    private final ContactInformation contactInformation;

    public Sponsor(final UUID uuid, final Instant createdAt, final Instant lastUpdated, @NotNull final String name,
                   @NotNull final String website, @NotNull @NotNegative final Integer financialContribution,
                   @NotNull final ContactInformation contactInformation) {

        super(uuid, createdAt, lastUpdated);
        this.name = name;
        this.website = website;
        this.financialContribution = financialContribution;
        this.contactInformation = contactInformation;
    }

    public String getName() {

        return name;
    }

    public String getWebsite() {

        return website;
    }

    public Integer getFinancialContribution() {

        return financialContribution;
    }

    public ContactInformation getContactInformation() {

        return contactInformation;
    }

    @Override
    public boolean equals(final Object o) {

        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final Sponsor that = (Sponsor) o;

        return new EqualsBuilder().appendSuper(super.equals(that)).append(name, that.name).append(website, that.website)
                .append(financialContribution, that.financialContribution)
                .append(contactInformation, that.contactInformation).build();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(name).append(website)
                .append(financialContribution).append(contactInformation).build();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
                .append("name", name).append("website", website).append("financialContribution", financialContribution)
                .append("contactInformation", contactInformation).toString();
    }

}
