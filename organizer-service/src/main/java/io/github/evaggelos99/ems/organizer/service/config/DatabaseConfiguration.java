package io.github.evaggelos99.ems.organizer.service.config;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.time.Duration;

@Configuration
@PropertySource("classpath:properties/db.properties")
@EnableTransactionManagement
public class DatabaseConfiguration {

    private final String username;
    private final String password;
    private final String port;
    private final String database;
    private final String host;
    private final String schema;

    public DatabaseConfiguration(@Value("${io.github.evaggelos99.ems.organizer-service.db.username}") final String username,
                                 @Value("${io.github.evaggelos99.ems.organizer-service.db.password}") final String password,
                                 @Value("${io.github.evaggelos99.ems.organizer-service.db.port}") final String port,
                                 @Value("${io.github.evaggelos99.ems.organizer-service.db.database}") final String database,
                                 @Value("${io.github.evaggelos99.ems.organizer-service.db.host}") final String host,
                                 @Value("${io.github.evaggelos99.ems.organizer-service.db.schema}") final String schema) {

        this.username = username;
        this.password = password;
        this.port = port;
        this.database = database;
        this.host = host;
        this.schema = schema;
    }

    @Bean
    ConnectionFactory postgresqlConnectionFactory() {

        return new PostgresqlConnectionFactory(PostgresqlConnectionConfiguration.builder()
                .host(host)
                .port(Integer.parseInt(port))
                .database(database)
                .username(username)
                .password(password)
                .connectTimeout(Duration.ofSeconds(5))
                .codecRegistrar(EnumCodec.builder()
                        .withEnum("event_type_enum", EventType.class)
                        .build())
                .schema(schema)
                .tcpKeepAlive(true)
                .build());

//        return ConnectionFactoryBuilder.withUrl(String.format("r2dbc:postgresql://%s:%s/%s?schema=%s", host, port, database, schema))
//                .username(username)
//                .password(password)
//                .build();
    }

    @Bean
    DatabaseClient databaseClient(final ConnectionFactory postgresqlConnectionFactory) {

        return DatabaseClient.builder().connectionFactory(postgresqlConnectionFactory).build();
    }

}
