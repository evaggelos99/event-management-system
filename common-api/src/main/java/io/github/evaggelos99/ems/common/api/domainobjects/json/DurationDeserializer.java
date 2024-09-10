package io.github.evaggelos99.ems.common.api.domainobjects.json;

import java.io.IOException;
import java.time.Duration;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public final class DurationDeserializer extends JsonDeserializer<Duration> {

	@Override
	public Duration deserialize(final JsonParser p, final DeserializationContext ctxt)
			throws IOException, JacksonException {

		return Duration.parse(p.getText());
	}

}