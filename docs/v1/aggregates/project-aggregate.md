# Project Aggregate

## Goal

Manage project lifecycle and environments.

## Aggregate Boundary

```txt
Project
 └── Environment
```

## Responsibilities

- Project creation
- Default environment creation
- Project metadata updates

## Invariants

### INV-01
Project name must be unique inside workspace.

### INV-02
Project must always contain environments.

### INV-03
Default environments are automatically created.

## Default Environments

- dev
- staging
- prod

## Aggregate Root

Project

## Internal Entities

- Environment

## Relationships

Project references workspace by ID.

```txt
workspaceId
```

Feature flags reference environments externally.

## Notes

Environment is intentionally lightweight in V1.
