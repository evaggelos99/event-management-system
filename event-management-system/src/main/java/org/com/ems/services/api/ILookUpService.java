package org.com.ems.services.api;

import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.AbstractDomainObject;

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
