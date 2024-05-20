package org.com.ems.db.rowmappers;

import java.time.Duration;
import java.util.function.Function;

import org.postgresql.util.PGInterval;
import org.springframework.stereotype.Component;

@Component
public class DurationToIntervalConverter implements Function<Duration, Object> {

    @Override
    public Object apply(final Duration duration) {

	return new PGInterval(0, 0, 0, 0, 0, duration.getSeconds());

    }

}
