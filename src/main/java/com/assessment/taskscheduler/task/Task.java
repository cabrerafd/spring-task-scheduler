package com.assessment.taskscheduler.task;

import com.assessment.taskscheduler.projectplan.ProjectPlan;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    private Integer duration;

    @ManyToOne
    @JoinColumn(
            name = "project_plan_id"
    )
    private ProjectPlan projectPlan;

    @ManyToMany
    @JoinTable(
            name = "dependent_task",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "dependent_task_id")
    )
    private List<Task> dependentTasks;
}
