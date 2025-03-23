package io.github.evaggelos99.ems.migrate.service;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Spring boot service used only to run migration scripts
 * As soon as the scripts are run the Spring boot service terminates
 *
 * @author Evangelos Georgiou
 */
@SpringBootApplication
public class MigratorService {

    public static void main(final String[] args) {
        new SpringApplicationBuilder(MigratorService.class).web(WebApplicationType.NONE).run(args);
    }
}