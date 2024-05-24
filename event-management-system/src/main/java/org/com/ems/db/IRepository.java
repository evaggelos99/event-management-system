package org.com.ems.db;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IRepository<T, DTO> {

    /**
     *
     * @param dto
     * @return
     */
    T save(DTO dto);

    Optional<T> findById(UUID uuid);

    boolean deleteById(UUID uuid);

    boolean existsById(UUID uuid);

    Collection<T> findAll();

    T edit(DTO dto);

}
