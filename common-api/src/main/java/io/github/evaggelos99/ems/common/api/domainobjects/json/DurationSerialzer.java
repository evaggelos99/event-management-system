package io.github.evaggelos99.ems.common.api.domainobjects.json;

import java.io.IOException;
import java.time.Duration;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class DurationSerialzer extends JsonSerializer<Duration> {

	@Override
	public void serialize(final Duration value, final JsonGenerator gen, final SerializerProvider serializers)
			throws IOException {

		gen.writeString(value.toString());
	}

}
