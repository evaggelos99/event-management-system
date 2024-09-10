package io.github.evaggelos99.ems.common.api.db;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;

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
