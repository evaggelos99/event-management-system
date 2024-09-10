package io.github.evaggelos99.ems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication
public class EventManagementSystemApplication extends SpringBootServletInitializer {

	public static void main(final String[] args) {

		SpringApplication.run(EventManagementSystemApplication.class, args);

	}

}