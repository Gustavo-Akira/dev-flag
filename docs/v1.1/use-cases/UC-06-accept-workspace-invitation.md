# UC-06 Invite Workspace Member

## Objective
User accepts invite to a workspace

## Roles
- User

## Preconditions
- User has to be authenticated.
- The user not belongs to the workspace.

## MAIN FLOW
1. User open the link sent.
2. System check if the invitation exists and is PENDING.
3. System display accept form.
4. User accept the invitation.
5. System update the relation with the relation specified on the invitation.

## ALTERNATIVE FLOWS

### AF-01 - Invitation not exist
At step 2:
1. The system detects that the invitation does not exist.
2. The system informs the user that the invitation does not exist.

### AF-02 - Invitation already accepted
At step 2:
1. The system detects that invitation are in a final status.
2. The system rejects the request.
3. The system informs the user that the invitation already finished.

### AF-03 - User is not on the system
At step 2:
1. The system detects that the user are not in the system.
2. The system open the UC-01 (register user).
3. The system return to step 3.


### AF-03 - User is not authenticated
At step 2:
1. The system detects that the user is not authenticated.
2. The system open the UC-02 (authenticate user).
3. The system return to step 3.

## Postconditions
- A workspace invitation exists.
- The invitation is ACCEPTED or REJECTED.

## Constraints
- The workspace have to exist.
- Invitation cannot be accepted or rejected after a final status.
