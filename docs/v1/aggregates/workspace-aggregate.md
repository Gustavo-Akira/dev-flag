# Workspace Aggregate

## Goal

Manage workspace lifecycle, members and permissions.

## Aggregate Boundary

```txt
Workspace
 └── WorkspaceMember
```

## Responsibilities

- Workspace creation
- Member invitation
- Permission validation
- Role assignment

## Invariants

### INV-01
Same user cannot exist twice in same workspace.

### INV-02
Workspace must always have at least one OWNER.

### INV-03
Only OWNER or ADMIN can invite members.

## Aggregate Root

Workspace

## Internal Entities

- WorkspaceMember

## Relationships

Workspace references projects by ID only.

```txt
projectId
```

## Notes

Workspace is the tenant boundary of the platform.
