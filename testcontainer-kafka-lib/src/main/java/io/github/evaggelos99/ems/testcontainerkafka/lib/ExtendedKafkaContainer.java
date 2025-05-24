package io.github.evaggelos99.ems.testcontainerkafka.lib;

import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.kafka.KafkaContainer;

import java.time.Duration;

public class ExtendedKafkaContainer extends KafkaContainer {

    /**
     * C-or overriding a specific env
     */
    public ExtendedKafkaContainer() {

        super("apache/kafka");

        withEnv("KAFKA_LISTENERS", "PLAINTEXT://:9092,BROKER://:9093, CONTROLLER://:9094")
                .withExposedPorts(9092)
                .setWaitStrategy(new LogMessageWaitStrategy()
                        .withRegEx(".*Kafka Server started.*")
                        .withStartupTimeout(Duration.ofSeconds(60)));
    }

}
