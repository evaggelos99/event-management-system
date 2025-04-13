package io.github.evaggelos99.ems.common.api.service.remote.pojos;

import io.github.evaggelos99.ems.common.api.service.remote.ITokenRetriever;

import java.util.Optional;

/**
 * Interface that is returned when invoking a {@link ITokenRetriever} class
 */
public interface ITokenPayload {

    /**
     * The JWT that is used to authenticate
     * @return the JWT or access token
     */
    String getAccessToken();

    /**
     * The JWT or access token that is used to refresh the existing token
     * @return a JWT or access token, <b>MAYBE</b> NULL
     */
    Optional<String> getRefreshToken();

    /**
     * How long in seconds usually the token is valid for
     * @return seconds in {@link Long} type
     */
    Long getExpiry();

    /**
     * Whenether the token is bearer or opaque
     * @return the type of the token
     */
    String getTokenType();

    /**
     * Usually is the uuid of the user that the token belongs to
     * @return who the token belongs to, <b>MAYBE</b> NULL
     */
    Optional<String> getAudience();
}
