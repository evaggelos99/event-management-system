package io.github.evaggelos99.ems.common.api.service.remote.pojos;

import java.util.Optional;

public class FusionAuthTokenPayload implements ITokenPayload {

    private final String accessToken;
    private final Long expiresIn;
    private final String tokenType;
    private final String userId;

    /**
     * C-or
     * @param accessToken the <b>access_token</b> attribute of the payload
     * @param expiresIn the <b>expires_in</b> attribute of the payload
     * @param tokenType the <b>token_type</b> attribute of the payload
     * @param userId the <b>userId</b> attribute of the payload
     */
    public FusionAuthTokenPayload(final String accessToken, final Long expiresIn, final String tokenType, final String userId) {
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
