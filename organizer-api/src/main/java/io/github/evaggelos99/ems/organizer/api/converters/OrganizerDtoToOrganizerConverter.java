package io.github.evaggelos99.ems.organizer.api.converters;

import java.util.function.Function;

import org.springframework.stereotype.Component;

import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;

@Component
public class OrganizerDtoToOrganizerConverter implements Function<OrganizerDto, Organizer> {

	@Override
	public Organizer apply(final OrganizerDto organizerDto) {

		return new Organizer(organizerDto.uuid(), organizerDto.createdAt(), organizerDto.lastUpdated(),
				organizerDto.name(), organizerDto.website(), organizerDto.information(), organizerDto.eventTypes(),
				organizerDto.contactInformation());

	}

}
