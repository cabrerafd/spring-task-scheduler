package com.assessment.taskscheduler.projectplan;

import com.assessment.taskscheduler.task.Task;
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
public class ProjectPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    @OneToMany(
            mappedBy = "projectPlan",
            fetch = FetchType.EAGER
    )
    private List<Task> tasks;

}