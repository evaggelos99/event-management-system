package io.github.evaggelos99.scheduler.service.component;

import io.github.evaggelos99.scheduler.service.model.AttendeeInfo;
import io.github.evaggelos99.scheduler.service.model.EventNamePlace;
import io.github.evaggelos99.scheduler.service.model.TicketInfo;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@ApplicationScoped
public class SchedulerService {

    private static final String ADMIN_EMAIL = "noreply@event-management-system.com";
    private static final String FROM_NAME = "Event management System";
    private static final String SUBJECT_REMINDER = "TODO The event you booked is in two days.";
    private static final String SUBJECT_FEEDBACK = "Give Feedback about the event you just attended!";
    private static final String FEEDBACK_STR_FORMAT = "Hi %s %s," + "\n" +
            "How was your the event: %s in %s?" +
            "Would you care to give us feedback?" +
            "Please click the link %s to rate your experience!";

    private static final String REMINDER_STR_FORMAT = "Hi %s %s," + "\n" +
            "The event: %s in %s is starting in 2 days.";
    private final DataSource dataSource;
    private final Mailer mailer;
    private final RedisClient redisClient;
    private final String saveEmailSql;
    private String backendUrl;
    private final JwtService jwtService;

    @Inject
    public SchedulerService(DataSource dataSource, Mailer mailer, final RedisClient redisClient,
                            @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.sql.save-email") String saveEmailSql,
                            @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.service.backend-url", defaultValue = "http://localhost:8087/") String backendUrl,
                            JwtService jwtService) {

        this.dataSource = dataSource;
        this.mailer = mailer;
        this.redisClient = redisClient;
        this.saveEmailSql = saveEmailSql;
        this.backendUrl = backendUrl;
        this.jwtService = jwtService;
    }

    public void sendReminder(final String fetchAttendeesSql) {

        Map<EventNamePlace, List<AttendeeInfo>> eventToAttendees = getAllEvents(fetchAttendeesSql);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(saveEmailSql)) {

            connection.setAutoCommit(false); // Start transaction

            eventToAttendees.forEach((k, v) -> {
                if (!v.isEmpty()) {

                    v.forEach(attendeeInfo -> {

                        final String body = String.format(REMINDER_STR_FORMAT, attendeeInfo.firstName(), attendeeInfo.lastName(), k.name(), k.place());

                        final String toName = attendeeInfo.firstName() + " " + attendeeInfo.lastName();
                        final String toEmail = attendeeInfo.email();

                        if (!redisClient.existsForReminder(toEmail, k.eventId())) {
                            mailer.sendMail(EmailBuilder.startingBlank()
                                    .from(FROM_NAME, ADMIN_EMAIL)
                                    .to(toName, toEmail)
                                    .withSubject(SUBJECT_REMINDER)
                                    .withPlainText(body)
                                    .buildEmail(), false);

                            redisClient.cacheReminderAttendee(toEmail, k.eventId());

                            try {
                                statement.setString(1, ADMIN_EMAIL);
                                statement.setString(2, toEmail);
                                statement.setNull(3, Types.ARRAY);
                                statement.setString(4, body);
                                statement.setString(5, FROM_NAME);
                                statement.setString(6, toName);
                                statement.setString(7, SUBJECT_REMINDER);
                                statement.addBatch();
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }

                        }

                    });
                }
            });

            statement.executeBatch(); // Execute batch
            connection.commit(); // Commit transaction
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sendFeedback(final String sendFeedbackSql) {

        List<TicketInfo> eventToAttendees = getAllAttendees(sendFeedbackSql);

        System.out.println(eventToAttendees);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(saveEmailSql)) {

            connection.setAutoCommit(false);

            eventToAttendees.forEach(ticketInfo -> {

                final String body = String.format(FEEDBACK_STR_FORMAT, ticketInfo.firstName(), ticketInfo.lastName(), ticketInfo.eventName(), ticketInfo.eventPlace(), backendUrl+"feedback/" + jwtService.generateJwt(ticketInfo));

                final String toName = ticketInfo.firstName() + " " + ticketInfo.lastName();
                final String toEmail = ticketInfo.attendeeEmail();

                if (!redisClient.existsForFeedback(ticketInfo.ticketId())) {

                    mailer.sendMail(EmailBuilder.startingBlank()
                            .from(FROM_NAME, ADMIN_EMAIL)
                            .to(toName, toEmail)
                            .withSubject(SUBJECT_FEEDBACK)
                            .withPlainText(body)
                            .buildEmail(), false);

                    redisClient.cacheFeedbackAttendee(ticketInfo.ticketId());

                    try {
                        statement.setString(1, ADMIN_EMAIL);
                        statement.setString(2, toEmail);
                        statement.setNull(3, Types.ARRAY);
                        statement.setString(4, body);
                        statement.setString(5, FROM_NAME);
                        statement.setString(6, toName);
                        statement.setString(7, SUBJECT_FEEDBACK);
                        statement.addBatch();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }

                }

            });

            statement.executeBatch(); // Execute batch
            connection.commit(); // Commit transaction
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List<TicketInfo> getAllAttendees(final String sendFeedbackSql) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sendFeedbackSql)) {
            List<TicketInfo> listOfEvents = new LinkedList<>();
            final ResultSet res = statement.executeQuery();

            while (res.next()) {

                final UUID ticketId = res.getObject("ticket_id", UUID.class);
                final UUID eventId = res.getObject("event_id", UUID.class);
                final String eventName = res.getObject("event_name", String.class);
                final String eventPlace = res.getObject("event_place", String.class);
                final String attendeeEmail = res.getObject("attendee_email", String.class);
                final String firstName = res.getObject("first_name", String.class);
                final String lastName = res.getObject("last_name", String.class);

                listOfEvents.add(new TicketInfo(ticketId, eventId, eventName, eventPlace, attendeeEmail, firstName, lastName));
            }

            return listOfEvents;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Map<EventNamePlace, List<AttendeeInfo>> getAllEvents(final String sql) {

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            Map<EventNamePlace, List<AttendeeInfo>> listOfEvents = new HashMap<>();
            final ResultSet res = statement.executeQuery();

            while (res.next()) {

                final String attendeeEmail = res.getObject("attendee_email", String.class);
                final String firstName = res.getObject("first_name", String.class);
                final String lastName = res.getObject("last_name", String.class);
                final AttendeeInfo attendeeInfo = new AttendeeInfo(attendeeEmail, firstName, lastName);

                final EventNamePlace dto = new EventNamePlace(res.getObject("event_uid", UUID.class),
                        res.getObject("event_name", String.class),
                        res.getObject("event_place", String.class));

                final List<AttendeeInfo> arr;
                if (!listOfEvents.containsKey(dto)) {
                    arr = new ArrayList<>();
                } else {
                    arr = listOfEvents.get(dto);
                }

                arr.add(attendeeInfo);
                listOfEvents.put(dto, arr);
            }

            return listOfEvents;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
