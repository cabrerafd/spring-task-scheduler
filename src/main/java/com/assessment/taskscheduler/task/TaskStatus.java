package com.assessment.taskscheduler.task;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TaskStatus {
    ON_DECK("on_deck"),
    IN_PROGRESS("in_progress"),
    COMPLETED("completed");

    private final String taskStatus;

    TaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    @JsonValue
    public String getTaskStatus() {
        return taskStatus;
    }

    @JsonCreator
    public static TaskStatus fromValue(String value) {
        for (TaskStatus status: values()) {
            if (status.getTaskStatus().equals(value)) return status;
        }

        throw new IllegalArgumentException("Task status mus be 'on_deck', 'in_progress', or 'completed'");
    }
}
