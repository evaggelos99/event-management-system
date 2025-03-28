package io.github.evaggelos99.ems.event.service;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
        "io.github.evaggelos99.ems.event.api", "io.github.evaggelos99.ems.event.service", "io.github.evaggelos99.ems.security.lib"})
public class EventServiceApplication {

    public static void main(final String[] args) {

        new SpringApplicationBuilder(EventServiceApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }

}