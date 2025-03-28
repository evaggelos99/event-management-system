package io.github.evaggelos99.ems.common.api.db;

import io.github.evaggelos99.ems.common.api.domainobjects.EventType;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

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
