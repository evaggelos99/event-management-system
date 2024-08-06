package com.github.evaggelos99.ems.common.api.service;

import java.util.Collection;
import java.util.UUID;

import com.github.evaggelos99.ems.common.api.domainobjects.AbstractDomainObject;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 *
 * @author Evangelos Georgiou
 *
 * @param <T> represents the {@link AbstractDomainObject}
 * @param <D> represents the DTO object of the {@link AbstractDomainObject}
 */
public interface IService<T extends AbstractDomainObject, D> extends ILookUpService<T> {

    /**
     * Adds the corresponding {@link D} to the service
     *
     * @param dto
     *
     * @return the {@link T} domain object
     */
    Mono<T> add(D dto);

    /**
     * {@inheritDoc}
     */
    @Override
    Mono<T> get(UUID id);

    /**
     * Deletes the {@link T} object that matches the id
     *
     * @param id UUID of the object that is about to be deleted
     */
    Mono<Boolean> delete(UUID id);

    /**
     * Edits the {@link T} object
     *
     * @param id  the existing {@link T} object's id
     * @param dto the objects payload
     * @return the {@link T} domain object
     */
    Mono<T> edit(UUID id,
		 D dto);

    /**
     * Fetches all the {@link T} objects available
     *
     * @return {@link Collection} of all the objects
     */
    Flux<T> getAll();

    /**
     * Method that tells your if the object with the specified id exists
     *
     * @param id of the object
     * @return {@link Boolean#TRUE} if the object exists otherwise
     *         {@link Boolean#FALSE}
     */
    Mono<Boolean> existsById(UUID id);

}
