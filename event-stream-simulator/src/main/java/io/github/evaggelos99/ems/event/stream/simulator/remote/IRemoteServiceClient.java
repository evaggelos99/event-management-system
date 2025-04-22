package io.github.evaggelos99.ems.event.stream.simulator.remote;

import reactor.core.publisher.Mono;

public interface IRemoteServiceClient {

    /**
     * Hits ping endpoint
     *
     * @return {@link Boolean#TRUE} if the service is reachable other wise
     * {@link Boolean#FALSE} if the service is not reachable
     */
    Mono<Boolean> ping();

}
