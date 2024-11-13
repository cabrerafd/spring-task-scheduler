package com.assessment.taskscheduler.projectplan;

import com.assessment.taskscheduler.projectplan.dto.ProjectPlanCreateRequest;
import com.assessment.taskscheduler.projectplan.dto.ProjectPlanListResponse;
import com.assessment.taskscheduler.projectplan.dto.ProjectPlanSingleResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project_plans")
@RequiredArgsConstructor
public class ProjectPlanController {

    private final ProjectPlanService service;

    @GetMapping
    public ResponseEntity<List<ProjectPlanListResponse>> getAllProjectPlans() {
        return new ResponseEntity<>(service.getAllProjectPlans(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectPlanSingleResponse> getProjectPlanById(@PathVariable String id) {
        return new ResponseEntity<>(service.getProjectPlanById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProjectPlanSingleResponse> createProjectPlan(@Valid @RequestBody ProjectPlanCreateRequest request) {
        return new ResponseEntity<>(service.createProjectPlan(request), HttpStatus.CREATED);
    }

}
