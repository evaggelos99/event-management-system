package io.github.evaggelos99.ems.sponsor.api;

import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.validators.constraints.NotNegative;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;

import java.time.Instant;
import java.util.UUID;

/**
 * Sponsor Entity object
 *
 * @author Evangelos Georgiou
 */
public record SponsorDto(@Schema(hidden = true, description = "The UUID of the Attendee") UUID uuid, //
                         @Null @Schema(hidden = true) Instant createdAt, //
                         @Null @Schema(hidden = true) Instant lastUpdated, //
                         @NotBlank @Schema(example = "dolore", description = "Name of the Sponsor") String name, //
                         @NotBlank @Schema(example = "www.deserunt.com", description = "Website of the Sponsor") String website,
                         //
                         @NotNegative @Schema(example = "85000", description = "How much money the sponsor gave") Integer financialContribution,
                         //
                         @NotNull @Schema(description = "The contact information of the Sponsor") ContactInformation contactInformation) {

}
