package io.github.evaggelos99.ems.event.stream.simulator.beans;

import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.stream.simulator.remote.EventLookUpRemoteService;
import io.github.evaggelos99.ems.event.stream.simulator.remote.EventStreamingPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SimulatorExecutor {

    private final EventStreamingPublisher publisher;
    private final EventLookUpRemoteService lookUpRemoteService;
    private final boolean enabled;

    /**
     * C-or
     * @param publisher class that publishes random event payloads to a topic
     * @param lookUpRemoteService service that can look up events
     * @param enabled flag that determines whenever the simulator emitter will be enabled
     */
    public SimulatorExecutor(final EventStreamingPublisher publisher,
                             final EventLookUpRemoteService lookUpRemoteService,
                             @Value("${io.github.evaggelos99.ems.event.simulator.enabled:false}") final boolean enabled) {
        this.publisher = publisher;
        this.lookUpRemoteService = lookUpRemoteService;
        this.enabled = enabled;
    }

    @Scheduled(cron = "0 */30 * * * *")
    private void streamEvents() {
        if (enabled) {
            lookUpRemoteService.getAllEventDtos()
                    .filter(EventDto::streamable)
                    .flatMap(publisher::streamEvents)
                    .subscribe();
        }
    }

}
