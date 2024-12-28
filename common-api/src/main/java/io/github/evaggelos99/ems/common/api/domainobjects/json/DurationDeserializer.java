package io.github.evaggelos99.ems.common.api.domainobjects.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Duration;

public final class DurationDeserializer extends JsonDeserializer<Duration> {

    @Override
    public Duration deserialize(final JsonParser p, final DeserializationContext ctxt)
            throws IOException {

        return Duration.parse(p.getText());
    }

}
