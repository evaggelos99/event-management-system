package io.github.evaggelos99.scheduler.service;


import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {

    public static void main(String... args) {
        Quarkus.run(SchedulerApplication.class);
    }
}
