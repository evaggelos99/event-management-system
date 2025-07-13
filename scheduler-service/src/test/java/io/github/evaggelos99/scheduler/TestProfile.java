package io.github.evaggelos99.scheduler;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.Set;

@QuarkusTest
public class TestProfile implements QuarkusTestProfile {

    @Override
    public String getConfigProfile() {
        return "test";
    }

    @Override
    public Set<Class<?>> getEnabledAlternatives() {
        return Set.of();
    }
}
