# UC-01 — Create Workspace

## Objective
Create an isolated workspace for a team.

## Actors
- Authenticated User

## Preconditions
- User authenticated

## Main Flow
1. User accesses workspace creation page.
2. Fills name and slug.
3. System validates slug uniqueness.
4. System creates workspace.
5. System assigns creator as OWNER.
6. Success response.

## Business Rules
- Slug must be unique.
- Creator becomes OWNER automatically.
