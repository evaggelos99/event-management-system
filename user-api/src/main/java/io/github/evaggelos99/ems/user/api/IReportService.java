package io.github.evaggelos99.ems.user.api;

import io.github.evaggelos99.ems.common.api.dto.EventAttendanceReport;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface IReportService {

    /**
     * Generate a report about how many attendees actually came to events
     */
    //  TODO create specific response
    EventAttendanceReport generateAttendeesCame(List<UUID> eventUuidList);

    /**
     * Generate a report about the total number of events organized within a specific time period.
     */
    // todo do we cascade certain elements?
//     void generateTotalEventsReport(Instant from, Instant to);

    /**
     * Generate a report about the most popular events based on attendee count.
     */
    // TODO return specific elements return top 10 of events
//     Object generatePopularEventsReport();

    /**
     * Generate a report about the revenue generated from ticket sales for a specific event.
     */
    // todo add auth rules for an organizer(must have all roles) or admin to see this
    // Object generateRevenueReport(UUID eventUuid);

    /**
     * Generate a report about the average attendance rate across all events based on organizer & type
     */
    // Object generateAverageAttendanceReport(UUID organizerUuid, EventType eventType);

    /**
     * Generate a report about the events with the highest no-show rates.
     */
    // todo top 10
    // Object generateNoShowReport();

    /**
     * Generate a report about attendee demographics (e.g., age, location) for a specific event.
     */
    // pretty advanced
    // void generateAttendeeDemographicsReport();

    /**
     * Generate a report about the feedback ratings for events. TODO make feedback table in attendee
     */
    // void generateEventFeedbackReport(List<UUID> eventUuidList);

}
