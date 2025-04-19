package io.github.evaggelos99.ems.common.api.service.remote.pojos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Optional;

public class FusionAuthTokenPayload implements ITokenPayload {

    @JsonProperty("access_token")
    private final String accessToken;
    @JsonProperty("expires_in")
    private final Long expiresIn;
    @JsonProperty("token_type")
    private final String tokenType;
    @JsonProperty("userId")
    private final String userId;

    /**
     * C-or
     * @param accessToken the <b>access_token</b> attribute of the payload
     * @param expiresIn the <b>expires_in</b> attribute of the payload
     * @param tokenType the <b>token_type</b> attribute of the payload
     * @param userId the <b>userId</b> attribute of the payload
     */
    public FusionAuthTokenPayload(@JsonProperty("access_token") final String accessToken, @JsonProperty("expires_in") final Long expiresIn, @JsonProperty("token_type") final String tokenType, @JsonProperty("userId") final String userId) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.tokenType = tokenType;
        this.userId = userId;
    }


    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public Optional<String> getRefreshToken() {
        return Optional.empty();
    }

    @Override
    public Long getExpiry() {
        return expiresIn;
    }

    @Override
    public String getTokenType() {
        return tokenType;
    }

    @Override
    public Optional<String> getAudience() {
        return Optional.of(userId);
    }
}
