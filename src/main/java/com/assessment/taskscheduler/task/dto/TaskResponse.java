package com.assessment.taskscheduler.task.dto;

import com.assessment.taskscheduler.task.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

public record TaskResponse(
        String id,
        String name,
        TaskStatus status,
        Integer duration,
        @JsonProperty("start_date")
        Date startDate,
        @JsonProperty("end_date")
        Date endDate,
        @JsonProperty("dependent_task_ids")
        List<String> dependentTaskIds,
        @JsonProperty("project_plan_id")
        String projectPlanId
) {
}
