package com.assessment.taskscheduler.config;

import com.assessment.taskscheduler.projectplan.ProjectPlan;
import com.assessment.taskscheduler.projectplan.ProjectPlanRepository;
import com.assessment.taskscheduler.task.Task;
import com.assessment.taskscheduler.task.TaskRepository;
import com.assessment.taskscheduler.task.TaskStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import net.datafaker.Faker;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Seed {

    private final ProjectPlanRepository projectPlanRepository;
    private final TaskRepository taskRepository;

    @Bean
    CommandLineRunner dbSeed() {
        return args -> {
            Faker faker = new Faker();

            // generate project plans
            List<ProjectPlan> generatedProjectPlans = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                String projectName = faker.natoPhoneticAlphabet().codeWord();

                ProjectPlan projectPlan = projectPlanRepository.save(
                        ProjectPlan.builder()
                                .name(projectName)
                                .build()
                );

                generatedProjectPlans.add(projectPlan);
            }

            // generate tasks
            for (int i = 0; i < 20; i++) {
                String taskName = faker.name().fullName();
                ProjectPlan projectPlan = generatedProjectPlans.get(faker.number().numberBetween(0, 5));

                // generate dependent tasks
                List<Task> dependentTasks = new ArrayList<>();
                int numberOfDependency = faker.number().numberBetween(0, 7);
                for (int j = 0; j < numberOfDependency; j++) {
                    Task task = taskRepository.save(
                            Task.builder()
                                    .name(faker.name().fullName())
                                    .duration(faker.number().numberBetween(1, 10))
                                    .status(faker.options().option(TaskStatus.class))
                                    .projectPlan(projectPlan)
                                    .build()
                    );

                    dependentTasks.add(task);
                }

                taskRepository.save(
                        Task.builder()
                                .name(taskName)
                                .duration(faker.number().numberBetween(1, 10))
                                .status(TaskStatus.ON_DECK)
                                .projectPlan(projectPlan)
                                .dependentTasks(dependentTasks)
                                .build()
                );
            }
        };
    }
}
