package io.github.evaggelos99.ems.common.api.dto;

import java.util.List;
import java.util.UUID;

// { [
// { eventId:null, attendees:["abc", "def]}, { eventId:null, attendees:["abc", "def]}
// ] }

//

public record EventAttendanceReport(List<EventIdAttendeeNameResponse> attendees) {

    private record EventIdAttendeeNameResponse(UUID eventId, List<String> attendeeNames) {}
}
