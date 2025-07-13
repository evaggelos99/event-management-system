package io.github.evaggelos99.scheduler.service;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.Quarkus;

public class SchedulerApplication implements QuarkusApplication {

    @Override
    public int run(final String... args) throws Exception {
        Quarkus.waitForExit(); // Keeps the application running
        return 0;
    }
}
