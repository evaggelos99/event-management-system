package io.github.evaggelos99.scheduler.service.component;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Cron {

    private final SchedulerService schedulerService;
    private final String fetchAttendeesSql;
    private final String sendFeedbackSql;

    @Inject
    public Cron(SchedulerService schedulerService,
                @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.sql.get-attendee-to-be-emailed") String fetchAttendeesSql,
                @ConfigProperty(name = "io.github.evaggelos99.ems.scheduler-service.sql.get-attendees-that-attendeed-past-events-1-day") String sendFeedbackSql) {
        this.schedulerService = schedulerService;
        this.fetchAttendeesSql = fetchAttendeesSql;
        this.sendFeedbackSql = sendFeedbackSql;
    }

    @Scheduled(cron="0 0 */2 * * ?")
    void cronJobReminder(ScheduledExecution execution) {
        schedulerService.sendReminder(fetchAttendeesSql);
    }

    @Scheduled(cron="0 0 */1 * * ?")
    void cronJobFeedback(ScheduledExecution execution) {
        schedulerService.sendFeedback(sendFeedbackSql);
    }

}
