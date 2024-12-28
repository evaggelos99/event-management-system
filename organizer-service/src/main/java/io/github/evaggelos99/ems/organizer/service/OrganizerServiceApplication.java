package io.github.evaggelos99.ems.organizer.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
        "io.github.evaggelos99.ems.organizer.api", "io.github.evaggelos99.ems.organizer.service"})
public class OrganizerServiceApplication {

    public static void main(final String[] args) {

        SpringApplication.run(OrganizerServiceApplication.class, args);

    }

}