package com.assessment.taskscheduler.task.dto;

import java.util.Date;

public record TaskScheduleResponse(
        String id,
        int duration,
        Date startDate,
        Date endDate
) {
}
