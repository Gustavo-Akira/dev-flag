# UC-09 Create Feature Flag

## Objective
User create a feature flag

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

## MAIN FLOW
1. User open the project section.
2. System display the projects of that workspace.
3. User open the environment section.
4. User open the feature flag section.
5. User request the feature flag creation.
6. System display the feature flag creation form.
7. User provides the required information:
    - Name
    - Key
    - Enabled
8. System validates inputs.
9. System create the flag.
10. System inform the user that the flag was created.

## ALTERNATIVE FLOWS

### AF-01 - Invalid Input

At step 8:

1. The system detects invalid input.
2. The system returns the validation errors.
3. The feature flag creation process is aborted.


## Postconditions
- The feature flag is created in the workspace.

## Constraints
- The workspace have to exist.
