package org.com.ems.api.dto;

import java.sql.Timestamp;
import java.util.UUID;

import org.com.ems.api.domainobjects.ContactInformation;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Sponsor Entity object
 *
 * @author Evangelos Georgiou
 */
public record SponsorDto(@Schema(hidden = true) UUID uuid, @Schema(hidden = true) Timestamp lastUpdated,
	@Schema(example = "RedBull", description = "Name of the Sponsor") String name,
	@Schema(example = "www.redbull.com", description = "Website of the Sponsor") String website,
	@Schema(example = "85000", description = "How much money the sponsor gave") Integer financialContribution,
	@Schema(description = "The contact information of the Sponsor") ContactInformation contactInformation) {

}
