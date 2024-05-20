package org.com.ems.util;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class DbConfiguration {

    @Bean
    @Profile("integration-tests")
    DataSource dataSource() {

	return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addScript("classpath:/schema.sql")
		.build();

    }

    @Bean
    @Profile("integration-tests")
    JdbcTemplate jdbcTemplate() {

	return new JdbcTemplate(this.dataSource());

    }

}
