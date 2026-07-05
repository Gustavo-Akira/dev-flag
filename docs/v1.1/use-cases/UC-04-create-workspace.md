# UC-04 — Create Workspace

## Objective
Create an isolated workspace for a team.

## Roles
- Authenticated User

## Preconditions
- User authenticated

## Main Flow
1. The User request workspace creation.
2. The system displays the workspace creation form.
3. Fills name and slug.
4. System validates inputs.
5. System validates slug uniqueness.
6. System creates workspace.
7. System assigns creator as OWNER.
8. System informs the user that the workspace was created.

# Alternative Flows
## AF-01 - Invalid Input

At step 4:

1. The system detects invalid input.
2. The system returns the validation errors.
3. The workspace creation process is aborted.
## AF-02 - Slug Already Exists

At step 5:

1. The system detects that the provided slug is already in use.
2. The system rejects the request.
3. The system informs the user that the slug is unavailable.
## AF-03 - Workspace Creation Failure
 
At steps 6 or 7:

1. The system fails to create the workspace or assign the creator as OWNER.
2. The entire operation is rolled back.
3. No workspace without an OWNER is persisted.
4. The system informs the user that the workspace could not be created.


## Postconditions
- A new workspace exists.
- The creator is a member of the workspace.
- The creator has the OWNER role.


## Constraints
- Slug must be unique.
- Creator becomes OWNER automatically.
- Name cannot be null or empty
