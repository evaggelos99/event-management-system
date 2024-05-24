package org.com.ems.services.api;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IService<T, DTO> extends ILookUpService<T> {

    T add(DTO object);

    @Override
    Optional<T> get(UUID uuid);

    void delete(UUID uuid);

    T edit(UUID uuid,
	   DTO object);

    Collection<T> getAll();

    boolean existsById(UUID objectId);

}
