package io.github.evaggelos99.ems.event.stream.simulator;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication
@EnableScheduling
public class EventSimulatorApplication {

    public static void main(final String[] args) {

        new SpringApplicationBuilder(EventSimulatorApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }

}