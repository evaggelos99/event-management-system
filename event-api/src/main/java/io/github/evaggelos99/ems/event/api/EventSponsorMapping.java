package io.github.evaggelos99.ems.event.api;

import io.github.evaggelos99.ems.common.api.domainobjects.IMapping;

import java.util.UUID;

public record EventSponsorMapping(UUID eventId, UUID sponsorUuid) implements IMapping {

}
