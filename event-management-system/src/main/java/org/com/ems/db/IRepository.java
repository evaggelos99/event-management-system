package org.com.ems.db;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.com.ems.api.domainobjects.AbstractDomainObject;

/**
 *
 * @author Evangelos Georgiou
 *
 * @param <T> represents the {@link AbstractDomainObject}
 * @param <D> represents the DTO object of the {@link AbstractDomainObject}
 */
public interface IRepository<T, D> {

    /**
     * Saves the dto and returns the domain object
     *
     * @param dto of the object
     * @return the object {@link T}
     */
    T save(D dto);

    /**
     * Finds by id
     *
     * @param id of the {@link T}
     * @return Optional wrapped {@link T}
     */
    Optional<T> findById(UUID id);

    /**
     * Deletes given the UUID
     *
     * @param id of the {@link T}
     * @return if the deletion was sucessful
     */
    boolean deleteById(UUID id);

    /**
     * If it exists by id
     *
     * @param id of the {@link T}
     * @return if the {@link T} exists
     */
    boolean existsById(UUID id);

    /**
     * Returns all of the {@link T} in the Repository
     *
     * @return
     */
    Collection<T> findAll();

    /**
     * Edits the dto and returns the domain object
     *
     * @param dto of the object
     * @return the object {@link T}
     */
    T edit(D dto);

}
