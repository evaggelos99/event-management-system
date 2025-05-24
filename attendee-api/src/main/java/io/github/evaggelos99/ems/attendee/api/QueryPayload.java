package io.github.evaggelos99.ems.attendee.api;

import java.util.UUID;

public record QueryPayload(UUID uuid) {

    public QueryPayload(UUID uuid) {
        this.uuid = uuid;
    }

}
