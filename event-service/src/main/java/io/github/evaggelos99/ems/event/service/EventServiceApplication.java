package io.github.evaggelos99.ems.event.service;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

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