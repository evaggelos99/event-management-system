package org.com.ems.db;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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
     * @param uuid of the {@link T}
     * @return Optional wrapped {@link T}
     */
    Optional<T> findById(UUID uuid);

    /**
     * Deletes given the UUID
     *
     * @param uuid of the {@link T}
     * @return if the deletion was sucessful
     */
    boolean deleteById(UUID uuid);

    /**
     * If it exists by id
     *
     * @param uuid of the {@link T}
     * @return if the {@link T} exists
     */
    boolean existsById(UUID uuid);

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
