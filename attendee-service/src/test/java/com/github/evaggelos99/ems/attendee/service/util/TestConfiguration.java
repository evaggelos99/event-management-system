package com.github.evaggelos99.ems.attendee.service.util;

import java.time.OffsetDateTime;
import java.util.LinkedList;
import java.util.UUID;

import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import com.github.evaggelos99.ems.attendee.api.Attendee;
import com.github.evaggelos99.ems.attendee.api.repo.AttendeeRowMapper;

import io.r2dbc.h2.H2ConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ConnectionFactoryOptions.Builder;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

@Configuration
public class TestConfiguration {

    @Bean("attendeeRowMapper")
    AttendeeRowMapper attendeeRowMapper() {

	return new AttendeeRowMapper(null) {

	    @Override
	    public Attendee apply(final Row row,
				  final RowMetadata u) {

		final Object[] ticketIds = (Object[]) row.get("ticket_ids");
		final LinkedList<UUID> ll = new LinkedList<>();

		for (final Object uuid : ticketIds) {
		    ll.add((UUID) uuid);
		}
		return new Attendee(row.get("id", UUID.class), row.get("created_at", OffsetDateTime.class).toInstant(),
			row.get("last_updated", OffsetDateTime.class).toInstant(), row.get("first_name", String.class),
			row.get("last_name", String.class), ll);
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
