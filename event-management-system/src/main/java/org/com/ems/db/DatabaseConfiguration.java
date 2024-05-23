package org.com.ems.db;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

@Configuration
@PropertySource("classpath:properties/db.properties")
public class DatabaseConfiguration {

    private final String driver;

    private final String url;

    private final String username;

    private final String password;

    /**
     * C-or
     *
     * @param driver
     * @param url
     * @param username
     * @param password
     */
    public DatabaseConfiguration(@Value("${org.com.ems.db.driver}") final String driver,
				 @Value("${org.com.ems.db.url}") final String url,
				 @Value("${org.com.ems.db.username}") final String username,
				 @Value("${org.com.ems.db.password}") final String password) {

	this.driver = driver;
	this.url = url;
	this.username = username;
	this.password = password;

    }

    @Bean
    DataSource dataSource() {

	final DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	driverManagerDataSource.setUrl(this.url);
	driverManagerDataSource.setUsername(this.username);
	driverManagerDataSource.setPassword(this.password);
	driverManagerDataSource.setDriverClassName(this.driver);

	return driverManagerDataSource;

    }

    @Bean
    JdbcTemplate jdbcTemplate() {

	return new JdbcTemplate(this.dataSource());

    }

}
