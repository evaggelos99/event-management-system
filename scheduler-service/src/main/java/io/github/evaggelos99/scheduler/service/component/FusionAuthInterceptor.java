package io.github.evaggelos99.scheduler.service.component;

import io.github.evaggelos99.scheduler.service.model.FusionauthTokenDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.ClientRequestContext;
import jakarta.ws.rs.client.ClientRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MultivaluedMap;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.ext.ClientHeadersFactory;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.reactive.common.util.QuarkusMultivaluedHashMap;

import java.io.IOException;

@ApplicationScoped
public class FusionAuthInterceptor implements ClientHeadersFactory {

    private final String grantType;
    private final String username;
    private final String password;
    private final String clientId;
    private final String clientSecret;
    private final IFusionAuthClient client;

    @Inject
    public FusionAuthInterceptor(
            @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.idp.fusionauth.grant-type") final String grantType,
            @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.idp.fusionauth.username") final String username,
            @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.idp.fusionauth.password") final String password,
            @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.idp.fusionauth.client-id") final String clientId,
            @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.idp.fusionauth.client-secret")final String clientSecret,
            @RestClient final IFusionAuthClient client) {

        this.grantType = grantType;
        this.username = username;
        this.password = password;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.client = client;
    }

    @Override
    public MultivaluedMap<String, String> update(final MultivaluedMap<String, String> incomingHeaders, final MultivaluedMap<String, String> clientOutgoingHeaders) {

        final String token = getTokenFromIdp();

        MultivaluedMap<String, String> map = new QuarkusMultivaluedHashMap<>();

        map.putSingle(HttpHeaders.AUTHORIZATION, "Bearer "+ token);

        return map;
    }

    private String getTokenFromIdp() {

        final FusionauthTokenDto res = client.getToken(grantType, username, password, clientId, clientSecret);

        System.out.println("asdjkldasjkldakslj");
        System.out.println(res);
        return res.accessToken();
    }


}
