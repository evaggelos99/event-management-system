package org.com.ems.services;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IService<T> {

    // TODO throw exception when not available
    T add(T object);

    Optional<T> get(UUID uuid);

    void delete(UUID uuid);

    T edit(UUID uuid,
	   T object);

    Collection<T> getAll();

    boolean existsById(UUID objectId);

}
