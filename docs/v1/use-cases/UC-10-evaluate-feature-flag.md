# UC-10 — Evaluate Feature Flag

## Objective
Allow SDK to evaluate feature state.

## Request Example

```json
{
  "flagKey": "new-checkout",
  "userId": "123"
}
```

## Main Flow
1. SDK sends request.
2. System finds feature flag.
3. Validates enabled state.
4. Evaluates targeting.
5. Evaluates rollout.
6. Returns evaluation result.

## Evaluation Priority
1. Explicit targeting
2. Rollout percentage
3. Default state
