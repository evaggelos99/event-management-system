package io.github.evaggelos99.ems.attendee.service.transport.kafka;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * Interceptor that propagates the existing Authorization header into the new Client
 */
public class CustomTokenBearerInterceptor implements ExchangeFilterFunction {

    /**
     * Gets the current JWT in the HTTP headers and adds it in the WebClient requests
     *
     * @param request the current request
     * @param next the next exchange function in the chain
     * @return the ClientResponse with the Authorization header popualated
     */
    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {

        return Mono.deferContextual(Mono::just)
                .doOnNext(context -> System.out.println(ToStringBuilder.reflectionToString(context)))
                .flatMap(context -> context.get((Object) SecurityContext.class))
                .cast(SecurityContext.class)
                .map(SecurityContext::getAuthentication)
                .cast(JwtAuthenticationToken.class)
                .map(JwtAuthenticationToken::getToken)
                .map(AbstractOAuth2Token::getTokenValue)
                .map(token -> mapRequestToToken(request, token))
                .flatMap(next::exchange);
    }

    private ClientRequest mapRequestToToken(ClientRequest request, String token) {
        return ClientRequest.from(request)
                .headers((headers) -> headers.setBearerAuth(token))
                .build();
    }
}
