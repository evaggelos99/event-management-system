package org.com.ems.db.rowmappers.util;

import java.sql.Array;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.com.ems.api.domainobjects.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ArrayToListOfEventTypes implements Function<Array, List<EventType>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArrayToListOfEventTypes.class);

    @Override
    public List<EventType> apply(final Array array) {

	final List<EventType> list = new LinkedList<>();

	try {

	    for (final String eventType : (String[]) array.getArray()) {

		list.add(EventType.valueOf(eventType));

	    }
	} catch (final SQLException e) {

	    LOGGER.error("Exception occured", e);
	} catch (final ClassCastException cce) {

	    LOGGER.error("The array was not of type EventType", cce);
	}

	return list;

    }
}
