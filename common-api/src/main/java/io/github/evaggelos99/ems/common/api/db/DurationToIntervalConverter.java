package io.github.evaggelos99.ems.common.api.db;

import io.r2dbc.postgresql.codec.Interval;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.function.Function;

@Component
public class DurationToIntervalConverter implements Function<Duration, Object> {

    @Override
    public Interval apply(final Duration duration) {

        return Interval.of(duration);
    }
}
