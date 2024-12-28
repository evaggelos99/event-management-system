package io.github.evaggelos99.ems.common.api.db;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

@Component
public class ArrayToListOfUuidConverter implements Function<UUID[], List<UUID>> {

    @Override
    public List<UUID> apply(final UUID[] array) {

        final List<UUID> list = new LinkedList<>();

        if (array == null)
            return list;

        Collections.addAll(list, array);

        return list;
    }
}
