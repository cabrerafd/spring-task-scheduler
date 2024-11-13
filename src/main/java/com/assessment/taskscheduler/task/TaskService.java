package com.assessment.taskscheduler.task;

import com.assessment.taskscheduler.task.dto.TaskResponse;
import com.assessment.taskscheduler.task.dto.TaskScheduleRequest;
import com.assessment.taskscheduler.task.dto.TaskScheduleResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;

    public List<TaskScheduleResponse> scheduleTasks(List<TaskScheduleRequest> request) {
        List<TaskScheduleResponse> responses = new ArrayList<>();

        request.forEach(scheduleRequest -> {
            Task task = findTaskById(scheduleRequest.id());
            responses.addAll(createScheduleList(List.of(task), null));
        });

        return responses;
    }

    private Task findTaskById(String id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("Task with id %s not found.", id))
        );
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
                dependentTaskIds
        );
    }
}
