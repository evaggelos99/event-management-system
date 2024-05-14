package org.com.ems.api.domainobjects;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Organizer Entity object
 *
 * @author Evangelos Georgiou
 */
public class Organizer extends AbstractDomainObject {

    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    // TODO add regex validation
    private String website;
    @Nullable
    private String description;
    @NotNull
    private List<EventType> eventTypes;
    @NotNull
    private ContactInformation contactInformation;

    public Organizer(final UUID uuid,
		     final Instant lastUpdated,
		     @NotNull @NotBlank final String name,
		     @NotNull @NotBlank final String website,
		     final String description,
		     @NotNull final List<EventType> eventTypes,
		     @NotNull final ContactInformation contactInformation) {

	super(uuid, lastUpdated);
	this.name = name;
	this.website = website;
	this.description = description;
	this.eventTypes = eventTypes;
	this.contactInformation = contactInformation;

    }

    public Organizer() {

    }

    public String getName() {

	return this.name;

    }

    public String getWebsite() {

	return this.website;

    }

    public String getDescription() {

	return this.description;

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
		.append(this.website, that.website).append(this.description, that.description)
		.append(this.eventTypes, that.eventTypes).append(this.contactInformation, that.contactInformation)
		.build();

    }

    @Override
    public int hashCode() {

	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.name).append(this.website)
		.append(this.description).append(this.eventTypes).append(this.contactInformation).build();

    }

    @Override
    public String toString() {

	return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
		.append("name", this.name).append("website", this.website).append("description", this.description)
		.append("eventTypes", this.eventTypes).append("contactInformation", this.contactInformation).toString();

    }

}
