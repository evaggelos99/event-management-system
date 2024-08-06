package com.github.evaggelos99.ems.common.api.db;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.evaggelos99.ems.common.api.db.DurationToIntervalConverter;

import io.r2dbc.postgresql.codec.Interval;

class DurationToIntervalConverterTest {

    DurationToIntervalConverter converter;

    @BeforeEach
    void setUp() {

	this.converter = new DurationToIntervalConverter();

    }

    @Test
    void test() {

	final Duration expected = Duration.ofSeconds(15);
	final Interval actual = this.converter.apply(expected);
	Assertions.assertEquals(expected.get(ChronoUnit.SECONDS), actual.get(ChronoUnit.SECONDS));

    }

}
