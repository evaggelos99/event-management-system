package io.github.evaggelos99.ems.event.simulator.remote;

import io.github.evaggelos99.ems.common.api.service.remote.IRemoteServiceClient;
import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Evangelos Georgiou
 */
@Service
public class EventStreamingPublisher implements IRemoteServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventStreamingPublisher.class);

    private static final String[] randomWords = new String[]{"Lorem", "ipsum", "dolor", "sit", "amet,", "consectetur", "adipiscing", "elit,",
            "sed", "do", "eiusmod", "tempor", "incididunt", "ut", "labore", "et", "dolore",
            "magna", "aliqua.", "Ut", "enim", "ad", "minim", "veniam,", "quis", "nostrud",
            "exercitation", "ullamco", "laboris", "nisi", "ut", "aliquip", "ex", "ea",
            "commodo", "consequat.", "Duis", "aute", "irure", "dolor", "in", "reprehenderit",
            "in", "voluptate", "velit", "esse", "cillum", "dolore", "eu", "fugiat", "nulla",
            "pariatur.", "Excepteur", "sint", "occaecat", "cupidatat", "non", "proident,",
            "sunt", "in", "culpa", "qui", "officia", "deserunt", "mollit", "anim", "id",
            "est", "laborum."};

    private final KafkaTemplate<String, Serializable> template;
    private final String topicName;

    public EventStreamingPublisher(final KafkaTemplate<String, Serializable> template,
                                   @Value("${io.github.evaggelos99.ems.event.simulator.topic.event-streaming}") final String topicName) {

        this.template = template;
        this.topicName = topicName;
    }

    /**
     * {@inheritDoc}
     */
    public Mono<Boolean> streamEvent(final UUID eventId) {

        LOGGER.trace("Publishing data to topic: {}", topicName);

        final CompletableFuture<SendResult<String, Serializable>> fut = template.send(topicName, randomizeEventStream(eventId));
        // return hook?
        return Mono.fromFuture(fut)
                .map(x -> true)
                .doOnError(error -> LOGGER.error("Could not reach send message to EventService", error))
                .onErrorReturn(false);
    }

    private Serializable randomizeEventStream(final UUID eventId) {

        var random = new Random();
        return new EventStreamPayload(eventId, "text", Instant.now(), "body", randomWords[random.nextInt(randomWords.length - 1)],
                "EN", random.nextBoolean(), Map.of());
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
