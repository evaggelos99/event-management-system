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

    final protected UUID uuid;
    final protected Instant createdAt;
    final protected Instant lastUpdated;

    public AbstractDomainObject(final UUID uuid,
				final Instant createdAt,
				final Instant lastUpdated) {

	this.uuid = uuid;
	this.createdAt = createdAt;
	this.lastUpdated = lastUpdated;

    }

    public UUID getUuid() {

	return this.uuid;

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

	return new EqualsBuilder().append(this.uuid, that.uuid).build();

    }

    @Override
    public int hashCode() {

	return new HashCodeBuilder(17, 37).append(this.uuid).build();

    }

    @Override
    public String toString() {

	return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("uuid", this.uuid).toString();

    }

}
