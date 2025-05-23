package io.github.evaggelos99.ems.testcontainerkafka.lib;

import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy;
import org.testcontainers.kafka.KafkaContainer;

public class ExtendedKafkaContainer extends KafkaContainer {

    /**
     * C-or overriding a specific env
     */
    public ExtendedKafkaContainer() {

        super("apache/kafka");

        this.withEnv("KAFKA_LISTENERS", "PLAINTEXT://:9092,BROKER://:9093, CONTROLLER://:9094");
        this.setWaitStrategy(new HostPortWaitStrategy().forPorts(9092));
    }

}
