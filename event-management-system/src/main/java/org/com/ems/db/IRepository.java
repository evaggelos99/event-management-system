package org.com.ems.db;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface IRepository<T, T_DTO> {

    T save(T_DTO t);

    Optional<T> findById(UUID uuid);

    boolean deleteById(UUID uuid);

    boolean existsById(UUID uuid);

    Collection<T> findAll();

    T edit(T_DTO attendeeDto);

}
