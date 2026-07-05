# Workspace Aggregate

## Overview

The Workspace Aggregate represents the tenant boundary of the platform.

A Workspace groups users, projects, environments, feature flags, and other workspace-scoped resources.

The aggregate is responsible for maintaining membership and invitation invariants.

---

## Aggregate Root

### Workspace

The `Workspace` entity is the Aggregate Root.

All modifications to workspace members and invitations must occur through the Workspace Aggregate Root.

```text
Workspace
├── WorkspaceMember
└── Invitation
```

---

## Workspace

### Attributes

* `id`
* `name`
* `slug`
* `members`
* `invitations`
* `createdAt`
* `updatedAt`

### Responsibilities

* Create a workspace.
* Assign the creator as OWNER.
* Manage workspace members.
* Create invitations.
* Accept invitations.
* Revoke invitations.
* Prevent duplicate memberships.
* Prevent duplicate active invitations.
* Ensure the workspace has an OWNER.

### Behaviors

```text
create(name, slug, creatorId)

inviteMember(email, role, invitedBy)

acceptInvitation(invitationToken, userId)

revokeInvitation(invitationId)

removeMember(memberId)

changeMemberRole(memberId, role)
```

---

## WorkspaceMember

`WorkspaceMember` represents the relationship between a platform User and a Workspace.

It is an Entity inside the Workspace Aggregate.

### Attributes

* `id`
* `userId`
* `role`
* `joinedAt`

### Roles

* `OWNER`
* `ADMIN`
* `DEVELOPER`
* `VIEWER`

### Responsibilities

* Represent workspace membership.
* Define the member authorization level.
* Track when the user joined the workspace.

### Constraints

* A user can appear only once as an active member of the same workspace.
* A member must reference an existing platform user.
* A member role must be valid.
* OWNER membership cannot be removed if it would leave the workspace without an OWNER.

---

## Invitation

`Invitation` represents a pending request for a person to join a Workspace.

It is an Entity inside the Workspace Aggregate.

### Attributes

* `id`
* `email`
* `role`
* `token`
* `status`
* `invitedBy`
* `createdAt`
* `expiresAt`
* `acceptedAt`

### Status

* `PENDING`
* `ACCEPTED`
* `EXPIRED`
* `REVOKED`
* `DELIVERY_FAILED`

### Responsibilities

* Represent a workspace invitation.
* Track invitation lifecycle.
* Validate expiration.
* Prevent reuse after acceptance.
* Associate the future member role with the invitation.

### Behaviors

```text
accept(userId)

revoke()

expire()

markDeliveryFailed()
```

### Constraints

* Only PENDING invitations can be accepted.
* Expired invitations cannot be accepted.
* Revoked invitations cannot be accepted.
* Accepted invitations cannot be reused.
* An invitation must have an expiration date.
* A workspace cannot have multiple active invitations for the same email address.

---

## Aggregate Invariants

### INV-01 - Workspace must have an OWNER

A Workspace must never exist without at least one OWNER.

---

### INV-02 - Creator becomes OWNER

When a Workspace is created, the creator must automatically become an OWNER member.

---

### INV-03 - Unique membership

A User cannot have multiple active memberships in the same Workspace.

---

### INV-04 - Unique active invitation

A Workspace cannot have multiple active invitations for the same email address.

---

### INV-05 - Existing members cannot be invited

A User who already belongs to the Workspace cannot receive a new active invitation for the same Workspace.

---

### INV-06 - Invitation lifecycle

Only PENDING and non-expired invitations can be accepted.

---

### INV-07 - Invitation acceptance creates membership

When a valid invitation is accepted:

1. The invitation becomes ACCEPTED.
2. A WorkspaceMember is created.
3. The member receives the role defined by the invitation.

These changes must be atomic.

---

### INV-08 - Last OWNER cannot be removed

An OWNER cannot be removed or downgraded if they are the last OWNER of the Workspace.

---

## Aggregate Operations

### Create Workspace

```text
Workspace.create(
    name,
    slug,
    creatorId
)
```

Result:

```text
Workspace
└── WorkspaceMember
    ├── userId = creatorId
    └── role = OWNER
```

---

### Invite Member

```text
workspace.inviteMember(
    email,
    role,
    invitedBy
)
```

Validation:

1. Actor has permission to invite.
2. Email is valid.
3. User is not already a member.
4. No active invitation exists for the email.

Result:

```text
Invitation
├── status = PENDING
├── role = selectedRole
└── expiresAt = configuredExpiration
```

---

### Accept Invitation

```text
workspace.acceptInvitation(
    invitationToken,
    userId
)
```

Validation:

1. Invitation exists.
2. Invitation is PENDING.
3. Invitation is not expired.
4. User is not already a member.

Result:

```text
Invitation
└── status = ACCEPTED

WorkspaceMember
├── userId
├── role = invitation.role
└── joinedAt
```

---

### Remove Member

```text
workspace.removeMember(memberId)
```

Validation:

1. Member exists.
2. Actor has permission.
3. Member removal does not leave the Workspace without an OWNER.

---

### Change Member Role

```text
workspace.changeMemberRole(
    memberId,
    newRole
)
```

Validation:

1. Member exists.
2. Actor has permission.
3. New role is valid.
4. Role change does not leave the Workspace without an OWNER.

---

## Domain Events

The aggregate may publish the following domain events:

```text
WorkspaceCreated

WorkspaceMemberInvited

WorkspaceInvitationAccepted

WorkspaceInvitationRevoked

WorkspaceMemberJoined

WorkspaceMemberRemoved

WorkspaceMemberRoleChanged
```

---

## Persistence Model

```text
workspaces
├── id
├── name
├── slug
├── created_at
└── updated_at

workspace_members
├── id
├── workspace_id
├── user_id
├── role
└── joined_at

workspace_invitations
├── id
├── workspace_id
├── email
├── role
├── token
├── status
├── invited_by
├── created_at
├── expires_at
└── accepted_at
```

---

## Aggregate Boundary

```text
┌─────────────────────────────────────┐
│         Workspace Aggregate         │
│                                     │
│  ┌───────────────────────────────┐  │
│  │ Workspace                     │  │
│  │ Aggregate Root                │  │
│  └───────────────┬───────────────┘  │
│                  │                  │
│         ┌────────┴────────┐         │
│         │                 │         │
│         ▼                 ▼         │
│  WorkspaceMember      Invitation    │
│     Entity              Entity      │
│                                     │
└─────────────────────────────────────┘

External References

User
  ▲
  │ userId
  │
WorkspaceMember
```

---

## External Aggregate References

The Workspace Aggregate references external aggregates only by identifier.

```text
WorkspaceMember.userId
    └── UserId

Invitation.invitedBy
    └── UserId
```

The Workspace Aggregate must not directly contain or load the `User` Aggregate.
