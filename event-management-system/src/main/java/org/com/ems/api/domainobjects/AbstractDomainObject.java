package org.com.ems.api.domainobjects;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;

/**
 * Super class for Entity classes
 *
 * @author Evangelos Georgiou
 */
@MappedSuperclass
public abstract class AbstractDomainObject implements Serializable {

	private static final long serialVersionUID = 2518014933571661892L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Schema(description = "the uuid of the dao object", hidden = true)
	protected UUID uuid;
	@Version
	@Schema(description = "the last updated timestamp of the dao object", hidden = true)
	protected Instant lastUpdated;

	public AbstractDomainObject(final UUID uuid, final Instant lastUpdated) {

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

		return new HashCodeBuilder(17, 37).append(this.uuid).toHashCode();
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("uuid", this.uuid).toString();
	}

}
