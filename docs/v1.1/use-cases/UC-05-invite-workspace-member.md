# UC-05 Invite Workspace Member

## Objective
Invite users to collaborate in a workspace.

## Roles
- OWNER
- ADMIN

## Preconditions
- User has to be authenticated.
- The user belongs to the workspace. 
- The user has permission to invite members.

## MAIN FLOW
1. User opens workspace members page.
2. Adds member email.
3. Selects role.
4. System validate the request.
5. System sends invite.
6. System informs user that the invitation was created with success

## ALTERNATIVE FLOWS

### AF-01 - Invalid Input
At step 4:
1. The system detects invalid input.
2. The system returns the validation errors.
3. The invitation process is aborted.

### AF-02 - Invitation already exists
At step 5:
1. The system detects an active invitation for the same email address.
2. The system rejects the request.
3. The system informs the user that an active invitation already exists.

### AF-03 - User is already a workspace member
At step 5:
1. The system detects that the user already belongs to the workspace.
2. The system rejects the invitation.
3. The system informs the user accordingly.

### AF-04 - Email delivery failed
At step 6:
1. The system creates the invitation with status PENDING.
2. The system attempts to send the email.
3. If all retries fail, the invitation status is updated to DELIVERY_FAILED.
4. The system records the failure for future retry or manual intervention.

## Postconditions
- A workspace invitation exists.
- The invitation has a status.
- An email delivery has been attempted.

## Constraints
- Only OWNER or ADMIN can invite.
- A workspace cannot have multiple active invitations for the same email address. 
- Existing workspace members cannot be invited again.
- Invitations expire after a configurable period.
- Invitation emails should be sent asynchronously.
