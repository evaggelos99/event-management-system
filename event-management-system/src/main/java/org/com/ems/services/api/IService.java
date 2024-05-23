package org.com.ems.services.api;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IService<T, T_DTO> {

    // TODO throw exception when not available
    T add(T_DTO object);

    Optional<T> get(UUID uuid);

    void delete(UUID uuid);

    T edit(UUID uuid,
	   T_DTO object);

    Collection<T> getAll();

    boolean existsById(UUID objectId);

}
