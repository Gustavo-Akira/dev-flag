# UC-03 Get Current User

## Objective
Get information of the authenticated user on the system.

## Roles
- OWNER
- ADMIN
- DEVELOPER
- VIEWER

## Preconditions
- The user is authenticated.

## MAIN FLOW
1. The user enter in the profile.
2. The system gets the user information.
3. The system authenticate the user.

## ALTERNATIVE FLOWS

### AF01 - User not authenticated
At step 2:
1. The system detects that the user is not authenticated/expired.
2. The system reject the request.
3. The system redirect to the login page and informs that the user must be authenticated.

## Post Conditions
- The user see its information.

## Constraints
