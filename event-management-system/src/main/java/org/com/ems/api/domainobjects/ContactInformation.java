package org.com.ems.api.domainobjects;

import static java.util.Objects.requireNonNull;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;

/**
 * ContactInformation Embeddable object
 *
 * @author Evangelos Georgiou
 */
@Embeddable
public final class ContactInformation {

	// TODO add regex validation for email
	@NotNull
	@Schema(example = "example@domain.com", description = "An email address belonging to an entity")
	private String email;
	@NotNull
	@Schema(example = "70493729392", description = "A phone number belonging to an entity")
	private Long phoneNumber;
	@NotNull
	@Schema(example = "308 Negra Arroyo Lane, Albuquerque, New Mexico.", description = "A phone number belonging to an entity")
	private String physicalAddress;

	protected ContactInformation() {

	}

	public ContactInformation(@NotNull final String email, @NotNull final Long phoneNumber,
			@NotNull final String physicalAddress) {

		this.email = requireNonNull(email);
		this.phoneNumber = requireNonNull(phoneNumber);
		this.physicalAddress = requireNonNull(physicalAddress);
	}

	@Override
	public String toString() {

		return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append(this.email).append(this.phoneNumber)
				.append(this.physicalAddress).build();
	}

	@Override
	public int hashCode() {

		return new HashCodeBuilder().append(this.email).append(this.phoneNumber).append(this.physicalAddress).build();
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

		final var rhs = (ContactInformation) object;

		return new EqualsBuilder().append(this.email, rhs.email).append(this.phoneNumber, rhs.phoneNumber)
				.append(this.physicalAddress, rhs.physicalAddress).build();
	}

	public String getEmail() {
		return this.email;
	}

	public Long getPhoneNumber() {
		return this.phoneNumber;
	}

	public String getPhysicalAddress() {
		return this.physicalAddress;
	}

}
