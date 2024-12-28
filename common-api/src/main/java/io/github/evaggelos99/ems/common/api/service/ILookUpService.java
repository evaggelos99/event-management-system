package io.github.evaggelos99.ems.common.api.service;

import io.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;

public interface ILookUpService<T extends AbstractDomainObject> {

    /**
     * Fetches the {@link T} Domain Object
     *
     * @param id of the object
     * @return {@link Optional} of the {@link T} object
     */
    Mono<T> get(UUID id);

    /**
     * Ping endpoint for the Service
     *
     * @return {@link Boolean#TRUE} if the service is reachable other wise
     * {@link Boolean#FALSE} if the service is not reachable
     */
    Mono<Boolean> ping();

}
