package io.github.evaggelos99.ems.sponsor.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main class
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication(scanBasePackages = {"io.github.evaggelos99.ems.common.api",
        "io.github.evaggelos99.ems.sponsor.api", "io.github.evaggelos99.ems.sponsor.service"})
public class SponsorServiceApplication {

    public static void main(final String[] args) {

        SpringApplication.run(SponsorServiceApplication.class, args);

    }

}