package io.github.evaggelos99.ems.event.service.transport.kafka;

import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.service.IEventMessagingService;
import io.github.evaggelos99.ems.event.service.EventService;
import io.github.evaggelos99.ems.kafka.lib.deserializer.ObjectDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class PoolingConsumer implements ApplicationListener<ApplicationReadyEvent> {

    public static final Duration POLL_DURATION = Duration.ofMillis(500);
    public static final int MAX_POLL_RECORDS = Integer.MAX_VALUE / 2;
    private static final Logger LOGGER = LoggerFactory.getLogger(PoolingConsumer.class);
    private final IEventMessagingService eventMessagingService;
    private final ObjectDeserializer objectDeserializer;
    private final KafkaConsumer<String, byte[]> consumer;
    private final String topicNamePrefix;
    private final List<String> subscribedTopics = new LinkedList<>();

    /**
     * C-or
     *
     * @param eventMessagingService the {@link EventService}
     */
    public PoolingConsumer(final IEventMessagingService eventMessagingService,
                           final ObjectDeserializer objectDeserializer,
                           @Qualifier("kafkaConsumerProperties") Map<String, Object> kafkaConsumerProperties,
                           @Value("${io.github.evaggelos99.ems.event.topic.event-streaming-prefix}") String topicNamePrefix) {

        this.eventMessagingService = eventMessagingService;
        this.objectDeserializer = objectDeserializer;
        this.topicNamePrefix = topicNamePrefix;
        kafkaConsumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "event-service-event-stream-group");
        kafkaConsumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
        kafkaConsumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<>(kafkaConsumerProperties);
    }


    //    @Scheduled(cron = "*/10 * * * * *")
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {

        try {
            Thread.sleep(10_000); // initial delay
        } catch (InterruptedException ignored) {

        }

        while (true) {

            final List<String> listOfTopics = consumer.listTopics().keySet()
                    .stream()
                    .filter(partitionInfos -> partitionInfos.startsWith(topicNamePrefix))
                    .toList();

            if (!subscribedTopics.equals(listOfTopics)) {
                subscribedTopics.clear();
                subscribedTopics.addAll(listOfTopics);
                consumer.assign(
                        subscribedTopics.stream()
                                .flatMap(topic -> consumer.partitionsFor(topic).stream()
                                        .map(partitionInfo -> new TopicPartition(topic, partitionInfo.partition())))
                                .toList());
            }

            if (!consumer.assignment().isEmpty()) {

                final List<EventStreamPayload> eventStreamPayloadsToBeProcessed = new LinkedList<>();
                var records = consumer.poll(POLL_DURATION);
                records.forEach(record -> {
                    eventStreamPayloadsToBeProcessed.add((EventStreamPayload) objectDeserializer.convertBytesToObject(record.value()));
                });

                eventMessagingService.saveMultipleEventStreamPayload(eventStreamPayloadsToBeProcessed);
            }

            consumer.commitAsync();
        }
    }
}

