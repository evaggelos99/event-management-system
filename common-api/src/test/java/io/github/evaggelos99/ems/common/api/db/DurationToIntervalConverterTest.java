package io.github.evaggelos99.ems.common.api.db;

import io.r2dbc.postgresql.codec.Interval;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

class DurationToIntervalConverterTest {

    private DurationToIntervalConverter converter;

    @BeforeEach
    void setUp() {

        converter = new DurationToIntervalConverter();
    }

    @Test
    void test() {

        final Duration expected = Duration.ofSeconds(15);
        final Interval actual = this.converter.apply(expected);
        Assertions.assertEquals(expected.get(ChronoUnit.SECONDS), actual.get(ChronoUnit.SECONDS));

    }

}
