package io.github.evaggelos99.ems.organizer.api.converters;

import io.github.evaggelos99.ems.organizer.api.Organizer;
import io.github.evaggelos99.ems.organizer.api.OrganizerDto;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class OrganizerToOrganizerDtoConverter implements Function<Organizer, OrganizerDto> {

    @Override
    public OrganizerDto apply(final Organizer organizer) {

        return OrganizerDto.builder()
                .uuid(organizer.getUuid())
                .createdAt(organizer.getCreatedAt())
                .lastUpdated(organizer.getLastUpdated())
                .name(organizer.getName())
                .website(organizer.getWebsite())
                .information(organizer.getInformation())
                .eventTypes(organizer.getEventTypes())
                .contactInformation(organizer.getContactInformation()).build();
    }

}
