# UC-08 Create Environment

## Objective
User create an environment

## Roles
- OWNER
- ADMIN
- DEVELOPER

## Preconditions
- User has to be authenticated.
- User has to be one of the specified roles.
- The user belongs to the workspace.
- The project exists on the workspace.

## MAIN FLOW
1. User open the project section.
2. System display the projects of that workspace.
3. User open the environment section.
4. User request the environment creation.
5. System display the environment creation form.
6. User provides the required information:
    - Name
7. System validates inputs.
8. System create the environment
9. System inform the user that the environment was created.

## ALTERNATIVE FLOWS

### AF-01 - Invalid Input

At step 7:

1. The system detects invalid input.
2. The system returns the validation errors.
3. The environment creation process is aborted.


## Postconditions
- The environment is created in the workspace.

## Constraints
- The workspace have to exist.
