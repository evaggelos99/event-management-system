package io.github.evaggelos99.ems.event.stream.simulator.remote;

import io.github.evaggelos99.ems.common.api.service.remote.ITokenRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.web.reactive.function.OAuth2BodyExtractors;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
public class FusionAuthTokenRetriever implements ITokenRetriever {

    private final String grantType;
    private final String username;
    private final String password;
    private final String clientId;
    private final String clientSecret;
    private final WebClient fusionAuthClient;

    /**
     * C-or
     *
     * @param grantType        What grant_type we are requesting the token with
     * @param username         The username of the User
     * @param password         The password of the User
     * @param clientId         The client_id we are requesting to authenticate for
     * @param clientSecret     The client_secret of the client
     * @param fusionAuthUrl    The url of the authentication server
     * @param webClientBuilder The {@link WebClient} used to retrieve the token
     */
    public FusionAuthTokenRetriever(@Value("${io.github.evaggelos99.ems.event.simulator.grantType}") final String grantType,
                                    @Value("${io.github.evaggelos99.ems.event.simulator.username}") final String username,
                                    @Value("${io.github.evaggelos99.ems.event.simulator.password}") final String password,
                                    @Value("${io.github.evaggelos99.ems.event.simulator.clientId}") final String clientId,
                                    @Value("${io.github.evaggelos99.ems.event.simulator.clientSecret}") final String clientSecret,
                                    @Value("${io.github.evaggelos99.ems.event.simulator.fusionAuthClient}") final String fusionAuthUrl,
                                    final WebClient.Builder webClientBuilder) {

        this.grantType = grantType;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.fusionAuthClient = webClientBuilder.baseUrl(fusionAuthUrl).build();
    }

    @Override
    public Mono<String> getToken() {

        MultiValueMap<String, String> properties = MultiValueMap.fromSingleValue(Map.of("grant_type", grantType,
                "username", username,
                "password", password,
                "client_id", clientId,
                "client_secret", clientSecret));

        return fusionAuthClient.post().uri("/oauth2/token").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(properties)
                .exchangeToMono(response -> response.body(OAuth2BodyExtractors.oauth2AccessTokenResponse()))
                .map(OAuth2AccessTokenResponse::getAccessToken)
                .map(AbstractOAuth2Token::getTokenValue);
    }

}
