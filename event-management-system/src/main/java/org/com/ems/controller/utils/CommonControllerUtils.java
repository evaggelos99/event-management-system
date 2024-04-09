package org.com.ems.controller.utils;

import java.util.UUID;

/**
 * Util class for controllers
 *
 * @author Evangelos Georgiou
 *
 */
public final class CommonControllerUtils {

	private CommonControllerUtils() {

	}

	/**
	 * Util method to convert String to uuid
	 *
	 * @param uuid
	 *
	 * @return the converted {@link UUID}
	 */
	public static UUID stringToUUID(final String uuid) {
		return UUID.fromString(uuid);
	}
}
