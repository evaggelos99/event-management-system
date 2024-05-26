package org.com.ems.services.api;

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
public interface IService<T extends AbstractDomainObject, D> extends ILookUpService<T> {

    /**
     * Adds the corresponding {@link D} to the service
     *
     * @param dto
     *
     * @return the {@link T} domain object
     */
    T add(D dto);

    /**
     * {@inheritDoc}
     */
    @Override
    Optional<T> get(UUID id);

    /**
     * Deletes the {@link T} object that matches the id
     *
     * @param id UUID of the object that is about to be deleted
     */
    void delete(UUID id);

    /**
     * Edits the {@link T} object
     *
     * @param id  the existing {@link T} object's id
     * @param dto the objects payload
     * @return the {@link T} domain object
     */
    T edit(UUID id,
	   D dto);

    /**
     * Fetches all the {@link T} objects available
     *
     * @return {@link Collection} of all the objects
     */
    Collection<T> getAll();

    /**
     * Method that tells your if the object with the specified id exists
     *
     * @param id of the object
     * @return {@link Boolean#TRUE} if the object exists otherwise
     *         {@link Boolean#FALSE}
     */
    boolean existsById(UUID id);

}
