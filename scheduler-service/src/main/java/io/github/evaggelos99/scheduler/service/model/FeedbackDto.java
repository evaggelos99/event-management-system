package io.github.evaggelos99.scheduler.service.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record FeedbackDto(
        @Min(1) @Max(10) Integer rating,
        @NotBlank String likedMost,
        @NotBlank String whatCanBeImproved,
        @NotBlank String otherComments) {

}
