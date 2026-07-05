# UC-14 Fetch Feature Flags

## Objective
User fetch  feature flags

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
5. System display the feature flags on that environment.



## ALTERNATIVE FLOWS

### AF-01 - Not Found Flags

At step 5:

1. The system return no flags.
2. The system inform the user tha doesnt have any flag in this environment.


## Postconditions

## Constraints
- The workspace have to exist.

