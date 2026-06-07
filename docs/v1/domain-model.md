# Feature Flag Platform — Domain Model (V1)

## Goal

Define the domain model for V1 of the Feature Flag Platform.

The objective is to keep the model pragmatic, production-like and easy to evolve.

---

# Domain Hierarchy

```txt
Workspace
 └── Project
      └── Environment
           └── Feature Flag
```

---

# Entities

## Workspace

Represents an isolated tenant boundary.

A workspace groups projects, users and permissions.

### Attributes

| Attribute | Type    | Required |
| --------- | ------- | -------- |
| id        | UUID    | Yes      |
| name      | String  | Yes      |
| slug      | String  | Yes      |
| createdAt | Instant | Yes      |
| updatedAt | Instant | Yes      |

### Rules

* Slug must be unique
* Creator becomes OWNER automatically
* Workspace isolates all data

---

## WorkspaceMember

Represents a user inside a workspace.

### Attributes

| Attribute   | Type          |
| ----------- | ------------- |
| id          | UUID          |
| workspaceId | UUID          |
| userId      | UUID          |
| role        | WorkspaceRole |
| createdAt   | Instant       |

### Rules

* One role per member
* Same user cannot exist twice in same workspace

---

## Project

Represents an application or service.

Examples:

* checkout-api
* checkout-web
* mobile-app

### Attributes

| Attribute   | Type    |
| ----------- | ------- |
| id          | UUID    |
| workspaceId | UUID    |
| name        | String  |
| description | String  |
| createdAt   | Instant |
| updatedAt   | Instant |

### Rules

* Project name must be unique inside workspace
* Default environments are auto-created

---

## Environment

Represents runtime isolation.

### Supported Values (V1)

* dev
* staging
* prod

### Attributes

| Attribute | Type            |
| --------- | --------------- |
| id        | UUID            |
| projectId | UUID            |
| name      | EnvironmentType |
| createdAt | Instant         |

### Rules

* Created automatically
* Cannot be deleted in V1
* Custom environments out of scope

---

## FeatureFlag

Represents a runtime feature toggle.

### Attributes

| Attribute     | Type    |
| ------------- | ------- |
| id            | UUID    |
| environmentId | UUID    |
| name          | String  |
| key           | String  |
| description   | String  |
| enabled       | Boolean |
| createdAt     | Instant |
| updatedAt     | Instant |

### Rules

* Key must be unique within environment
* Key is immutable
* Flag starts disabled by default

---

## RolloutConfiguration

Defines percentage rollout behavior.

### Attributes

| Attribute     | Type    |
| ------------- | ------- |
| id            | UUID    |
| featureFlagId | UUID    |
| percentage    | Integer |

### Rules

* Value between 0 and 100
* Deterministic evaluation for same user

### Example

50%

Means:

```txt
Half of users receive feature.
Half do not.
```

---

## TargetingRule

Defines explicit user targeting.

### Scope (V1)

Simple allowlist only.

### Attributes

| Attribute     | Type   |
| ------------- | ------ |
| id            | UUID   |
| featureFlagId | UUID   |
| userId        | String |

### Example

```txt
[
  "user-1",
  "user-2",
  "user-3"
]
```

### Rules

* Explicit targeting overrides rollout

---

## AuditLog

Stores domain changes.

### Attributes

| Attribute   | Type        |
| ----------- | ----------- |
| id          | UUID        |
| workspaceId | UUID        |
| actorId     | UUID        |
| entityType  | String      |
| entityId    | UUID        |
| action      | AuditAction |
| oldValue    | JSON        |
| newValue    | JSON        |
| createdAt   | Instant     |

### Example Actions

* FEATURE_FLAG_CREATED
* FEATURE_FLAG_UPDATED
* FEATURE_FLAG_ENABLED
* FEATURE_FLAG_DISABLED
* ROLLOUT_UPDATED

---

# Value Objects

## WorkspaceSlug

Represents unique workspace identifier.

### Example

```txt
akira-tech
```

Rules:

* lowercase
* kebab-case
* unique

---

## FeatureKey

Represents immutable flag key.

### Example

```txt
new-checkout
```

Rules:

* unique inside environment
* immutable

---

## RolloutPercentage

Represents rollout percentage.

### Rules

```txt
0 <= percentage <= 100
```

---

# Enums

## WorkspaceRole

```txt
OWNER
ADMIN
DEVELOPER
VIEWER
```

---

## EnvironmentType

```txt
DEV
STAGING
PROD
```

---

## AuditAction

```txt
FEATURE_FLAG_CREATED
FEATURE_FLAG_UPDATED
FEATURE_FLAG_ENABLED
FEATURE_FLAG_DISABLED
ROLLOUT_UPDATED
TARGETING_UPDATED
```

---

# Relationships

```txt
Workspace (1)
 ├── WorkspaceMember (N)
 └── Project (N)

Project (1)
 └── Environment (N)

Environment (1)
 └── FeatureFlag (N)

FeatureFlag (1)
 ├── RolloutConfiguration (1)
 ├── TargetingRule (N)
 └── AuditLog (N)
```

---

# Evaluation Priority

Feature evaluation order:

```txt
1. Feature enabled?
2. Explicit targeting
3. Rollout percentage
4. Default false
```

This rule must be deterministic.
