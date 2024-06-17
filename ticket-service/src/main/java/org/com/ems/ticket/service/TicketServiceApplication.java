package org.com.ems.ticket.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = { "org.com.ems.common.api", "org.com.ems.ticket.api",
	"org.com.ems.ticket.service" })
public class TicketServiceApplication extends SpringBootServletInitializer {

    public static void main(final String[] args) {

	SpringApplication.run(TicketServiceApplication.class, args);

    }

}