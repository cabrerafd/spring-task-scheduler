package com.assessment.taskscheduler.task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

public record TaskScheduleRequest(
        @NotBlank(message = "id must not be blank")
        String id,
        @JsonProperty("start_date")
        @FutureOrPresent
        Date startDate
) {
}
