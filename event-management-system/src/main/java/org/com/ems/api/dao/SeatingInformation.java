package org.com.ems.api.dao;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

/**
 * TODO can we transform this to record?
 *
 * @author Evangelos Georgiou
 *
 */
@Embeddable
public final class SeatingInformation {

	@NotNull
	@Schema(example = "A15", description = "The description of the organizer")
	private String seat;
	@NotNull
	@Schema(example = "West", description = "The description of the organizer")
	private String section;

	protected SeatingInformation() {

	}

	public SeatingInformation(final String seat, final String section) {
		this.seat = requireNonNull(seat);
		this.section = requireNonNull(section);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.seat).append(this.section).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.seat).append(this.section).build();
	}

	@Override
	public boolean equals(final Object object) {

		if (this == object) {
			return true;
		}

		if (object == null) {
			return false;
		}

		if (this.getClass() != object.getClass()) {
			return false;
		}

		final var rhs = (SeatingInformation) object;

		return new EqualsBuilder().append(this.seat, rhs.seat).append(this.section, rhs.section).build();
	}

	public String getSeat() {
		return this.seat;
	}

	public String getSection() {
		return this.section;
	}

}
