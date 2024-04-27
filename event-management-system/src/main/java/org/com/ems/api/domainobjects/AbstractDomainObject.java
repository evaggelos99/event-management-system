package org.com.ems.api.domainobjects;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

/**
 * Super class for Entity classes
 *
 * @author Evangelos Georgiou
 */
@MappedSuperclass
public abstract class AbstractDomainObject implements Serializable {

    private static final long serialVersionUID = 2114216090280817221L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    protected UUID uuid;
    @UpdateTimestamp
    protected Instant lastUpdated;

    public AbstractDomainObject(final UUID uuid,
				final Instant lastUpdated) {

	this.uuid = uuid;
	this.lastUpdated = lastUpdated;

    }

    protected AbstractDomainObject() {

    }

    public UUID getUuid() {

	return this.uuid;

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
