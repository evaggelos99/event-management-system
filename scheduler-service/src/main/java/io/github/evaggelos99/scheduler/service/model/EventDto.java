package io.github.evaggelos99.scheduler.service.model;

import java.time.Instant;
import java.time.Period;
import java.util.UUID;

public record EventDto(UUID uid,
                       Instant createdAt,
                       Instant lastUpdated,
                       String name,
                       String place,
                       String eventType,
                       UUID organizerId,
                       Integer limitOfPeople,
                       Instant startTime,
                       Period duration,
                       Boolean stremable) {

}
