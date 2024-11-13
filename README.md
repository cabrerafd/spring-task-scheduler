# Task Scheduler
This is a Spring Boot application for a simple task scheduling system. It uses Java 1.17 and Spring Boot 3.3.5.

## Getting Started

### How to build and run the application

#### Requirements
* Java 1.17 SDK

### Build and Run
To build and run the application, simply use the `mvnw` file the comes with the repository.
On the root of the repository, execute the command:
```shell
./mvnw spring-boot:run
```
This will build the project to the `./target` folder, as well as run the application.

Server will start listening at port `8080`.

### Database
The database can be accessed at http://localhost:8080/h2-console. The username is 'sa' and password is 'password'.

The database will be automatically seeded everytime the application starts.
When the application stops, all data is deleted (no persistence).

## Endpoints
Listed here are the endpoints of this application. There is also a postman collection and environment in the `postman` folder.
### Project Plans

#### Get All Project Plans
This endpoint returns all project plans

##### Path
**GET** /project_plans

##### Sample Response
```json
[
    {
        "name": "Bravo",
        "id": "d9b79075-e857-43fc-b78d-a721a92340ed"
    },
    {
        "name": "Romeo",
        "id": "48b27873-3b77-4c7b-8c38-fbcc07c0f301"
    }
]
```

#### Get Project Plan By Id
This endpoint returns the project plan by the given Id. It also includes all the tasks of the project plan.

##### Path
**GET** /project_plans/:id
- id (string, required): the id of the project plan

##### Sample Response
```json
{
    "id": "d5ddf3e8-81d8-4862-a4b9-168f22a5f101",
    "name": "Zulu",
    "tasks": [
        {
            "id": "219a5be3-f400-44ad-bb89-a332933f093b",
            "name": "Jc Larson",
            "status": "on_deck",
            "duration": 9,
            "start_date": null,
            "end_date": null,
            "dependent_task_ids": [],
            "project_plan_id": "d5ddf3e8-81d8-4862-a4b9-168f22a5f101"
        },
        {
            "id": "fcc9fd2d-04b2-4052-a661-ce37b34a7c71",
            "name": "Coy Bartoletti",
            "status": "on_deck",
            "duration": 4,
            "start_date": null,
            "end_date": null,
            "dependent_task_ids": [],
            "project_plan_id": "d5ddf3e8-81d8-4862-a4b9-168f22a5f101"
        },
        {
            "id": "494e4c16-9964-4bc9-a1f9-a00d8514592b",
            "name": "Jordan Kohler",
            "status": "on_deck",
            "duration": 3,
            "start_date": null,
            "end_date": null,
            "dependent_task_ids": [
                "219a5be3-f400-44ad-bb89-a332933f093b",
                "fcc9fd2d-04b2-4052-a661-ce37b34a7c71"
            ],
            "project_plan_id": "d5ddf3e8-81d8-4862-a4b9-168f22a5f101"
        }
    ]
}
```

#### Create Project Plan
This endpoint creates a new project plan.

##### Path
**POST** /project_plans

##### Request Body Sample
```json
{
    "name": "Project Name Here"
}
```
- name (string, required): the name of the project plan

##### Response Body Sample
```json
{
    "id": "110a0886-aded-4c74-864e-a9af0df38ef2",
    "name": "Project Name Here"
}
```

### Tasks

#### Create Task
This endpoint creates a new task.

##### Path
**POST** /tasks

##### Request Body Sample
```json
{
    "name": "Task Name",
    "duration": 1,
    "project_plan_id": "project_plan_id here"
}
```
- name (string, required): the name of the task
- duration (number, required): the number of days to complete the task
- project_plan_id (string, required): the id of the project plan that owns this task

##### Response Body Sample
```json
{
    "id": "2ec7df42-10c7-4dca-9dde-3d2e74019d8c",
    "name": "Task Name",
    "status": "on_deck",
    "duration": 1,
    "start_date": null,
    "end_date": null,
    "dependent_task_ids": [],
    "project_plan_id": "110a0886-aded-4c74-864e-a9af0df38ef2"
}
```

#### Get Task By Id
This endpoint returns the task by the given id. It also lists the ids of the tasks that it depends on.

##### Path
**GET** /tasks/:id
- id (string, required): the task id

##### Response Body Sample
```json
{
    "id": "64744526-0d1d-4ffe-ade3-6a43b244163c",
    "name": "Cecily Steuber",
    "status": "on_deck",
    "duration": 5,
    "start_date": "2024-12-11T03:56:08.053+00:00",
    "end_date": "2024-12-16T03:56:08.053+00:00",
    "dependent_task_ids": [
        "674be725-7b24-4a9e-b935-204a71c768fb",
        "4e922f21-d219-42d2-8451-d2b586d2d9ae",
        "c7a9597b-2387-4e79-b28d-d80c3b77527f",
        "63023dde-df53-4e0b-b422-38e648972670",
        "f6f26406-dd94-46df-99db-94726a07eb21",
        "f601f74f-d170-41f5-909d-b7cf5acaa129"
    ]
}
```

#### Update Task Status
This endpoint updates the task status by the given id. If the task is "on_deck", it will become
"in_progress". If it is "in_progress", it will be "completed". If it is "completed", it will throw
an error stating that the task is already completed.

If any dependent task is not yet completed, it will throw an error since it will only be in progress
once all dependent task is completed.

##### Path
**POST** /tasks/:id/update_status
- id (string, required): the task id

##### Response Body Sample
```json
{
    "id": "2ec7df42-10c7-4dca-9dde-3d2e74019d8c",
    "name": "benchmark",
    "status": "in_progress",
    "duration": 5,
    "start_date": null,
    "end_date": null,
    "dependent_task_ids": [],
    "project_plan_id": "110a0886-aded-4c74-864e-a9af0df38ef2"
}
```

#### Create Schedule
This endpoint creates a schedule for all the given task ids. If a task has dependents, it will also create a schedule for each of those dependents.

The start and end dates are consecutive in such a way that it covers the duration of the task. For example, if Task A's duration is 5
and Task B's duration is 2:
- Task A:
  - start_date: today
  - end_date: 5 days from now
- Task B:
  - start_date: 5 days from now
  - end_date: 7 days from now

##### Path
**POST** /tasks/schedule

##### Request Body Sample
```json
[
    {
        "id": "91b0e214-fad0-4160-82ea-5e064232293c"
    },
    {
        "id": "eedde1f2-15ae-4337-bc96-0162415e8d02"
    },
    {...}
]

```
- id: the task id

##### Response Body Sample
```json
[
    {
        "id": "91b0e214-fad0-4160-82ea-5e064232293c",
        "duration": 2,
        "startDate": "2024-11-13T05:57:42.978+00:00",
        "endDate": "2024-11-15T05:57:42.978+00:00"
    },
    {
        "id": "e0f5abe5-bca4-451e-9472-03ea7e6cf20c",
        "duration": 4,
        "startDate": "2024-11-15T05:57:42.978+00:00",
        "endDate": "2024-11-19T05:57:42.978+00:00"
    }
]
```
