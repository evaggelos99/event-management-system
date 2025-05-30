package io.github.evaggelos99.ems.attendee.service.repository;

import io.github.evaggelos99.ems.attendee.api.AttendeeTicketMapping;
import io.github.evaggelos99.ems.attendee.api.repo.TicketMappingRowMapper;
import io.github.evaggelos99.ems.common.api.db.IMappingRepository;
import io.github.evaggelos99.ems.common.api.db.MappingQueriesOperations;
import io.r2dbc.spi.Connection;
import io.r2dbc.spi.Statement;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class TicketMappingRepository implements IMappingRepository<AttendeeTicketMapping> {

    private final DatabaseClient databaseClient;
    private final Map<MappingQueriesOperations, String> mappingQueriesProperties;
    private final TicketMappingRowMapper ticketMappingRowMapper;

    public TicketMappingRepository(final DatabaseClient databaseClient,
                                   @Qualifier("mappingQueriesProperties") Map<MappingQueriesOperations, String> mappingQueriesProperties,
                                   final TicketMappingRowMapper ticketMappingRowMapper) {

        this.databaseClient = databaseClient;
        this.mappingQueriesProperties = mappingQueriesProperties;
        this.ticketMappingRowMapper = ticketMappingRowMapper;
    }

    @Override
    public Flux<AttendeeTicketMapping> saveMapping(UUID entityUuid, UUID[] secondaryUuids) {

        return databaseClient.inConnectionMany(conn -> executeBatchAttendeeTicketsMapping(conn, entityUuid, secondaryUuids));
    }

    @Override
    public Mono<AttendeeTicketMapping> saveSingularMapping(final UUID entityUuid, final UUID secondaryUuid) {

        return databaseClient.sql(mappingQueriesProperties.get(MappingQueriesOperations.SAVE_MAPPING))
                .bind(0,entityUuid)
                .bind(1, secondaryUuid)
                .fetch().rowsUpdated().map(x-> x==1)
                .filter(Boolean::booleanValue)
                .map(x-> new AttendeeTicketMapping(entityUuid, secondaryUuid));
    }

    @Override
    public Flux<AttendeeTicketMapping> editMapping(final UUID entityUuid, final UUID[] uuids) {

        return deleteMapping(entityUuid).filter(Boolean::booleanValue)
                .flatMapMany(x -> saveMapping(entityUuid, uuids));
    }

    @Override
    public Mono<Boolean> deleteMapping(final UUID uuid) {

        return databaseClient.sql(mappingQueriesProperties.get(MappingQueriesOperations.DELETE_MAPPINGS))
                .bind(0, uuid)
                .fetch()
                .rowsUpdated()
                .map(Objects::nonNull);
    }

    @Override
    public Mono<Boolean> deleteSingularMapping(final UUID entityUuid, final UUID secondaryUuid) {

        return databaseClient.sql(mappingQueriesProperties.get(MappingQueriesOperations.DELETE_SINGULAR_MAPPING))
                .bind(0, entityUuid)
                .bind(1, secondaryUuid)
                .fetch()
                .rowsUpdated()
                .map(Objects::nonNull);
    }

    private Flux<AttendeeTicketMapping> executeBatchAttendeeTicketsMapping(final Connection conn, final UUID uuid, final UUID[] uuids) {

        if (uuids.length == 0) {
            return Flux.empty();
        }

        final Statement st = conn.createStatement(mappingQueriesProperties.get(MappingQueriesOperations.SAVE_MAPPING));

        for (int i = 0; i < uuids.length - 1; i++) {
            st.bind(0, uuid).bind(1, uuids[i]).add();
        }

        st.bind(0,uuid).bind(1, uuids[uuids.length-1]);

        return Flux.from(st.execute())
                .flatMap(result -> result.map(ticketMappingRowMapper));
    }


}
