package org.com.ems.api.domainobjects;

import java.time.Instant;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Super class for domain object classes
 *
 * @author Evangelos Georgiou
 */
public abstract class AbstractDomainObject {

    private final UUID id;
    private final Instant createdAt;
    private final Instant lastUpdated;

    protected AbstractDomainObject(final UUID id,
				   final Instant createdAt,
				   final Instant lastUpdated) {

	this.id = id;
	this.createdAt = createdAt;
	this.lastUpdated = lastUpdated;

    }

    public UUID getId() {

	return this.id;

    }

    public Instant getCreatedAt() {

	return this.createdAt;

    }

    public Instant getLastUpdated() {

	return this.lastUpdated;

    }

    @Override
    public boolean equals(final Object o) {

	if (this == o)
	    return true;
	if (o == null || this.getClass() != o.getClass())
	    return false;

	final AbstractDomainObject that = (AbstractDomainObject) o;

	return new EqualsBuilder().append(this.id, that.id).build();

    }

    @Override
    public int hashCode() {

	return new HashCodeBuilder(17, 37).append(this.id).build();

    }

    @Override
    public String toString() {

	return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("uuid", this.id).toString();

    }

}
