package org.com.ems.db.rowmappers.util;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.com.ems.api.domainobjects.EventType;
import org.springframework.stereotype.Component;

@Component
public class ArrayToListOfEventTypes implements Function<EventType[], List<EventType>> {

    @Override
    public List<EventType> apply(final EventType[] array) {

	final List<EventType> list = new LinkedList<>();

	for (final EventType eventType : array) {

	    list.add(eventType);
	}

	return list;

    }
}
