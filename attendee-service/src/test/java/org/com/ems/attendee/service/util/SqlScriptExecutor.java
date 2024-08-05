package org.com.ems.attendee.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.r2dbc.connection.init.ScriptUtils;
import org.springframework.stereotype.Component;

import io.r2dbc.spi.ConnectionFactory;
import reactor.core.publisher.Mono;

@Component
public class SqlScriptExecutor {

    @Autowired
    ConnectionFactory connectionFactory;

    /**
     * Sets up the H2 database
     */
    public void setup() {

	this.executeScriptBlocking(new ClassPathResource("schema.sql"));
	this.executeScriptBlocking(new ClassPathResource("data.sql"));
    }

    private void executeScriptBlocking(final Resource sqlScript) {

	Mono.from(this.connectionFactory.create())
		.flatMap(connection -> ScriptUtils.executeSqlScript(connection, sqlScript)).block();
    }
}
