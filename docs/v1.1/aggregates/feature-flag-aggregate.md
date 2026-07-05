# Feature Flag Aggregate

## Overview

The Feature Flag Aggregate represents a configurable feature toggle associated with a specific Environment.

The aggregate is responsible for maintaining the lifecycle, rollout configuration, explicit targets, and evaluation-related invariants of a Feature Flag.

---

## Aggregate Root

### FeatureFlag

The `FeatureFlag` entity is the Aggregate Root.

All modifications to rollout configuration and explicit targets must occur through the FeatureFlag Aggregate Root.

```text
FeatureFlag
├── RolloutConfiguration
└── Target*
```

---

## FeatureFlag

### Attributes

* `id`
* `environmentId`
* `name`
* `key`
* `description`
* `enabled`
* `archived`
* `defaultValue`
* `rolloutConfiguration`
* `targets`
* `createdAt`
* `updatedAt`

### Responsibilities

* Create a Feature Flag.
* Enable a Feature Flag.
* Disable a Feature Flag.
* Archive a Feature Flag.
* Restore an archived Feature Flag.
* Update Feature Flag metadata.
* Configure percentage rollout.
* Remove percentage rollout.
* Add explicit targets.
* Remove explicit targets.
* Maintain evaluation configuration invariants.

### Behaviors

```text
create(
    environmentId,
    name,
    key,
    description,
    defaultValue
)

enable()

disable()

archive()

restore()

updateMetadata(
    name,
    description
)

configureRollout(
    percentage
)

removeRollout()

addTarget(
    targetType,
    targetKey
)

removeTarget(
    targetId
)
```

---

## RolloutConfiguration

`RolloutConfiguration` represents percentage-based distribution for a Feature Flag.

It is part of the Feature Flag Aggregate.

### Attributes

* `percentage`

### Responsibilities

* Represent the percentage of evaluation contexts included in the rollout.
* Validate percentage boundaries.
* Provide rollout configuration to the SDK runtime.

### Constraints

* Percentage must be between `0` and `100`.
* A Feature Flag can have at most one active RolloutConfiguration.
* Rollout configuration belongs to exactly one Feature Flag.
* Rollout configuration cannot exist independently from a Feature Flag.

---

## Target

`Target` represents an explicit evaluation target for a Feature Flag.

It is an Entity inside the Feature Flag Aggregate.

A Target does not reference a platform User.

The target belongs to the consuming application domain.

Examples:

```text
USER         -> user-123

TENANT       -> tenant-acme

ORGANIZATION -> org-001

DEVICE       -> device-xyz
```

### Attributes

* `id`
* `type`
* `key`
* `createdAt`

### Target Types

* `USER`
* `TENANT`
* `ORGANIZATION`
* `DEVICE`

### Responsibilities

* Represent an explicit target for a Feature Flag.
* Associate an external identifier with a target type.
* Support direct targeting before percentage rollout evaluation.

### Constraints

* Target key must not be empty.
* Target type must be valid.
* A Target belongs to exactly one Feature Flag.
* A Target cannot exist independently from a Feature Flag.
* A Feature Flag cannot contain duplicate targets with the same type and key.
* Target keys do not reference platform User identifiers.

---

## Aggregate Invariants

### INV-01 - Feature Flag belongs to an Environment

A Feature Flag must belong to exactly one Environment.

The Environment is referenced by identifier:

```text
FeatureFlag.environmentId
    └── EnvironmentId
```

The Feature Flag Aggregate must not directly contain or load the Project Aggregate.

---

### INV-02 - Feature Flag Key

A Feature Flag must have a valid non-empty key.

Example:

```text
new-checkout
```

---

### INV-03 - Unique Feature Flag Key

A Feature Flag key must be unique within the same Environment.

Valid:

```text
Development
├── new-checkout
└── recommendations-v2

Production
└── new-checkout
```

The same key may exist in different Environments.

Global uniqueness requires coordination with the persistence layer.

The database must enforce uniqueness using:

```text
UNIQUE (environment_id, key)
```

---

### INV-04 - Rollout Percentage Range

Rollout percentage must be between:

```text
0 <= percentage <= 100
```

---

### INV-05 - Single Rollout Configuration

A Feature Flag can have at most one active RolloutConfiguration.

---

### INV-06 - Unique Target

A Feature Flag cannot contain duplicate targets with the same:

```text
type + key
```

Valid:

```text
USER   -> user-123

TENANT -> user-123
```

Invalid:

```text
USER -> user-123

USER -> user-123
```

---

### INV-07 - Archived Feature Flag

An archived Feature Flag cannot be modified until restored.

Operations rejected while archived include:

* Enable
* Disable
* Update metadata
* Configure rollout
* Add target
* Remove target

---

### INV-08 - Target is external

A Target must not reference the platform User Aggregate.

For example:

```text
users.id
```

must not be used as a foreign key by a Target.

Target identifiers belong to the consuming application.

---

## Aggregate Operations

### Create Feature Flag

```text
FeatureFlag.create(
    environmentId,
    name,
    key,
    description,
    defaultValue
)
```

Validation:

1. Environment identifier is provided.
2. Name is valid.
3. Key is valid.
4. Default value is provided.
5. Key is unique within the Environment.

Result:

```text
FeatureFlag
├── enabled = false
├── archived = false
├── rolloutConfiguration = none
└── targets = empty
```

---

### Enable Feature Flag

```text
featureFlag.enable()
```

Validation:

1. Feature Flag is not archived.

Result:

```text
enabled = true
```

---

### Disable Feature Flag

```text
featureFlag.disable()
```

Validation:

1. Feature Flag is not archived.

Result:

```text
enabled = false
```

---

### Archive Feature Flag

```text
featureFlag.archive()
```

Result:

```text
archived = true
```

An archived Feature Flag remains persisted for historical and audit purposes.

---

### Restore Feature Flag

```text
featureFlag.restore()
```

Result:

```text
archived = false
```

---

### Configure Rollout

```text
featureFlag.configureRollout(
    percentage
)
```

Validation:

1. Feature Flag is not archived.
2. Percentage is between `0` and `100`.

Result:

```text
RolloutConfiguration
└── percentage
```

If a rollout configuration already exists, it is replaced or updated according to the aggregate implementation strategy.

---

### Remove Rollout

```text
featureFlag.removeRollout()
```

Validation:

1. Feature Flag is not archived.

Result:

```text
rolloutConfiguration = none
```

---

### Add Target

```text
featureFlag.addTarget(
    targetType,
    targetKey
)
```

Validation:

1. Feature Flag is not archived.
2. Target type is valid.
3. Target key is valid.
4. No target with the same type and key already exists.

Result:

```text
FeatureFlag
└── Target
    ├── type
    └── key
```

---

### Remove Target

```text
featureFlag.removeTarget(
    targetId
)
```

Validation:

1. Feature Flag is not archived.
2. Target exists.

Result:

```text
Target removed from FeatureFlag
```

---

## Evaluation Semantics

The aggregate stores configuration used by the evaluation runtime.

A possible V1 evaluation order is:

```text
1. Archived?
   └── return default behavior

2. Enabled?
   └── false -> return default behavior

3. Explicit Target matches?
   └── yes -> return enabled result

4. Rollout configured?
   └── yes -> evaluate deterministic percentage

5. Otherwise
   └── return defaultValue
```

The exact evaluation algorithm should be documented in a dedicated ADR because evaluation precedence is a platform-level architectural decision.

---

## Domain Events

The aggregate may publish the following domain events:

```text
FeatureFlagCreated

FeatureFlagEnabled

FeatureFlagDisabled

FeatureFlagArchived

FeatureFlagRestored

FeatureFlagMetadataUpdated

RolloutConfigured

RolloutRemoved

TargetAdded

TargetRemoved
```

---

## Persistence Model

```text
feature_flags
├── id
├── environment_id
├── name
├── key
├── description
├── enabled
├── archived
├── default_value
├── created_at
└── updated_at

rollout_configurations
├── id
├── feature_flag_id
└── percentage

feature_flag_targets
├── id
├── feature_flag_id
├── target_type
├── target_key
└── created_at
```

Recommended constraints:

```text
feature_flags

UNIQUE (
    environment_id,
    key
)
```

```text
rollout_configurations

UNIQUE (
    feature_flag_id
)
```

```text
feature_flag_targets

UNIQUE (
    feature_flag_id,
    target_type,
    target_key
)
```

```text
rollout_configurations

CHECK (
    percentage >= 0
    AND
    percentage <= 100
)
```

---

## Aggregate Boundary

```text
┌─────────────────────────────────────────┐
│       Feature Flag Aggregate            │
│                                         │
│  ┌───────────────────────────────────┐  │
│  │ FeatureFlag                       │  │
│  │ Aggregate Root                    │  │
│  └─────────────────┬─────────────────┘  │
│                    │                    │
│          ┌─────────┴─────────┐          │
│          │                   │          │
│          ▼                   ▼          │
│ RolloutConfiguration      Target*       │
│    Value Object            Entity       │
│                                         │
└─────────────────────────────────────────┘

External Reference

Environment
    ▲
    │ environmentId
    │
FeatureFlag
```

---

## External Aggregate References

The Feature Flag Aggregate references Environment only by identifier.

```text
FeatureFlag.environmentId
    └── EnvironmentId
```

The Feature Flag Aggregate must not directly contain or load:

```text
Project

Environment

Workspace

User
```

Targets represent identifiers from consuming applications:

```text
Target
├── type = USER
└── key = user-123
```

This identifier has no foreign key relationship with:

```text
users.id
```

---

## Future Evolution

The V1 uses explicit Targets.

Future versions may introduce:

```text
FeatureFlag
├── Targets
├── Rules
├── Segments
└── RolloutConfiguration
```

Possible rule examples:

```text
country == BR

plan == PREMIUM

tenant == acme
```

These capabilities should be introduced without changing the meaning of existing explicit Targets.
