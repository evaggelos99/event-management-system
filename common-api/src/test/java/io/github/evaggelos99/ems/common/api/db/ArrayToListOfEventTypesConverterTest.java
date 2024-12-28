package io.github.evaggelos99.ems.common.api.db;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class ArrayToListOfEventTypesConverterTest {

    private static final EventType EVENT_TYPE = EventType.CONFERENCE;

    ArrayToListOfEventTypesConverter converter;

    @BeforeEach
    void setUp() {

        this.converter = new ArrayToListOfEventTypesConverter();

    }

    @Test
    void apply_givenNullArray_() {

        final List<EventType> actualList = this.converter.apply(null);

        Assertions.assertTrue(actualList.isEmpty());

    }

    @Test
    void apply_givenValidEventTypeArray_thenExpectForListContainCorrectElements_andTheExactSameElement() {

        final List<EventType> actualList = this.converter.apply(new EventType[]{EVENT_TYPE});

        Assertions.assertTrue(actualList.size() == 1);
        Assertions.assertEquals(EVENT_TYPE, actualList.get(0));

    }

}
