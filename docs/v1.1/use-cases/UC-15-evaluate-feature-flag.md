# UC-15 Evaluate Feature Flag

## Objective
User evaluate a feature flag

## Roles
- OWNER
- ADMIN
- DEVELOPER

## Preconditions
- User has to be authenticated.
- User has to be one of the specified roles.
- The user belongs to the workspace.
- The project exists on the workspace.
- The environment exists on the project.
- The feature flag has to exist.

## MAIN FLOW
1. User open the project section.
2. System display the projects of that workspace.
3. User open the environment section.
4. User open the feature flag section.
5. User request the feature flag view.
6. System display the feature flag and its configurations.

## ALTERNATIVE FLOWS

### AF-01 - Feature Flag does not exist

At step 6:

1. The system not found the feature flag.
2. The system return not found feature flag.
3. The feature flag evaluate process is aborted.

## Postconditions

## Constraints
- The workspace have to exist.

