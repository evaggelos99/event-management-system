package org.com.ems.util;

import java.sql.Array;
import java.sql.SQLException;
import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import javax.sql.DataSource;

import org.com.ems.api.domainobjects.Event;
import org.com.ems.api.domainobjects.EventType;
import org.com.ems.db.rowmappers.EventRowMapper;
import org.h2.api.Interval;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Profile("integration-tests")
public class TestConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception {

	return http.authorizeHttpRequests(x -> x.anyRequest().hasRole("TEST")).build();

    }

    @Bean
    @Profile("integration-tests")
    DataSource dataSource() {

	return new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2).addDefaultScripts().build();

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

		if (array != null) {

		    for (final Object id : (Object[]) array.getArray()) {

			list.add((UUID) id);

		    }
		}
	    } catch (final SQLException e) {

	    } catch (final ClassCastException cce) {

	    }

	    return list;
	};

    }

    /**
     * Overriding {@link EventRowMapper} because of casting of Arrays
     *
     * @return the row mapper
     */
    @Bean
    @Profile("integration-tests")
    RowMapper<Event> eventRowMapper() {

	return (rs,
		y) -> {

	    final List<UUID> attendees = this.arrayToListOfUuid().apply(rs.getArray("attendee_ids"));

	    final List<UUID> sponsors = this.arrayToListOfUuid().apply(rs.getArray("sponsors_ids"));

	    final Duration duration = this.extractDuration((Interval) rs.getObject("duration"));

	    return new Event(UUID.fromString(rs.getString("id")), rs.getTimestamp("created_at").toInstant(),
		    rs.getTimestamp("last_updated").toInstant(), rs.getString("name"), rs.getString("place"),
		    EventType.valueOf(rs.getString("event_type")), attendees,
		    UUID.fromString(rs.getString("organizer_id")), rs.getInt("limit_of_people"), sponsors,
		    rs.getTimestamp("start_time").toLocalDateTime(), duration);
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

    private Duration extractDuration(final Interval pgInterval) {

	final long hours = pgInterval.getHours();
	final long mins = pgInterval.getMinutes();
	final double seconds = pgInterval.getSeconds();

	return Duration.ofHours(hours).plusMinutes(mins).plusSeconds((long) seconds);

    }

}