package com.github.evaggelos99.ems.common.api.service;

import java.util.Optional;
import java.util.UUID;

import com.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;

import reactor.core.publisher.Mono;

public interface ILookUpService<T extends AbstractDomainObject> {

    /**
     * Fetches the {@link T} Domain Object
     *
     * @param id of the object
     *
     * @return {@link Optional} of the {@link T} object
     */
    Mono<T> get(UUID id);

}
