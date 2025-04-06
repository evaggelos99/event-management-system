package io.github.evaggelos99.ems.event.simulator.beans;

import io.github.evaggelos99.ems.event.simulator.remote.EventLookUpRemoteService;
import io.github.evaggelos99.ems.event.simulator.remote.EventStreamingPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.Executors;

@Component
public class SimulatorExecutor {

    private final EventStreamingPublisher publisher;
    private final EventLookUpRemoteService lookUpRemoteService;
    private final UUID eventId;
    private final boolean enabled;

    public SimulatorExecutor(final EventStreamingPublisher publisher, final EventLookUpRemoteService lookUpRemoteService,
                             @Value("${io.github.evaggelos99.ems.event.simulator.topic.uuid}") final String eventId,
                             @Value("${io.github.evaggelos99.ems.event.simulator.enabled:false}") final boolean enabled) {
        this.publisher = publisher;
        this.lookUpRemoteService = lookUpRemoteService;
        this.eventId = UUID.fromString(eventId);
        this.enabled = enabled;
        foo();
    }

    private void foo() {

        while (enabled) {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Executors.newCachedThreadPool()
                    .submit(() -> publisher.streamEvent(eventId)
                            .block());
        }
    }

}
