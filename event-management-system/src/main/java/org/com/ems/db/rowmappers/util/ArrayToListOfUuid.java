package org.com.ems.db.rowmappers.util;

import java.sql.Array;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ArrayToListOfUuid implements Function<Array, List<UUID>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayToListOfUuid.class);

    @Override
    public List<UUID> apply(final Array array) {

	final List<UUID> list = new LinkedList<>();

	try {

	    for (final UUID uuid : (UUID[]) array.getArray()) {

		list.add(uuid);

	    }
	} catch (final SQLException e) {

	    LOGGER.error("Exception occured", e);
	} catch (final ClassCastException cce) {

	    LOGGER.error("The array was not of type UUID", cce);
	}

	return list;

    }
}
