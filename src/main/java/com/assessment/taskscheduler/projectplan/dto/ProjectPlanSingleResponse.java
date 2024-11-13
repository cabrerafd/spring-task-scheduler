package com.assessment.taskscheduler.projectplan.dto;

import com.assessment.taskscheduler.task.dto.TaskResponse;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProjectPlanSingleResponse(
        String id,
        String name,
        List<TaskResponse> tasks
) {
}
