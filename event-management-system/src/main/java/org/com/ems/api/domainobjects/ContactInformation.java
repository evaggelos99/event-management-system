package org.com.ems.api.domainobjects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record ContactInformation(
	@NotNull @Schema(example = "example@domain.com", description = "An email address belonging to an entity") String email,
	@NotNull @Schema(example = "70493729392", description = "A phone number belonging to an entity") String phoneNumber,
	@NotNull @Schema(example = "308 Negra Arroyo Lane, Albuquerque, New Mexico.", description = "A phone number belonging to an entity") String physicalAddress) {

}
