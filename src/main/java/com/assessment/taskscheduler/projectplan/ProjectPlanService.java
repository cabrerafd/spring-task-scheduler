package com.assessment.taskscheduler.projectplan;

import com.assessment.taskscheduler.projectplan.dto.ProjectPlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectPlanService {

    private final ProjectPlanRepository repository;

    public List<ProjectPlanResponse> getAllProjectPlans() {
        return repository.findAll().stream().map(projectPlan ->
            new ProjectPlanResponse(
                    projectPlan.getId(),
                    projectPlan.getName()
            )
        ).toList();
    }
}
