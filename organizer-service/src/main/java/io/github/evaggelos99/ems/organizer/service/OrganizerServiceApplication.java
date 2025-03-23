package io.github.evaggelos99.ems.organizer.service;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
        "io.github.evaggelos99.ems.organizer.api", "io.github.evaggelos99.ems.organizer.service", "io.github.evaggelos99.ems.security.lib"})
public class OrganizerServiceApplication {

    public static void main(final String[] args) {

        new SpringApplicationBuilder(OrganizerServiceApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }

}