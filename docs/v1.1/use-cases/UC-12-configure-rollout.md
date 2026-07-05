# UC-12 Configure Rollout

## Objective
User configure a rollout to a feature flag

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
5. User request a rollout config.
6. System display the rollout form.
7. User provides the required information:
    - Percentage
8. System validate inputs
9. System informs that the rollout config was created.

## ALTERNATIVE FLOWS
### AF-01 - Invalid Input
At step 8:

1. The system detects invalid input.
2. The system returns the validation errors.
3. The rollout config process is aborted.
### AF-02 - Rollout already exists
At step 5:

1. The system detects that the rollout already exists.
2. The system informs that the rollout config already exists.
3. The rollout config process is aborted.

## Postconditions
- The rollout config is created for the flag.

## Constraints
- The workspace have to exist.
