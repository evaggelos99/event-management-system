package io.github.evaggelos99.ems.event.service.transport.kafka;

import io.github.evaggelos99.ems.common.api.transport.EventStreamPayload;
import io.github.evaggelos99.ems.event.api.service.IEventMessagingService;
import io.github.evaggelos99.ems.kafka.lib.deserializer.ObjectDeserializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnProperty(prefix = "kafka", name = "enabled", havingValue = "true", matchIfMissing = true)
public class EventStreamPoolingConsumer {

    private static final Duration POLL_DURATION = Duration.ofMillis(500);
    private static final int MAX_POLL_RECORDS = Integer.MAX_VALUE / 2;

    private final IEventMessagingService eventMessagingService;
    private final ObjectDeserializer objectDeserializer;
    private final KafkaConsumer<String, byte[]> consumer;
    private final String topicNamePrefix;
    private final List<String> subscribedTopics = new LinkedList<>();

    /**
     * C-or
     *
     * @param eventMessagingService   the event service that handles messaging
     * @param objectDeserializer      deserializer that converts from binary to java objects
     * @param kafkaConsumerProperties general properties for consumers
     * @param topicNamePrefix         text prefix for event streaming topics
     */
    public EventStreamPoolingConsumer(final IEventMessagingService eventMessagingService,
                                      final ObjectDeserializer objectDeserializer,
                                      @Qualifier("kafkaConsumerProperties") Map<String, Object> kafkaConsumerProperties,
                                      @Value("${io.github.evaggelos99.ems.event.topic.event-streaming-prefix}") String topicNamePrefix) {

        this.eventMessagingService = eventMessagingService;
        this.objectDeserializer = objectDeserializer;
        this.topicNamePrefix = topicNamePrefix;
        this.consumer = new KafkaConsumer<>(setupKafkaConsumer(kafkaConsumerProperties));
    }

    @Scheduled(fixedRate = 600)
    public void consumeEventStreamingPrefixedTopics() {

        execute();
    }

    @Async
    private void execute() {

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
            records.forEach(record -> addMessageToList(record, eventStreamPayloadsToBeProcessed));

            eventMessagingService.saveMultipleEventStreamPayload(eventStreamPayloadsToBeProcessed);
        }

        consumer.commitAsync();
    }

    private boolean addMessageToList(final ConsumerRecord<String, byte[]> record, final List<EventStreamPayload> eventStreamPayloadsToBeProcessed) {
        return eventStreamPayloadsToBeProcessed.add((EventStreamPayload) objectDeserializer.convertBytesToObject(record.value()));
    }

    private Map<String, Object> setupKafkaConsumer(final Map<String, Object> kafkaConsumerProperties) {
        kafkaConsumerProperties.put(ConsumerConfig.GROUP_ID_CONFIG, "event-service-event-stream-group");
        kafkaConsumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, MAX_POLL_RECORDS);
        kafkaConsumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return kafkaConsumerProperties;
    }
}

