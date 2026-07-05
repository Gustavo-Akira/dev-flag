# UC-11 Toggle Feature Flag

## Objective
User toggle a feature flag

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
5. User toggle the flag.
6. System change the feature flag to the opposite.
7. System inform the user that the flag was toggled.

## ALTERNATIVE FLOWS


## Postconditions
- The feature flag is updated in the workspace.

## Constraints
- The workspace have to exist.
