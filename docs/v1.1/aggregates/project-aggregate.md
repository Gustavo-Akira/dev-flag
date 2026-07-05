# Project Aggregate

## Overview

The Project Aggregate represents an application or system managed inside a Workspace.

A Project groups Environments that isolate feature flag configurations across different deployment contexts.

Examples:

* Development
* Staging
* Production

The aggregate is responsible for maintaining Project and Environment invariants.

---

## Aggregate Root

### Project

The `Project` entity is the Aggregate Root.

All modifications to Environments must occur through the Project Aggregate Root.

```text
Project
└── Environment
```

---

## Project

### Attributes

* `id`
* `workspaceId`
* `name`
* `description`
* `environments`
* `createdAt`
* `updatedAt`

### Responsibilities

* Create a project.
* Associate the project with a workspace.
* Manage project environments.
* Add environments.
* Rename environments.
* Remove environments.
* Prevent duplicate environment names within the same project.
* Ensure project environment invariants.

### Behaviors

```text
create(
    workspaceId,
    name,
    description,
    initialEnvironment
)

addEnvironment(
    name
)

renameEnvironment(
    environmentId,
    newName
)

removeEnvironment(
    environmentId
)

findEnvironment(
    environmentId
)
```

---

## Environment

`Environment` represents an isolated deployment context inside a Project.

It is an Entity inside the Project Aggregate.

Examples:

```text
Development

Staging

Production
```

### Attributes

* `id`
* `name`
* `createdAt`
* `updatedAt`

### Responsibilities

* Represent an isolated deployment context.
* Maintain a unique identity inside the Project.
* Provide the boundary used by Feature Flags for environment-specific configuration.

### Constraints

* An Environment must belong to exactly one Project.
* Environment names must be unique within the same Project.
* An Environment cannot exist independently from a Project.
* Environment names must not be empty.

---

## Aggregate Invariants

### INV-01 - Project belongs to a Workspace

A Project must belong to exactly one Workspace.

The Workspace is referenced by identifier:

```text
Project.workspaceId
    └── WorkspaceId
```

The Project Aggregate must not directly contain or load the Workspace Aggregate.

---

### INV-02 - Environment belongs to a Project

An Environment must belong to exactly one Project.

An Environment cannot exist independently from a Project.

---

### INV-03 - Unique Environment Name

Environment names must be unique within the same Project.

Valid:

```text
Project A
├── Development
├── Staging
└── Production
```

Invalid:

```text
Project A
├── Production
└── Production
```

---

### INV-04 - Project must have an Environment

A Project must contain at least one Environment.

When a Project is created, an initial Environment must also be created.

These changes must be atomic.

---

### INV-05 - Last Environment cannot be removed

An Environment cannot be removed if it is the last remaining Environment of the Project.

---

## Aggregate Operations

### Create Project

```text
Project.create(
    workspaceId,
    name,
    description,
    initialEnvironmentName
)
```

Validation:

1. Workspace identifier is provided.
2. Project name is valid.
3. Initial Environment name is valid.

Result:

```text
Project
└── Environment
    └── name = initialEnvironmentName
```

Example:

```text
Checkout Service
└── Development
```

---

### Add Environment

```text
project.addEnvironment(
    name
)
```

Validation:

1. Environment name is valid.
2. No Environment with the same name exists in the Project.

Result:

```text
Project
├── Development
└── Production
```

---

### Rename Environment

```text
project.renameEnvironment(
    environmentId,
    newName
)
```

Validation:

1. Environment exists.
2. New name is valid.
3. No other Environment uses the same name.

---

### Remove Environment

```text
project.removeEnvironment(
    environmentId
)
```

Validation:

1. Environment exists.
2. Environment is not the last remaining Environment.

Result:

```text
Before

Project
├── Development
├── Staging
└── Production

After

Project
├── Development
└── Production
```

---

## Domain Events

The aggregate may publish the following domain events:

```text
ProjectCreated

EnvironmentAdded

EnvironmentRenamed

EnvironmentRemoved
```

---

## Persistence Model

```text
projects
├── id
├── workspace_id
├── name
├── description
├── created_at
└── updated_at

environments
├── id
├── project_id
├── name
├── created_at
└── updated_at
```

---

## Aggregate Boundary

```text
┌─────────────────────────────────────┐
│          Project Aggregate          │
│                                     │
│  ┌───────────────────────────────┐  │
│  │ Project                       │  │
│  │ Aggregate Root                │  │
│  └───────────────┬───────────────┘  │
│                  │                  │
│                  ▼                  │
│            Environment              │
│               Entity                │
│                                     │
└─────────────────────────────────────┘

External Reference

Workspace
   ▲
   │ workspaceId
   │
Project
```

---

## External Aggregate References

The Project Aggregate references external aggregates only by identifier.

```text
Project.workspaceId
    └── WorkspaceId
```

The Project Aggregate must not directly contain or load the `Workspace` Aggregate.

Feature Flags reference an Environment by identifier:

```text
FeatureFlag.environmentId
    └── EnvironmentId
```

The Project Aggregate must not directly contain or load the `FeatureFlag` Aggregate.
