package io.github.evaggelos99.ems.ticket.service.util;

import org.springframework.boot.r2dbc.ConnectionFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;

import io.github.evaggelos99.r2dbc.h2.H2ConnectionFactoryProvider;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import io.r2dbc.spi.ConnectionFactoryOptions.Builder;

@Configuration
public class TestConfiguration {

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

//	@Bean("ticketRowMapper")
//	TicketRowMapper ticketRowMapper() {
//
//		return new TicketRowMapper() {
//
//			@Override
//			public Ticket apply(final Row row, final RowMetadata u) {
//
//				final SeatingInformation seatingInformation = new SeatingInformation(row.get("seat", String.class),
//						row.get("section", String.class));
//
//				return new Ticket(row.get("id", UUID.class), row.get("created_at", OffsetDateTime.class).toInstant(),
//						row.get("last_updated", OffsetDateTime.class).toInstant(),
//						UUID.fromString(row.get("event_id", String.class)),
//						TicketType.valueOf(row.get("ticket_type", String.class)), row.get("price", Integer.class),
//						row.get("transferable", Boolean.class), seatingInformation);
//			}
//		};
//	}
}
