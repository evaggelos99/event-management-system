package org.com.ems.services.api;

import java.util.Optional;
import java.util.UUID;

public interface ILookUpService<T> {

    Optional<T> get(UUID uuid);
}
