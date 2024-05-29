package org.com.ems.api.domainobjects;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class Organizer extends AbstractDomainObject {

    private final String name;
    // TODO add regex validation
    private final String website;
    private final String information;
    private final List<EventType> eventTypes;
    private final ContactInformation contactInformation;

    public Organizer(final UUID id,
		     final Instant createdAt,
		     final Instant lastUpdated,
		     final String name,
		     final String website,
		     final String description,
		     final List<EventType> eventTypes,
		     final ContactInformation contactInformation) {

	super(id, createdAt, lastUpdated);
	this.name = name;
	this.website = website;
	this.information = description;
	this.eventTypes = eventTypes;
	this.contactInformation = contactInformation;

    }

    public String getName() {

	return this.name;

    }

    public String getWebsite() {

	return this.website;

    }

    public String getInformation() {

	return this.information;

    }

    public List<EventType> getEventTypes() {

	return this.eventTypes;

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

	final Organizer that = (Organizer) o;

	return new EqualsBuilder().appendSuper(super.equals(that)).append(this.name, that.name)
		.append(this.website, that.website).append(this.information, that.information)
		.append(this.eventTypes, that.eventTypes).append(this.contactInformation, that.contactInformation)
		.build();

    }

    @Override
    public int hashCode() {

	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.name).append(this.website)
		.append(this.information).append(this.eventTypes).append(this.contactInformation).build();

    }

    @Override
    public String toString() {

	return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
		.append("name", this.name).append("website", this.website).append("information", this.information)
		.append("eventTypes", this.eventTypes).append("contactInformation", this.contactInformation).toString();

    }

}
