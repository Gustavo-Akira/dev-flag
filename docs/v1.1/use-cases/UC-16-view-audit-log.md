# UC-16 — View Audit Log

## Objective

View the history of auditable actions performed within a workspace.

## Primary Actor

* OWNER
* ADMIN

## Preconditions

* The user is authenticated.
* The user belongs to the workspace.
* The user has permission to view audit logs.

## Main Flow

1. The user requests the workspace audit log.
2. The system validates that the user has permission to view audit logs.
3. The system retrieves audit log entries associated with the workspace.
4. The system applies the requested filters and pagination.
5. The system returns the audit log entries.
6. The user views the history of actions performed within the workspace.

## Alternative Flows

### AF-01 - Unauthorized Access

At step 2:

1. The system detects that the user does not have permission to view audit logs.
2. The system rejects the request.
3. The system informs the user that access is not allowed.

### AF-02 - No Audit Logs Found

At step 3:

1. The system finds no audit log entries for the workspace.
2. The system returns an empty result.
3. The use case completes successfully.

### AF-03 - Invalid Filters

At step 4:

1. The system detects invalid filter parameters.
2. The system rejects the request.
3. The system returns the validation errors.

### AF-04 - Invalid Pagination

At step 4:

1. The system detects invalid pagination parameters.
2. The system rejects the request.
3. The system returns the validation errors.

## Postconditions

* No audit log entry is modified.
* No audit log entry is deleted.
* The user receives only audit log entries belonging to the requested workspace.

## Constraints

* Only authorized workspace members can view audit logs.
* Audit logs must be isolated by workspace.
* Audit log entries are immutable.
* Audit log results must be paginated.
* Audit log entries must be ordered by creation date, with the most recent entries returned first by default.
* Filters may include:

    * Actor
    * Action
    * Resource type
    * Resource identifier
    * Date range
* Audit log access must not expose sensitive values such as:

    * Passwords
    * Password hashes
    * Authentication tokens
    * SDK keys
    * Invitation tokens
* Changes should contain only auditable field differences rather than complete resource snapshots.
