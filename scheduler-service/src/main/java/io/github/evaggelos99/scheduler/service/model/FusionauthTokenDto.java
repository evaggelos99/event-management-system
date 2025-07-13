package io.github.evaggelos99.scheduler.service.model;

import com.fasterxml.jackson.annotation.JsonAlias;

public record FusionauthTokenDto(
        @JsonAlias("access_token") String accessToken,
        @JsonAlias("expires_in") String expiresIn,
        @JsonAlias("token_type") String tokenType,
        @JsonAlias("userId") String userId) {

}
