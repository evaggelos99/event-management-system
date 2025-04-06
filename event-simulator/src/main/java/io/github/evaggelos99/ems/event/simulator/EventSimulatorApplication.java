package io.github.evaggelos99.ems.event.simulator;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
//@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
//        "io.github.evaggelos99.ems.attendee.api", "io.github.evaggelos99.ems.attendee.service", "io.github.evaggelos99.ems.security.lib"})
@SpringBootApplication
public class EventSimulatorApplication {

    public static void main(final String[] args) {

        new SpringApplicationBuilder(EventSimulatorApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }

}