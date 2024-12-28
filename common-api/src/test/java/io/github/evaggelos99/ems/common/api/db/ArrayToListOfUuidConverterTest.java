package io.github.evaggelos99.ems.common.api.db;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

class ArrayToListOfUuidConverterTest {

    private static final UUID RANDOM_UUID = UUID.randomUUID();
    ArrayToListOfUuidConverter converter;

    @BeforeEach
    void setUp() {

        this.converter = new ArrayToListOfUuidConverter();

    }

    @Test
    void apply_givenNullArray_thenExpectListToNotBeNull_andNotContainAnyElements() {

        final List<UUID> actualList = this.converter.apply(null);

        Assertions.assertTrue(actualList.isEmpty());

    }

    @Test
    void apply_givenValidUUIDArray_thenExpectForListContainCorrectElements_andTheExactSameElement() {

        final List<UUID> actualList = this.converter.apply(new UUID[]{RANDOM_UUID});

        Assertions.assertTrue(actualList.size() == 1);
        Assertions.assertEquals(RANDOM_UUID, actualList.get(0));

    }

}
