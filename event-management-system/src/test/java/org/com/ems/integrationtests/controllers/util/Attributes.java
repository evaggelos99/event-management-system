package org.com.ems.integrationtests.controllers.util;

import java.util.Set;

public final class Attributes {

	public static final Set<String> attendeeNotNullAttributes = Set.of("uuid", "lastUpdated", "firstName", "lastName");

	public static final Set<String> attendeeNullAttributes = Set.of("ticketIDs");

	public static final Set<String> ticketNotNullAttributes = Set.of("uuid", "lastUpdated", "firstName", "lastName",
			"eventID", "ticketType", "price", "transferable");

	public static final Set<String> ticketNullableAttributes = Set.of("attendeeID", "lastUpdated", "firstName",
			"lastName");

	public static final Set<String> seatInfoAttributes = Set.of("seat", "section");

}
