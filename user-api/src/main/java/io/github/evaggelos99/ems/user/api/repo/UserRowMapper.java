package io.github.evaggelos99.ems.user.api.repo;

import io.github.evaggelos99.ems.common.api.domainobjects.UserRole;
import io.github.evaggelos99.ems.user.api.User;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.function.BiFunction;

@Component
public class UserRowMapper implements BiFunction<Row, RowMetadata, User> {

    @Override
    public User apply(final Row row, final RowMetadata rmd) {

        return new User(row.get("id", UUID.class),
                row.get("created_at", OffsetDateTime.class),
                row.get("last_updated", OffsetDateTime.class),
                row.get("username", String.class),
                row.get("email", String.class),
                row.get("first_name", String.class),
                row.get("last_name", String.class),
                row.get("role", UserRole.class),
                row.get("mobile_phone", String.class),
                row.get("birth_date", LocalDate.class));
    }

}
