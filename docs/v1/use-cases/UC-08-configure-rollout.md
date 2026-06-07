# UC-08 — Configure Rollout

## Objective
Configure rollout percentage.

## Main Flow
1. User opens rollout settings.
2. Defines percentage.
3. System validates value.
4. System stores rollout config.
5. System stores audit log.

## Business Rules
- Value must be between 0 and 100.
- Rollout must be deterministic for same user.
