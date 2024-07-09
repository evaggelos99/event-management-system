package org.com.ems.common.api.db;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.com.ems.common.api.domainobjects.EventType;
import org.springframework.stereotype.Component;

@Component
public class ArrayToListOfEventTypesConverter implements Function<EventType[], List<EventType>> {

    @Override
    public List<EventType> apply(final EventType[] array) {

	final List<EventType> list = new LinkedList<>();

	if (array == null)
	    return list;

	Collections.addAll(list, array);

	return list;

    }
}
