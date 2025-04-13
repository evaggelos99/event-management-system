package io.github.evaggelos99.ems.common.api.service.remote;

import io.github.evaggelos99.ems.common.api.service.remote.pojos.ITokenPayload;
import reactor.core.publisher.Mono;

public interface ITokenRetriever {

    Mono<ITokenPayload> getToken();

}
