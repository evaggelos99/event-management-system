package io.github.evaggelos99.ems.event.api.repo;

import io.github.evaggelos99.ems.event.api.SponsorEventMapping;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.function.BiFunction;

@Component
public class SponsorMappingRowMapper implements BiFunction<Row, RowMetadata, SponsorEventMapping> {

    @Override
    public SponsorEventMapping apply(final Row row, final RowMetadata rowMetadata) {

        // TODO this returns list it will break
        return new SponsorEventMapping(row.get("event_id", UUID.class), row.get("sponsor_id", UUID.class));
    }
}
