package io.github.evaggelos99.ems.event.service.util;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
public class SqlScriptExecutor {

    @Autowired
    ConnectionFactory connectionFactory;

    /**
     * Sets up the database
     */
    public void setup(String path) {

        executeScriptBlocking(new ClassPathResource(path));
    }

    private void executeScriptBlocking(final Resource sqlScript) {

        Mono.from(connectionFactory.create()).flatMap(connection -> ScriptUtils.executeSqlScript(connection, sqlScript))
                .block(Duration.ofSeconds(10));
    }
}
