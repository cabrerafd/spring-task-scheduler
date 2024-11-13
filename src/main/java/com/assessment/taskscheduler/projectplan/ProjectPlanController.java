package com.assessment.taskscheduler.projectplan;

import com.assessment.taskscheduler.projectplan.dto.ProjectPlanResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/project_plans")
@RequiredArgsConstructor
public class ProjectPlanController {

    private final ProjectPlanService service;

    @GetMapping
    public ResponseEntity<List<ProjectPlanResponse>> getAllProjectPlans() {
        return new ResponseEntity<>(service.getAllProjectPlans(), HttpStatus.OK);
    }

}
