package com.assessment.taskscheduler.projectplan.dto;

import com.assessment.taskscheduler.task.dto.TaskResponse;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "name", "tasks" })
public record ProjectPlanResponse(
        String id,
        String name
) {
}
