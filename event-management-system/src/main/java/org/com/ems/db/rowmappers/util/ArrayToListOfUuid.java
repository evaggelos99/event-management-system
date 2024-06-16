package org.com.ems.db.rowmappers.util;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.springframework.stereotype.Component;

@Component
public class ArrayToListOfUuid implements Function<UUID[], List<UUID>> {

    @Override
    public List<UUID> apply(final UUID[] array) {

	final List<UUID> list = new LinkedList<>();

	Collections.addAll(list, array);

	return list;

    }
}
