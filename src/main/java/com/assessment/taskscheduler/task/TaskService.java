package com.assessment.taskscheduler.task;

import com.assessment.taskscheduler.projectplan.ProjectPlan;
import com.assessment.taskscheduler.projectplan.ProjectPlanService;
import com.assessment.taskscheduler.task.dto.TaskCreateRequest;
import com.assessment.taskscheduler.task.dto.TaskResponse;
import com.assessment.taskscheduler.task.dto.TaskScheduleRequest;
import com.assessment.taskscheduler.task.dto.TaskScheduleResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final ProjectPlanService projectPlanService;

    public List<TaskScheduleResponse> scheduleTasks(List<TaskScheduleRequest> request) {
        List<TaskScheduleResponse> responses = new ArrayList<>();

        request.forEach(scheduleRequest -> {
            Task task = findTaskById(scheduleRequest.id());
            responses.addAll(createScheduleList(List.of(task), null));
        });

        return responses;
    }

    private List<TaskScheduleResponse> createScheduleList(List<Task> tasks, Date startDate) {
        List<TaskScheduleResponse> responses = new ArrayList<>();
        if (startDate == null) startDate = new Date();

        for (Task task : tasks) {
            if (task.hasDependentTasks()) {
                List<TaskScheduleResponse> dependentScheduleList = createScheduleList(task.getDependentTasks(), startDate);
                responses.addAll(dependentScheduleList);

                if (!dependentScheduleList.isEmpty())
                    startDate = dependentScheduleList.get(dependentScheduleList.size() - 1).endDate();
            }

            if (task.getStatus() == TaskStatus.COMPLETED) continue;
            if (task.getEndDate() != null) continue;

            TaskScheduleResponse response = generateSchedule(task, startDate);
            task.setStartDate(response.startDate());
            task.setEndDate(response.endDate());
            repository.save(task);

            startDate = response.endDate();
            responses.add(response);
        }

        return responses;
    }

    private TaskScheduleResponse generateSchedule(Task task, Date startDate) {
        if (startDate == null) startDate = new Date();

        int duration = task.getDuration();
        Calendar c = Calendar.getInstance();
        c.setTime(startDate);
        c.add(Calendar.DATE, duration);

        Date endDate = c.getTime();

        return new TaskScheduleResponse(
                task.getId(),
                task.getDuration(),
                startDate,
                endDate
        );
    }

    public TaskResponse getTaskById(String id) {
        Task task = findTaskById(id);

        List<String> dependentTaskIds = new ArrayList<>();
        if (task.hasDependentTasks()) {
            task.getDependentTasks().forEach(dependentTask -> dependentTaskIds.add(dependentTask.getId()));
        }

        return new TaskResponse(
                task.getId(),
                task.getName(),
                task.getStatus(),
                task.getDuration(),
                task.getStartDate(),
                task.getEndDate(),
                dependentTaskIds,
                task.getProjectPlan().getId()
        );
    }

    public TaskResponse createTask(TaskCreateRequest request) {
        ProjectPlan projectPlan = projectPlanService.findProjectPlanById(request.projectPlanId());

        Task task = repository.save(
                Task.builder()
                        .name(request.name())
                        .duration(request.duration())
                        .status(TaskStatus.ON_DECK)
                        .projectPlan(projectPlan)
                        .build()
        );

        return new TaskResponse(
                task.getId(),
                task.getName(),
                task.getStatus(),
                task.getDuration(),
                task.getStartDate(),
                task.getEndDate(),
                Collections.emptyList(),
                projectPlan.getId()
        );
    }

    public TaskResponse updateTaskStatus(String id) {
        Task task = findTaskById(id);
        TaskStatus status = task.getStatus();

        if (task.isCompleted()) throw new IllegalStateException("Task is already completed");

        if (!task.incompleteTasks().isEmpty()) {
            throw new IllegalStateException(String.format(
                    "Cannot start task since dependent task %s is/are not yet completed",
                    task.incompleteTasks().stream().map(Task::getId).collect(Collectors.joining(", "))
            ));
        }

        task.setStatus(status.nextStatus());

        Task updatedTask = repository.save(task);

        return new TaskResponse(
                updatedTask.getId(),
                updatedTask.getName(),
                updatedTask.getStatus(),
                updatedTask.getDuration(),
                updatedTask.getStartDate(),
                updatedTask.getEndDate(),
                Collections.emptyList(),
                updatedTask.getProjectPlan().getId()
        );
    }

    private Task findTaskById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Task with id %s not found.", id))
        );
    }
}
