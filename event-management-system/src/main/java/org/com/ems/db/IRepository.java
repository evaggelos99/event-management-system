package org.com.ems.db;

import java.util.UUID;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IRepository<T, D> {

    /**
     * Saves the dto and returns the domain object
     *
     * @param dto of the object
     * @return the object {@link T}
     */
    Mono<T> save(D dto);

    /**
     * Finds by id
     *
     * @param uuid of the {@link T}
     * @return Optional wrapped {@link T}
     */
    Mono<T> findById(UUID uuid);

    /**
     * Deletes given the UUID
     *
     * @param uuid of the {@link T}
     * @return if the deletion was sucessful
     */
    Mono<Boolean> deleteById(UUID uuid);

    /**
     * If it exists by id
     *
     * @param uuid of the {@link T}
     * @return if the {@link T} exists
     */
    Mono<Boolean> existsById(UUID uuid);

    /**
     * Returns all of the {@link T} in the Repository
     *
     * @return
     */
    Flux<T> findAll();

    /**
     * Edits the dto and returns the domain object
     *
     * @param dto of the object
     * @return the object {@link T}
     */
    Mono<T> edit(D dto);

}
