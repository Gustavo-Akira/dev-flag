# UC-01 Register User

## Objective
Register user on the system.

## Roles
- VISITOR

## Preconditions
- The visitor is not authenticated.
- The email address is not already registered.

## MAIN FLOW
1. The visitor requests user registration.
2. The system displays the registration form.
3. The visitor provides the required information:
   - Full name
   - Email
   - Password
4. The system validates the provided information. 
5. The system verifies that the email address is unique. 
6. The system hashes the password. 
7. The system creates a new user account. 
8. The system authenticates the newly created user. 
9. The system returns the authenticated user

## ALTERNATIVE FLOWS

### AF01 - Email already registered
At step 5:
1. The system detects that the email is already registered.
2. The system rejects registration.
3. The system informs the visitor that the email already exists on the database.

### AF02 - Invalid Input
At step 4:
1. The system detects invalid input.
2. The system return the validations errors.
3. The registration process is aborted


## Post Conditions
- A new user is registered.
- The user is authenticated.
- The user does not belong to any workspace.
- The user can create a new workspace or accept a workspace invitation.

## Constraints

- Email have to be unique.
- Password must be stored as hashes.
- A newly registered user is not automatically assigned to any workspace. 
- A user may belong to zero or more workspaces.