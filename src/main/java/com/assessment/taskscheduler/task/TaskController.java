package com.assessment.taskscheduler.task;

import com.assessment.taskscheduler.task.dto.TaskCreateRequest;
import com.assessment.taskscheduler.task.dto.TaskResponse;
import com.assessment.taskscheduler.task.dto.TaskScheduleRequest;
import com.assessment.taskscheduler.task.dto.TaskScheduleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable String id) {
        return new ResponseEntity<>(service.getTaskById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskCreateRequest request) {
        return new ResponseEntity<>(service.createTask(request), HttpStatus.OK);
    }

    @PostMapping("/schedule")
    public ResponseEntity<List<TaskScheduleResponse>> scheduleTasks(@Valid @RequestBody List<TaskScheduleRequest> request) {
        return new ResponseEntity<>(service.scheduleTasks(request), HttpStatus.OK);
    }

    @PostMapping("/{id}/update_status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable String id) {
        return new ResponseEntity<>(service.updateTaskStatus(id), HttpStatus.OK);
    }

}
