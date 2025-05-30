package io.github.evaggelos99.ems.event.service.util;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.repo.EventRowMapper;
import io.github.evaggelos99.r2dbc.h2.H2ConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ConnectionFactoryOptions.Builder;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.h2.api.Interval;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Configuration
@Profile("test")
public class TestConfiguration {

    @Bean("durationToIntervalConverter")
    Function<Duration, Object> durationToIntervalConverter() {

        return x -> Interval.ofSeconds(x.getSeconds());
    }

    @Bean("eventRowMapper")
    EventRowMapper eventRowMapper() {

        return new EventRowMapper(null) {

            @Override
            public Event apply(final Row rs, final RowMetadata rmd) {

                final List<UUID> attendees = foo((Object[]) rs.get("attendee_ids"));

                final List<UUID> sponsors = foo((Object[]) rs.get("sponsor_ids"));

                final var dur = rs.get("duration", Interval.class);
                final Duration duration = Duration.of(dur.getNanosOfSecond(), ChronoUnit.NANOS)
                        .plusSeconds(dur.getSeconds()).plusHours(dur.getHours());

                return new Event(rs.get("id", UUID.class), rs.get("created_at", OffsetDateTime.class),
                        rs.get("last_updated", OffsetDateTime.class), rs.get("name", String.class),
                        rs.get("place", String.class), EventType.valueOf(rs.get("event_type", String.class)), attendees,
                        rs.get("organizer_id", UUID.class), rs.get("limit_of_people", Integer.class), sponsors,
                        Boolean.TRUE.equals(rs.get("streamable", Boolean.class)),
                        rs.get("start_time", OffsetDateTime.class).toLocalDateTime(), duration);
            }

            private List<UUID> foo(final Object[] objects) {

                final List<UUID> list = new LinkedList<>();

                if (objects == null) {

                    return list;
                }

                for (final var obj : objects) {

                    list.add((UUID) obj);
                }

                return list;
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
