package io.github.evaggelos99.ems.common.api.domainobjects;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record SeatingInformation(@NotNull @Schema(example = "A15", description = "The number of the seat") String seat,
                                 @NotNull @Schema(example = "West", description = "Which section your seat is located in") String section) {

}
