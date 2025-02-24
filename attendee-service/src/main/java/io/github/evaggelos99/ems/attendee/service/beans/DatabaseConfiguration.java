package io.github.evaggelos99.ems.attendee.service.beans;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.github.evaggelos99.ems.common.api.domainobjects.TicketType;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.r2dbc.core.binding.BindMarkersFactory;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;

@Configuration
@PropertySource("classpath:properties/db.properties")
@EnableTransactionManagement
public class DatabaseConfiguration {

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final String database;

    private final String host;

    private final String password;

    private final String port;

    private final String schema;

    private final String username;

    /**
     * C-or
     *
     * @param username the username of the DB
     * @param password the password of the DB
     * @param port     the port of the DB
     * @param database the name of the DB
     * @param host     the hostname of the DB
     * @param schema   the schema of the DB
     */
    public DatabaseConfiguration(@Value("${org.com.ems.db.username}") final String username,
                                 @Value("${org.com.ems.db.password}") final String password,
                                 @Value("${org.com.ems.db.port}") final String port,
                                 @Value("${org.com.ems.db.database}") final String database,
                                 @Value("${org.com.ems.db.host}") final String host,
                                 @Value("${org.com.ems.db.schema") final String schema) {

        this.username = username;
        this.password = password;
        this.port = port;
        this.database = database;
        this.host = host;
        this.schema = schema;
    }

    @Bean
    DatabaseClient databaseClient(final ConnectionFactory postgresqlConnectionFactory) {

        return DatabaseClient.builder()
//                .bindMarkers(BindMarkersFactory)
                .connectionFactory(postgresqlConnectionFactory).build();
    }

    @Bean
    ConnectionFactory postgresqlConnectionFactory() {

        return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(Integer.parseInt(port))
                .database(database)
                .username(username)
                .password(password)
                .connectTimeout(TIMEOUT)
                .codecRegistrar(EnumCodec.builder()
                        .withEnum("event_type_enum", EventType.class)
                        .withEnum("ticket_type_enum", TicketType.class)
                        .build())
                .schema(schema)
                .build());
    }

}
