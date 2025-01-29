package io.github.evaggelos99.ems.event.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
        "io.github.evaggelos99.ems.event.api", "io.github.evaggelos99.ems.event.service"})
public class EventServiceApplication {

    public static void main(final String[] args) {

        SpringApplication.run(EventServiceApplication.class, args);
    }

}