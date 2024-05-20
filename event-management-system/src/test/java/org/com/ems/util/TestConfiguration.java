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
import org.h2.api.Interval;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

@Configuration
public class TestConfiguration {

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

		for (final Object uuid : (Object[]) array.getArray()) {

		    list.add((UUID) uuid);

		}
	    } catch (final SQLException e) {

	    } catch (final ClassCastException cce) {

	    }

	    return list;
	};

    }

    @Bean
    @Profile("integration-tests")
    RowMapper<Event> eventRowMapper() {

	return (rs,
		y) -> {

	    final List<UUID> attendees = this.arrayToListOfUuid().apply(rs.getArray("attendee_ids"));

	    final Duration duration = this.extractDuration((Interval) rs.getObject("duration"));

	    final String sponsorId = rs.getString("sponsor_id");

	    final UUID sponsorUuid = sponsorId != null ? UUID.fromString(sponsorId) : null;

	    return new Event(UUID.fromString(rs.getString("id")), rs.getTimestamp("last_updated").toInstant(),
		    rs.getString("denomination"), rs.getString("place"), EventType.valueOf(rs.getString("event_type")),
		    attendees, UUID.fromString(rs.getString("organizer_id")), rs.getInt("limit_of_people"), sponsorUuid,
		    rs.getTimestamp("start_time").toLocalDateTime(), duration);
	};

    }

    private Duration extractDuration(final Interval pgInterval) {

	final long hours = pgInterval.getHours();
	final long mins = pgInterval.getMinutes();
	final double seconds = pgInterval.getSeconds();

	return Duration.ofHours(hours).plusMinutes(mins).plusSeconds((long) seconds);

    }

}