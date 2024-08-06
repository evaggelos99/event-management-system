package com.github.evaggelos99.ems.attendee.api;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;

import jakarta.validation.constraints.NotNull;

public final class Attendee extends AbstractDomainObject {

    private final String firstName;
    private final String lastName;
    private final List<UUID> ticketIDs;

    public Attendee(final UUID uuid,
		    final Instant createdAt,
		    final Instant lastUpdated,
		    @NotNull final String firstName,
		    @NotNull final String lastName,
		    final List<UUID> ticketIDs) {

	super(uuid, createdAt, lastUpdated);
	this.firstName = firstName;
	this.lastName = lastName;
	this.ticketIDs = ticketIDs;

    }

    public String getFirstName() {

	return this.firstName;

    }

    public String getLastName() {

	return this.lastName;

    }

    public List<UUID> getTicketIDs() {

	return this.ticketIDs;

    }

    @Override
    public boolean equals(final Object o) {

	if (this == o)
	    return true;
	if (o == null || this.getClass() != o.getClass())
	    return false;

	final Attendee that = (Attendee) o;

	return new EqualsBuilder().appendSuper(super.equals(that)).append(this.firstName, that.firstName)
		.append(this.lastName, that.lastName).append(this.ticketIDs, that.ticketIDs).build();

    }

    @Override
    public int hashCode() {

	return new HashCodeBuilder(17, 37).appendSuper(super.hashCode()).append(this.firstName).append(this.lastName)
		.append(this.ticketIDs).build();

    }

    @Override
    public String toString() {

	return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).appendSuper(super.toString())
		.append("firstName", this.firstName).append("lastName", this.lastName)
		.append("ticketIDs", this.ticketIDs).toString();

    }

}
