package io.github.evaggelos99.ems.common.api.db;

import io.github.evaggelos99.ems.common.api.domainobjects.IMapping;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface IMappingRepository<T extends IMapping> {

    Flux<T> saveMapping(UUID entityUuid, UUID[] secondaryUuids);

    Mono<T> saveSingularMapping(UUID entityUuid, UUID secondaryUuid);

    Flux<T> editMapping(UUID entityUuid, UUID[] uuids);

    Mono<Boolean> deleteMapping(UUID entityUuid);

    Mono<Boolean> deleteSingularMapping(UUID entityUuid, UUID secondaryUuid);

}
