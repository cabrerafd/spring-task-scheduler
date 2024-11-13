package com.assessment.taskscheduler.projectplan.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectPlanCreateRequest(
        @NotBlank(message = "name must not be blank")
        String name
) {
}
