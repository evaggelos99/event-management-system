package org.com.ems.db;

import java.time.Duration;

import org.com.ems.api.domainobjects.EventType;
import org.com.ems.api.domainobjects.TicketType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.codec.EnumCodec;

@Configuration
@PropertySource("classpath:properties/db.properties")
@EnableTransactionManagement
public class DatabaseConfiguration {

    private final String username;

    private final String password;

    private final String port;

    private final String database;

    private final String host;

    /**
     * C-or
     *
     * @param driver
     * @param url
     * @param username
     * @param password
     */
    public DatabaseConfiguration(@Value("${org.com.ems.db.username}") final String username,
				 @Value("${org.com.ems.db.password}") final String password,
				 @Value("${org.com.ems.db.port}") final String port,
				 @Value("${org.com.ems.db.database}") final String database,
				 @Value("${org.com.ems.db.host}") final String host) {

	this.username = username;
	this.password = password;
	this.port = port;
	this.database = database;
	this.host = host;

    }

    @Bean
    PostgresqlConnectionFactory postgresqlConnectionFactory() {

	return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder().host(this.host)
		.port(Integer.parseInt(this.port)).database(this.database).username(this.username)
		.password(this.password).connectTimeout(Duration.ofSeconds(5))
		.codecRegistrar(EnumCodec.builder().withEnum("event_type_enum", EventType.class)
			.withEnum("ticket_type_enum", TicketType.class).build())
		.build());

    }

    @Bean
    DatabaseClient databaseClient(final PostgresqlConnectionFactory postgresqlConnectionFactory) {

	return DatabaseClient.builder().connectionFactory(postgresqlConnectionFactory).build();

    }

}
