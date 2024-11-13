package com.assessment.taskscheduler.projectplan.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "name", "tasks" })
public record ProjectPlanListResponse(
        String id,
        String name
) {
}
