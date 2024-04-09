package org.com.ems.api.dao;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
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
public abstract class AbstractDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Schema(example = "1f0ae3a4-9843-4135-bad9-295736330f20", description = "the uuid of the dao object")
	protected UUID uuid;
	@CreationTimestamp
	@Schema(description = "the creation timestamp of the dao object", hidden = true)
	private Instant timestamp;
	@UpdateTimestamp
	@Schema(description = "the update timestamp of the dao object", hidden = true)
	private Instant updatedOn;

	protected AbstractDAO() {

	}

	public AbstractDAO(final UUID uuid) {

		this.uuid = requireNonNull(uuid);
	}

	public Instant getTimestamp() {
		return this.timestamp;
	}

	public Instant getUpdatedOn() {
		return this.updatedOn;
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
