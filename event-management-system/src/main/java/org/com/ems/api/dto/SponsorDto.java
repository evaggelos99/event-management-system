package org.com.ems.api.dto;

import java.sql.Timestamp;
import java.util.UUID;

import org.com.ems.api.domainobjects.ContactInformation;
import org.com.ems.api.domainobjects.validators.constraints.NotNegative;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

/**
 * Sponsor data transfer object
 *
 * @author Evangelos Georgiou
 */
public record SponsorDto(@Schema(hidden = true, description = "The UUID of the Sponsor") UUID id, //
	@Null @Schema(hidden = true) Timestamp createdAt, @Null @Schema(hidden = true) Timestamp lastUpdated, //
	@NotBlank @Schema(example = "RedBull", description = "Name of the Sponsor") String name, //
	@NotBlank @Schema(example = "www.redbull.com", description = "Website of the Sponsor") String website, //
	@NotNegative @Schema(example = "85000", description = "How much money the sponsor gave") Integer financialContribution, //
	@NotNull @Schema(description = "The contact information of the Sponsor") ContactInformation contactInformation) {

}
