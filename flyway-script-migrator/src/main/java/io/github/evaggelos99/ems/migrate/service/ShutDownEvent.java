package io.github.evaggelos99.ems.migrate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

public class ShutDownEvent implements ApplicationListener<ApplicationReadyEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ShutDownEvent.class);

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        LOGGER.info("Flyway migration took: {}", event.getTimeTaken());
        LOGGER.info("Now shutting Migrator down.");
        event.getApplicationContext().close();
    }
}
