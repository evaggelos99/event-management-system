package org.com.ems.common.api.db;

import java.util.List;

import org.com.ems.common.api.domainobjects.EventType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

	final List<EventType> actualList = this.converter.apply(new EventType[] { EVENT_TYPE });

	Assertions.assertTrue(actualList.size() == 1);
	Assertions.assertEquals(EVENT_TYPE, actualList.get(0));

    }

}
