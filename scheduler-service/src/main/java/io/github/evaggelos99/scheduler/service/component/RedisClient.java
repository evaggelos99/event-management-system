package io.github.evaggelos99.scheduler.service.component;

import io.vertx.redis.client.RedisAPI;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class RedisClient {

    private final static String EMS_REDIS_KEY_PREFIX = "event-management-system::";
    private final static String REDIS_EMAIL_NOTIFIED_KEY_PREFIX = "reminder-email-notified:";
    private final static String REDIS_FEEDBACK_NOTIFIED_KEY_PREFIX = "feedback-email-notified:";
    private final static String REDIS_FEEDBACK_JWT_RECEIVED_KEY_PREFIX = "feedback-jwt-received:";
    public static final String TTL_REMINDER = "172800";
    public static final String TTL_FEEDBACK = "172800";
    private final RedisAPI lowLevelClient;

    @Inject
    public RedisClient(final RedisAPI lowLevelClient) {

        this.lowLevelClient = lowLevelClient;
    }

    public boolean existsForReminder(String attendeeEmail, UUID eventUid) {
        return lowLevelClient.exists(List.of(getKey(REDIS_EMAIL_NOTIFIED_KEY_PREFIX, eventUid.toString() + ":" + attendeeEmail)))
                .toCompletionStage()
                .toCompletableFuture()
                .join().toBoolean();
    }

    public void cacheReminderAttendee(String attendeeEmail, UUID eventUid) {
        lowLevelClient.setex(getKey(REDIS_EMAIL_NOTIFIED_KEY_PREFIX, eventUid.toString() + ":" + attendeeEmail), TTL_REMINDER, "true")
                .toCompletionStage()
                .toCompletableFuture()
                .join();
    }

    public boolean existsForFeedback(UUID ticketId) {
        return lowLevelClient.exists(List.of(getKey(REDIS_FEEDBACK_NOTIFIED_KEY_PREFIX, ticketId.toString())))
                .toCompletionStage()
                .toCompletableFuture()
                .join().toBoolean();
    }

    public void cacheFeedbackAttendee(UUID ticketId) {
        lowLevelClient.setex(getKey(REDIS_FEEDBACK_NOTIFIED_KEY_PREFIX, ticketId.toString()), TTL_FEEDBACK, "true")
                .toCompletionStage()
                .toCompletableFuture()
                .join();
    }

    public boolean existsForFeedbackJwt(UUID ticketId) {
        return lowLevelClient.exists(List.of(getKey(REDIS_FEEDBACK_JWT_RECEIVED_KEY_PREFIX, ticketId.toString())))
                .toCompletionStage()
                .toCompletableFuture()
                .join().toBoolean();
    }

    public void cacheFeedbackJwt(UUID ticketId) {
        lowLevelClient.setex(getKey(REDIS_FEEDBACK_JWT_RECEIVED_KEY_PREFIX, ticketId.toString()), TTL_FEEDBACK, "true")
                .toCompletionStage()
                .toCompletableFuture()
                .join();
    }

    private static String getKey(final String prefix, final String str) {
        return EMS_REDIS_KEY_PREFIX + prefix + str;
    }

}
