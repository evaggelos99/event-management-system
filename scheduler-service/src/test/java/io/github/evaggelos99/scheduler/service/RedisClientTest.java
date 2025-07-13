package io.github.evaggelos99.scheduler.service;

import io.github.evaggelos99.scheduler.service.component.RedisClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

@QuarkusTest
@TestProfile(io.github.evaggelos99.scheduler.TestProfile.class)
class RedisClientTest {

    @Inject
    RedisClient redisClient;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void existsForReminder() {
        System.out.println(redisClient.existsForFeedback(UUID.randomUUID()));
    }

    @Test
    void cacheReminderAttendee() {
    }

    @Test
    void existsForFeedback() {
    }

    @Test
    void cacheFeedbackAttendee() {
    }
}