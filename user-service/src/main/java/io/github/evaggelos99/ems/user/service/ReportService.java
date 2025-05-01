package io.github.evaggelos99.ems.user.service;

import io.github.evaggelos99.ems.common.api.dto.EventAttendanceReport;
import io.github.evaggelos99.ems.user.api.IReportService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ReportService implements IReportService {

    private final ReportRepository reportRepository;

    public ReportService(final ReportRepository reportRepository) {

        this.reportRepository = reportRepository;
    }

    @Override
    public EventAttendanceReport generateAttendeesCame(final List<UUID> eventUuidList) {

//        eventUuidList.stream().map(reportRepository::attendeesCame);
        return null;
    }

}
