# UC-02 Authenticate User

## Objective
Authenticate user on the system.

## Roles
- VISITOR

## Preconditions
- The visitor is not authenticated.
- The email address is already registered.

## MAIN FLOW
1. The visitor requests user authentication.
2. The system displays the authentication form.
3. The visitor provides the required information:
   - Email
   - Password
4. The system validates the provided information.
5. The system gets the user by the email.
6. The system compares the credentials passed.
7. The system authenticate the user.

## ALTERNATIVE FLOWS

### AF01 - Email is not registered
At step 5:
1. The system detects that the email is not registered.
2. The system rejects authentication.
3. The system informs the visitor that the email not exists on the database.

### AF02 - Invalid Input
At step 4:
1. The system detects invalid input.
2. The system return the validations errors.
3. The registration process is aborted


### AF03 - Password doesnt match
At step 6:
1. The system determines that the credentials are invalid.
2. The system rejects authentication.
3. The system informs the visitor that the email or the password is wrong.

## Post Conditions
- The user is authenticated.
- An authenticated session (or access token) is established. 
- The user can access authorized resources.

## Constraints
- Authentication must be performed using the registered email address and password. 
- Passwords must never be stored or compared in plain text. 
- Authentication failures must not reveal whether the email address exists. 
- Authentication attempts may be audited.