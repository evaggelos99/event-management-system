package io.github.evaggelos99.ems.migrate.service;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatabaseMigratorConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseMigratorConfiguration.class);

    private static final String DB_PREFIX = "jdbc:postgresql://";
//    private static final String DB_PREFIX = "r2dbc:postgresql://";

    private final String username;
    private final String password;
    private final String port;
    private final String database;
    private final String host;

    public DatabaseMigratorConfiguration(@Value("${org.com.ems.db.username}") final String username,
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
    @ConditionalOnProperty(name = "org.com.ems.db.flyway.enabled")
    Flyway flyway() {

        final Flyway flyway = Flyway.configure()
                .baselineOnMigrate(true)
                .dataSource(getFullUrl(), username, password)
                .locations("db/migration")
                .createSchemas(true)
                .schemas("public", "ems_sponsor", "ems_attendee", "ems_organizer", "ems_event", "ems_ticket")
                .load();

        flyway.migrate().getSuccessfulMigrations().forEach(this::logMigration);

        return flyway;
    }

    @Bean
    ShutDownEvent shutDownEvent() {
        return new ShutDownEvent();
    }

    private String getFullUrl() {
        return String.format("%s%s:%s/%s", DB_PREFIX, host, port, database);
    }

    private void logMigration(MigrateOutput migrateOutput) {
        LOGGER.info("Migration {} took {} seconds, with version {}.", migrateOutput.description, migrateOutput.executionTime, migrateOutput.version);
    }

}
