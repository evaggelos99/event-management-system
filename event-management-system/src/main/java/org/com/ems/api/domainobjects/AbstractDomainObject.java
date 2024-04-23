package org.com.ems.api.domainobjects;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.UpdateTimestamp;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Super class for Entity classes
 *
 * @author Evangelos Georgiou
 */
@MappedSuperclass
@Getter
@EqualsAndHashCode(exclude = { "updatedTimestamp" })
@ToString
@NoArgsConstructor
public abstract class AbstractDomainObject implements Serializable {

	private static final long serialVersionUID = 2518014933571661892L;

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Schema(description = "the uuid of the dao object", hidden = true)
	protected UUID uuid;
	@UpdateTimestamp
	@Schema(description = "the update timestamp of the dao object", hidden = true)
	protected Instant updatedTimestamp;

}
