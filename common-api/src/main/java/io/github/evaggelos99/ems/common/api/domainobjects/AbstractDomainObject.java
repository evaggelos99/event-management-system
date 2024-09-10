package io.github.evaggelos99.ems.common.api.domainobjects;

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

	private final UUID uuid;
	private final Instant createdAt;
	private final Instant lastUpdated;

	protected AbstractDomainObject(final UUID uuid, final Instant createdAt, final Instant lastUpdated) {

		this.uuid = uuid;
		this.createdAt = createdAt;
		this.lastUpdated = lastUpdated;
	}

	public UUID getUuid() {

		return uuid;

	}

	public Instant getCreatedAt() {

		return createdAt;

	}

	public Instant getLastUpdated() {

		return lastUpdated;

	}

	@Override
	public boolean equals(final Object o) {

		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		final AbstractDomainObject that = (AbstractDomainObject) o;

		return new EqualsBuilder().append(uuid, that.uuid).build();

	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder(17, 37).append(uuid).build();

	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("uuid", uuid)
				.append("createdAt", createdAt).append("lastUpdated", lastUpdated).toString();

	}

}
