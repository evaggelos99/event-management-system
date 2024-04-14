package org.com.ems.api.domainobjects;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
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

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Schema(description = "the uuid of the dao object", hidden = true)
	protected UUID uuid;
	@UpdateTimestamp
	@Schema(description = "the update timestamp of the dao object", hidden = true)
	private Instant updatedTimestamp;

	protected AbstractDomainObject() {

	}

	public AbstractDomainObject(final UUID uuid) {

		this.uuid = requireNonNull(uuid);
	}

	public Instant getUpdatedTimestamp() {
		return this.updatedTimestamp;
	}

	public final UUID getUuid() {
		return this.uuid;
	}

	@Override
	public abstract String toString();

	@Override
	public abstract int hashCode();

	@Override
	public abstract boolean equals(Object object);

}
