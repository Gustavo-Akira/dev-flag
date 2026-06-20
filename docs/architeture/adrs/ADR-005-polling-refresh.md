# ADR-005 Polling Refresh

## Status
Accepted

## Context

Initial architecture decision for V1.

## Decision

SDK refreshes configuration through configurable polling.

## Consequences

- Simple implementation; evolve to ETag/SSE later.
- Decision may be revisited in future versions if requirements change.
