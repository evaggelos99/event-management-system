package org.com.ems.api.dao;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.UUID;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractDAO implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Schema(example = "1f0ae3a4-9843-4135-bad9-295736330f20", description = "the uuid of the dao object")
	protected UUID uuid;

	protected AbstractDAO() {

	}

	public AbstractDAO(final UUID uuid) {

		this.uuid = requireNonNull(uuid);
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
