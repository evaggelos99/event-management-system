package io.github.evaggelos99.ems.organizer.api;

import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public final class Organizer extends AbstractDomainObject {

    private final String name;
    private final String website;
    private final String information;
    private final List<EventType> eventTypes;
    private final ContactInformation contactInformation;

    public Organizer(final UUID uuid, final OffsetDateTime createdAt, final OffsetDateTime lastUpdated,
                     @NotNull @NotBlank final String name, @NotBlank final String website, final String information,
                     @NotNull final List<EventType> eventTypes, @NotNull final ContactInformation contactInformation) {

        super(uuid, createdAt, lastUpdated);
        this.name = name;
        this.website = website;
        this.information = information;
        this.eventTypes = eventTypes;
        this.contactInformation = contactInformation;
    }

    public String getName() {

        return name;
    }

    public String getWebsite() {

        return website;
    }

    public String getInformation() {

        return information;
    }

    public List<EventType> getEventTypes() {

        return Collections.unmodifiableList(eventTypes);
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

        final Organizer that = (Organizer) o;

        return new EqualsBuilder().appendSuper(super.equals(that)).append(name, that.name).append(website, that.website)
                .append(information, that.information).append(eventTypes, that.eventTypes)
                .append(contactInformation, that.contactInformation).build();
    }

    @Override
    public int hashCode() {

        return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(name).append(website)
                .append(information).append(eventTypes).append(contactInformation).build();
    }

    @Override
    public String toString() {

        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
                .append("name", name).append("website", website).append("information", information)
                .append("eventTypes", eventTypes).append("contactInformation", contactInformation).toString();
    }

}
