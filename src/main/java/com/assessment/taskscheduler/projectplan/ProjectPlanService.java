package com.assessment.taskscheduler.projectplan;

import com.assessment.taskscheduler.projectplan.dto.ProjectPlanCreateRequest;
import com.assessment.taskscheduler.projectplan.dto.ProjectPlanListResponse;
import com.assessment.taskscheduler.projectplan.dto.ProjectPlanSingleResponse;
import com.assessment.taskscheduler.task.dto.TaskResponse;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectPlanService {

    private final ProjectPlanRepository repository;

    public List<ProjectPlanListResponse> getAllProjectPlans() {
        return repository.findAll().stream().map(projectPlan ->
            new ProjectPlanListResponse(
                    projectPlan.getId(),
                    projectPlan.getName()
            )
        ).toList();
    }

    public ProjectPlanSingleResponse getProjectPlanById(String id) {
        ProjectPlan projectPlan = findProjectPlanById(id);

        List<TaskResponse> taskResponses = projectPlan.getTasks().stream().map(task -> {
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
                    projectPlan.getId()
            );
        }).toList();

        return new ProjectPlanSingleResponse(
                projectPlan.getId(),
                projectPlan.getName(),
                taskResponses
        );
    }

    public ProjectPlanSingleResponse createProjectPlan(@Valid ProjectPlanCreateRequest request) {
        ProjectPlan projectPlan = repository.save(
                ProjectPlan.builder()
                        .name(request.name())
                        .build()
        );

        return new ProjectPlanSingleResponse(
                projectPlan.getId(),
                projectPlan.getName(),
                null
        );
    }

    public ProjectPlan findProjectPlanById(String id) {
        return repository.findById(id).orElseThrow(() ->
            new EntityNotFoundException(String.format("Project plan with id %s not found", id))
        );
    }
}
