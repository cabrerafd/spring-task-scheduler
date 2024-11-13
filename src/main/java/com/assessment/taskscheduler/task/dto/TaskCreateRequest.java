package com.assessment.taskscheduler.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record TaskCreateRequest(
        @NotBlank(message = "name must be specified")
        String name,
        @Min(value = 1, message = "duration must be at least 1")
        int duration,
        @JsonProperty("project_plan_id")
        @NotBlank(message = "project_plan_id must be specified")
        String projectPlanId
) {
}
