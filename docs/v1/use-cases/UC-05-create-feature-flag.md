# UC-05 — Create Feature Flag

## Objective
Create a feature flag inside an environment.

## Main Flow
1. User selects project and environment.
2. Clicks create feature flag.
3. Fills:
   - name
   - key
   - description
4. System validates key uniqueness.
5. System creates feature flag.
6. System stores audit log.

## Business Rules
- Key is immutable.
- Flag starts disabled by default.
