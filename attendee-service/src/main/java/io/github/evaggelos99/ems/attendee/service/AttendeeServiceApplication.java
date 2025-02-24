package io.github.evaggelos99.ems.attendee.service;

import io.github.evaggelos99.ems.attendee.service.transport.kafka.AttendeeToEventServicePublisher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
        "io.github.evaggelos99.ems.attendee.api", "io.github.evaggelos99.ems.attendee.service", "io.github.evaggelos99.ems.security.lib"})
public class AttendeeServiceApplication {

    public static void main(final String[] args) {

        new SpringApplicationBuilder(AttendeeServiceApplication.class).web(WebApplicationType.REACTIVE).run(args);
    }

}