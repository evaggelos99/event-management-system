package org.com.ems.util;

import java.sql.Array;
import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.com.ems.api.domainobjects.EventType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class TestConfiguration {

//    @Bean
//    @Profile("integration-tests")
//    DataSource dataSource() {
//
//	return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addDefaultScripts().build();
//
//    }
//
//    @Bean
//    @Profile("integration-tests")
//    JdbcTemplate jdbcTemplate() {
//
//	return new JdbcTemplate(this.dataSource());
//
//    }

    @Bean
    @Profile("integration-tests")
    Function<Array, List<UUID>> arrayToListOfUuid() {

	return array -> {

	    final List<UUID> list = new LinkedList<>();

	    try {

		if (array != null) {

		    for (final Object uuid : (Object[]) array.getArray()) {

			list.add((UUID) uuid);

		    }
		}
	    } catch (final SQLException e) {

	    } catch (final ClassCastException cce) {

	    }

	    return list;
	};

    }

    /**
     * Used for the H2 database
     *
     * @return function that cast into Object[] instead of UUID[]
     */
    @Bean
    @Profile("integration-tests")
    Function<Array, List<EventType>> arrayToListOfEventTypes() {

	return array -> {

	    final List<EventType> list = new LinkedList<>();

	    try {

		for (final Object eventType : (Object[]) array.getArray()) {

		    list.add(EventType.valueOf((String) eventType));

		}

	    } catch (final SQLException e) {

	    } catch (final ClassCastException cce) {

	    }

	    return list;
	};

    }

    /**
     * Used for the H2 database
     *
     * @return function that returns the java object duration instead of interval
     */
    @Bean
    @Profile("integration-tests")
    Function<Duration, Object> durationToIntervalConverter() {

	return x -> x;

    }

}