package io.github.evaggelos99.ems.organizer.service.util;

import io.github.evaggelos99.ems.common.api.domainobjects.ContactInformation;
import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.repo.OrganizerRowMapper;
import io.github.evaggelos99.r2dbc.h2.H2ConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ConnectionFactoryOptions.Builder;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Configuration
public class TestConfiguration {

    @Bean("organizerRowMapper")
    OrganizerRowMapper organizerRowMapper() {

        return new OrganizerRowMapper(null) {

            @Override
            public Organizer apply(final Row row, final RowMetadata u) {

                final Object[] ss = ((Object[]) row.get("event_types"));
                final List<EventType> eventsTypes = new LinkedList<>();

                for (final var o : ss) {

                    eventsTypes.add(EventType.valueOf(o.toString())); // h2 encodes into Strings
                }

                final ContactInformation contactInformation = new ContactInformation(row.get("email", String.class),
                        row.get("phone_number", String.class), row.get("physical_address", String.class));

                return new Organizer(row.get("id", UUID.class), row.get("created_at", OffsetDateTime.class).toInstant(),
                        row.get("last_updated", OffsetDateTime.class).toInstant(), row.get("name", String.class),
                        row.get("website", String.class), row.get("information", String.class), eventsTypes,
                        contactInformation);
            }
        };
    }

    @Bean
    ConnectionFactory postgresqlConnectionFactory() {

        final Builder connectionFactoryOptionsBuilder = ConnectionFactoryOptions.builder()
                .option(ConnectionFactoryOptions.DRIVER, H2ConnectionFactoryProvider.H2_DRIVER)
                .option(ConnectionFactoryOptions.HOST, "localhost")
                .option(ConnectionFactoryOptions.DATABASE, "event-management-system")
                .option(H2ConnectionFactoryProvider.URL, "mem:test")
                .option(ConnectionFactoryOptions.PROTOCOL, H2ConnectionFactoryProvider.PROTOCOL_MEM);
        return ConnectionFactoryBuilder.withOptions(connectionFactoryOptionsBuilder).build();
    }

    @Bean
    DatabaseClient databaseClient(final ConnectionFactory postgresqlConnectionFactory) {

        return DatabaseClient.builder().connectionFactory(postgresqlConnectionFactory).build();
    }
}
