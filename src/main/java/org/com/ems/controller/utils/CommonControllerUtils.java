package org.com.ems.controller.utils;

import java.util.UUID;

public final class CommonControllerUtils {

	public static UUID stringToUUID(String id) {
		return UUID.fromString(id);
	}
}
