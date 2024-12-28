package io.github.evaggelos99.ems.organizer.api.converters;

import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrganizerToOrganizerDtoConverter implements Function<Organizer, OrganizerDto> {

    @Override
    public OrganizerDto apply(final Organizer organizer) {

        return new OrganizerDto(organizer.getUuid(), organizer.getCreatedAt(), organizer.getLastUpdated(),
                organizer.getName(), organizer.getWebsite(), organizer.getInformation(), organizer.getEventTypes(),
                organizer.getContactInformation());
    }

}
