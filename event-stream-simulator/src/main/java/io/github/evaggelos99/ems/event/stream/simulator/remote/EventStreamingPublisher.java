package io.github.evaggelos99.ems.event.stream.simulator.remote;

import io.github.evaggelos99.ems.common.api.service.remote.IRemoteServiceClient;
import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.EventDto;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    private final KafkaAdmin kafkaAdmin;
    private final String topicNamePrefix;

    public EventStreamingPublisher(final KafkaTemplate<String, Serializable> template,
                                   KafkaAdmin kafkaAdmin,
                                   @Value("${io.github.evaggelos99.ems.event.simulator.topic.event-streaming-prefix}") final String topicNamePrefix) {

        this.template = template;
        this.kafkaAdmin=kafkaAdmin;
        this.topicNamePrefix = topicNamePrefix;
    }

    /**
     * {@inheritDoc}
     */
    public Flux<Boolean> streamEvents(final EventDto eventDto) {

        kafkaAdmin.createOrModifyTopics(new NewTopic(topicNamePrefix+eventDto.uuid(),3, (short) 1));

        LOGGER.trace("Publishing data to topic: {}", topicNamePrefix + eventDto.uuid());

        return Flux.fromStream(IntStream.range(0, 1000)
                .mapToObj(num -> template.send(topicNamePrefix + eventDto.uuid(), randomizeEventStream(eventDto))))
                .map(x -> true)
                .doOnError(error -> LOGGER.error("Could not reach send message to EventService", error))
                .onErrorReturn(false);
    }

    private Serializable randomizeEventStream(final EventDto eventDto) {

        var random = new Random();

        return new EventStreamPayload(eventDto.uuid(), "text", Instant.now(), "body", constructMessage(random),
                "EN", random.nextBoolean(), "{}");
    }

    private String constructMessage(final Random random) {

        return IntStream.range(0,10).mapToObj(x -> randomWords[random.nextInt(randomWords.length - 1)])
                .collect(Collectors.joining(" ", "","."));
    }

    @Override
    public Mono<Boolean> ping() {

        return Mono.just(true);
    }

}
