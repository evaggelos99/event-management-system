package io.github.evaggelos99.ems.ticket.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
        "io.github.evaggelos99.ems.ticket.api", "io.github.evaggelos99.ems.ticket.service"})
public class TicketServiceApplication {

    public static void main(final String[] args) {

        SpringApplication.run(TicketServiceApplication.class, args);

    }

}