package io.github.evaggelos99.ems.event.service.util;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.h2.api.Interval;
import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.event.api.Event;
import io.github.evaggelos99.ems.event.api.repo.EventRowMapper;
import io.github.evaggelos99.r2dbc.h2.H2ConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ConnectionFactoryOptions.Builder;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Configuration
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

				final List<UUID> sponsors = foo((Object[]) rs.get("sponsors_ids"));

				final var dur = rs.get("duration", Interval.class);
				final Duration duration = Duration.of(dur.getNanosOfSecond(), ChronoUnit.NANOS)
						.plusSeconds(dur.getSeconds()).plusHours(dur.getHours());

				return new Event(rs.get("id", UUID.class), rs.get("created_at", OffsetDateTime.class).toInstant(),
						rs.get("last_updated", OffsetDateTime.class).toInstant(), rs.get("name", String.class),
						rs.get("place", String.class), EventType.valueOf(rs.get("event_type", String.class)), attendees,
						rs.get("organizer_id", UUID.class), rs.get("limit_of_people", Integer.class), sponsors,
						rs.get("start_time", OffsetDateTime.class).toLocalDateTime(), duration);
			}

			private List<UUID> foo(final Object[] objects) {

				final List<UUID> list = new LinkedList<>();

				for (final var obj : objects) {

					list.add((UUID) obj);
				}

				return list;
			}

		};
	}

//	@Bean("eventRowMapper")
//	EventRowMapper eventRowMapper() {
//
//		return new EventRowMapper(null) {
//
//			@Override
//			public Event apply(final Row rs, final RowMetadata u) {
//
//				final Object[] attendeesIds = (Object[]) rs.get("attendee_ids");
//				final LinkedList<UUID> attendees = new LinkedList<>();
//
//				for (final Object uuid : attendeesIds) {
//					attendees.add((UUID) uuid);
//				}
//
//				final Object[] sponsorIds = (Object[]) rs.get("sponsors_ids");
//				final LinkedList<UUID> sponsors = new LinkedList<>();
//
//				for (final Object uuid : sponsorIds) {
//					sponsors.add((UUID) uuid);
//				}
//
//				final Duration duration = rs.get("duration", io.r2dbc.postgresql.codec.Interval.class).getDuration();
//
//				return new Event(UUID.fromString(rs.get("id", String.class)),
//						rs.get("created_at", OffsetDateTime.class).toInstant(),
//						rs.get("last_updated", OffsetDateTime.class).toInstant(), rs.get("name", String.class),
//						rs.get("place", String.class), EventType.valueOf(rs.get("event_type", String.class)), attendees,
//						UUID.fromString(rs.get("organizer_id", String.class)), rs.get("limit_of_people", Integer.class),
//						sponsors, rs.get("start_time", OffsetDateTime.class).toLocalDateTime(), duration);
//
//			}
//		};
//	}

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
