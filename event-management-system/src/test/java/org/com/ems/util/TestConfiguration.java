package org.com.ems.util;

import java.sql.Array;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class TestConfiguration {

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

    @Bean
    @Profile("integration-tests")
    Function<Array, List<UUID>> arrayToListOfUuid() {

	return array -> {

	    final List<UUID> list = new LinkedList<>();

	    try {

		for (final Object uuid : (Object[]) array.getArray()) {

		    list.add((UUID) uuid);

		}
	    } catch (final SQLException e) {

	    } catch (final ClassCastException cce) {

	    }

	    return list;
	};

    }

}