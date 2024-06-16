package org.com.ems.db.rowmappers.util;

import java.time.Duration;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.r2dbc.postgresql.codec.Interval;

@Component
public class DurationToIntervalConverter implements Function<Duration, Object> {

    @Override
    public Object apply(final Duration duration) {

	return Interval.of(duration);

    }

}
