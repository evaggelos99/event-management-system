package io.github.evaggelos99.ems.common.api.service.remote;

import reactor.core.publisher.Mono;

public interface ITokenRetriever {

    Mono<String> getToken();

}
