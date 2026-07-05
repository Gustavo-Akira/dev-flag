# UC-07 Create Project

## Objective
User create a project

## Roles
- OWNER
- ADMIN
- DEVELOPER

## Preconditions
- User has to be authenticated.
- User has to be one of the specified roles.
- The user belongs to the workspace.

## MAIN FLOW
1. User open the project section.
2. System display the projects of that workspace.
3. User request the project creation for that workspace.
4. System display the project creation form.
5. User provides the required information:
    - Name
    - Description
6. System validates inputs.
7. System create the project
8. System inform the user that the project was created.

## ALTERNATIVE FLOWS

### AF-01 - Invalid Input

At step 6:

1. The system detects invalid input.
2. The system returns the validation errors.
3. The project creation process is aborted.


## Postconditions
- The project is created in the workspace.

## Constraints
- The workspace have to exist.
