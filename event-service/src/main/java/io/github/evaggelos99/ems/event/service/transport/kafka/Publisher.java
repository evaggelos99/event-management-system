package io.github.evaggelos99.ems.event.service.transport.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@Component
public class Publisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);

    private final String topicName;
    private final KafkaTemplate<String, Serializable> template;

    public Publisher(@Autowired final KafkaTemplate<String, Serializable> template,
                     @Value("${io.github.evaggelos99.ems.event.topic.add-attendee}") final String topicName) {

        this.template = template;
        this.topicName = topicName;
    }

    public Flux<?> send(final String msg) {

        return Flux.fromIterable(IntStream.range(0, 15000).mapToObj(x_ -> {

            final CompletableFuture<SendResult<String, Serializable>> future = this.template.send(this.topicName, msg);

            return Mono.fromFuture(future).map(x -> x.getProducerRecord().value()).doOnError(this::logError);
        }).toList());

    }

    private void logError(final Throwable throwable) {

        LOGGER.warn("Error when publishing to topic: " + this.topicName, throwable);
    }
}
