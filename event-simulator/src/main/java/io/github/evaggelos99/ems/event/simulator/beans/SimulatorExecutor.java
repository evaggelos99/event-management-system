package io.github.evaggelos99.ems.event.simulator.beans;

import io.github.evaggelos99.ems.event.api.EventDto;
import io.github.evaggelos99.ems.event.simulator.remote.EventLookUpRemoteService;
import io.github.evaggelos99.ems.event.simulator.remote.EventStreamingPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;

@Component
public class SimulatorExecutor {

    private final EventStreamingPublisher publisher;
    private final EventLookUpRemoteService lookUpRemoteService;
    private final boolean enabled;

    public SimulatorExecutor(final EventStreamingPublisher publisher, final EventLookUpRemoteService lookUpRemoteService,
                             @Value("${io.github.evaggelos99.ems.event.simulator.enabled:false}") final boolean enabled) {
        this.publisher = publisher;
        this.lookUpRemoteService = lookUpRemoteService;
        this.enabled = enabled;
    }

    @Scheduled(cron = "*/4 * * * * *")
    private void foo() {

        lookUpRemoteService.getAllEventDtos().filter(EventDto::streamable)
                .flatMap(publisher::streamEvents)
                .subscribe();
    }

}
