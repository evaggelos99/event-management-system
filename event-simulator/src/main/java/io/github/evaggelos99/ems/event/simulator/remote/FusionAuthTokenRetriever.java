package io.github.evaggelos99.ems.event.simulator.remote;

import io.github.evaggelos99.ems.common.api.service.remote.ITokenRetriever;
import io.github.evaggelos99.ems.common.api.service.remote.pojos.FusionAuthTokenPayload;
import io.github.evaggelos99.ems.common.api.service.remote.pojos.ITokenPayload;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

public class FusionAuthTokenRetriever implements ITokenRetriever {

    private final String grantType;
    private final String username;
    private final String password;
    private final String clientId;
    private final String clientSecret;
    private final WebClient fusionAuthClient;

    /**
     * C-or
     * @param grantType What grant_type we are requesting the token with
     * @param username The username of the User
     * @param password The password of the User
     * @param clientId The client_id we are requesting to authenticate for
     * @param clientSecret The client_secret of the client
     * @param fusionAuthUrl The url of the authentication server
     * @param webClientBuilder The {@link WebClient} used to retrieve the token
     */
    public FusionAuthTokenRetriever(@Value("${}") final String grantType,
                                    @Value("${}") final String username,
                                    @Value("${}") final String password,
                                    @Value("${}") final String clientId,
                                    @Value("${}") final String clientSecret,
                                    @Value("${}") final String fusionAuthUrl,
                                    final WebClient.Builder webClientBuilder) {

        this.grantType = grantType;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.fusionAuthClient = webClientBuilder.baseUrl(fusionAuthUrl).build();
    }

    @Override
    public Mono<ITokenPayload> getToken() {

        return fusionAuthClient.post().uri("/oauth2/token").contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(new XForm(grantType, username, password, clientId, clientSecret))
                .retrieve().toEntity(FusionAuthTokenPayload.class).map(HttpEntity::getBody);
    }

    private record XForm(String grantType,
                   String username,
                   String password,
                   String clientId,
                   String clientSecret) {

    }


}
