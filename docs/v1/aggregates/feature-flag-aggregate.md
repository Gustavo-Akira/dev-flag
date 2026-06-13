# Feature Flag Aggregate

## Goal

Manage feature behavior and evaluation rules.

## Aggregate Boundary

```txt
FeatureFlag
 ├── RolloutConfiguration
 └── TargetingRule
```

## Responsibilities

- Create feature flag
- Enable/disable feature
- Configure rollout
- Configure targeting
- Evaluate feature state

## Invariants

### INV-01
Feature key is immutable.

### INV-02
Feature key must be unique inside environment.

### INV-03
Rollout percentage must be between 0 and 100.

### INV-04
Explicit targeting overrides rollout.

### INV-05
Same targeted user cannot exist twice.

## Evaluation Priority

```txt
1. Feature enabled?
2. Explicit targeting
3. Rollout percentage
4. Default false
```

## Aggregate Root

FeatureFlag

## Internal Entities

- RolloutConfiguration
- TargetingRule

## Relationships

Feature flag references environment by ID.

```txt
environmentId
```

Audit log is external to aggregate.

## Notes

This is the core aggregate of the platform.
