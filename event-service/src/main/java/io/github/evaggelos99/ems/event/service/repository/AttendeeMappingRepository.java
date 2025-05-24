package io.github.evaggelos99.ems.event.service.repository;

import io.github.evaggelos99.ems.common.api.db.IMappingRepository;
import io.github.evaggelos99.ems.common.api.db.MappingQueriesOperations;
import io.github.evaggelos99.ems.event.api.EventAttendeeMapping;
import io.github.evaggelos99.ems.event.api.repo.AttendeeMappingRowMapper;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Component
public class AttendeeMappingRepository implements IMappingRepository<EventAttendeeMapping> {

    private final DatabaseClient databaseClient;
    private final Map<MappingQueriesOperations, String> mappingQueriesProperties;
    private final AttendeeMappingRowMapper attendeeMappingRowMapper;

    public AttendeeMappingRepository(final DatabaseClient databaseClient,
                                     @Qualifier("attendeeMappingQueriesProperties") Map<MappingQueriesOperations, String> mappingQueriesProperties,
                                     final AttendeeMappingRowMapper attendeeMappingRowMapper) {

        this.databaseClient = databaseClient;
        this.mappingQueriesProperties = mappingQueriesProperties;
        this.attendeeMappingRowMapper = attendeeMappingRowMapper;
    }

    @Override
    public Flux<EventAttendeeMapping> saveMapping(UUID entityUuid, UUID[] secondaryUuids) {

        return databaseClient.inConnectionMany(conn -> executeBatchAttendeeTicketsMapping(conn, entityUuid, secondaryUuids));
    }

    @Override
    public Mono<EventAttendeeMapping> saveSingularMapping(UUID entityUuid, UUID secondaryUuid) {

        return databaseClient.sql(mappingQueriesProperties.get(MappingQueriesOperations.SAVE_MAPPING))
                .bind(0,entityUuid)
                .bind(1, secondaryUuid)
                .fetch().rowsUpdated().map(x-> x==1)
                .filter(Boolean::booleanValue)
                .map(x-> new EventAttendeeMapping(entityUuid, secondaryUuid));
    }

    @Override
    public Flux<EventAttendeeMapping> editMapping(final UUID entityUuid, final UUID[] uuids) {

        return deleteMapping(entityUuid).filter(Boolean::booleanValue)
                .flatMapMany(x -> saveMapping(entityUuid, uuids));
    }

    @Override
    public Mono<Boolean> deleteMapping(final UUID uuid) {

        return databaseClient.sql(mappingQueriesProperties.get(MappingQueriesOperations.DELETE_MAPPINGS))
                .bind(0, uuid)
                .fetch()
                .rowsUpdated()
                .map(x-> x>=1);
    }

    @Override
    public Mono<Boolean> deleteSingularMapping(final UUID entityUuid, final UUID secondaryUuid) {

        return databaseClient.sql(mappingQueriesProperties.get(MappingQueriesOperations.DELETE_SINGULAR_MAPPING))
                .bind(0, entityUuid)
                .bind(1, secondaryUuid)
                .fetch()
                .rowsUpdated()
                .map(x-> x==1);
    }

    private Flux<EventAttendeeMapping> executeBatchAttendeeTicketsMapping(final Connection conn, final UUID uuid, final UUID[] uuids) {

        if (uuids.length == 0) {
            return Flux.empty();
        }

        final Statement statement = conn.createStatement(mappingQueriesProperties.get(MappingQueriesOperations.SAVE_MAPPING));

        for (int i = 0; i < uuids.length - 1; i++) {

            statement.bind(0, uuid)
                    .bind(1, uuids[i]).add();
        }

        statement.bind(0, uuid).bind(1, uuids[uuids.length - 1]);

        return Flux.from(statement.execute())
                .flatMap(result -> result.map(attendeeMappingRowMapper));
    }


}
