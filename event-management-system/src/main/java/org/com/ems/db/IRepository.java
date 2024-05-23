package org.com.ems.db;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IRepository<T, T_DTO> {

    /**
     *
     * @param dto
     * @return
     */
    T save(T_DTO dto);

    Optional<T> findById(UUID uuid);

    boolean deleteById(UUID uuid);

    boolean existsById(UUID uuid);

    Collection<T> findAll();

    T edit(T_DTO dto);

}
