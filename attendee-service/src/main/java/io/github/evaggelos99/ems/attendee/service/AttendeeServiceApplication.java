package io.github.evaggelos99.ems.attendee.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
    "io.github.evaggelos99.ems.attendee.api", "io.github.evaggelos99.ems.attendee.service"}, exclude = {
    org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class}) // work around to disable
// auto configuration for
// Gson used by eureka)
public class AttendeeServiceApplication {

    public static void main(final String[] args) {

        SpringApplication.run(AttendeeServiceApplication.class, args);
    }

}