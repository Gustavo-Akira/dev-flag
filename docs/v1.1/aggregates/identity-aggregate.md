# Identity Aggregate

## Overview

The Identity Aggregate represents a registered user of the platform.

The aggregate is responsible for maintaining the user's identity, account status, and credential-related invariants.

Authentication orchestration is handled by the application layer, while the User Aggregate protects its own state and invariants.

---

## Aggregate Root

### User

The `User` entity is the Aggregate Root.

```text
User
```

The aggregate does not contain Workspace membership, Invitations, Sessions, or Authentication Tokens.

---

## User

### Attributes

* `id`
* `name`
* `email`
* `passwordHash`
* `status`
* `createdAt`
* `updatedAt`

### Responsibilities

* Represent a registered platform user.
* Maintain user identity information.
* Maintain account status.
* Change user profile information.
* Change stored credentials.
* Activate or disable the account.

### Behaviors

```text
register(
    name,
    email,
    passwordHash
)

changeName(
    newName
)

changeEmail(
    newEmail
)

changePassword(
    newPasswordHash
)

activate()

disable()
```

---

## Account Status

A User has an account status.

```text
ACTIVE

DISABLED
```

If email verification is introduced in a future version:

```text
PENDING_VERIFICATION
```

may be added without changing the aggregate boundary.

---

## Aggregate Invariants

### INV-01 - User must have a valid name

A User must have a non-empty valid name.

---

### INV-02 - User must have a valid email

A User must have a valid email address.

The email is represented by an `Email` Value Object.

---

### INV-03 - User email must be unique

Two platform users cannot share the same normalized email address.

This invariant requires coordination with the persistence layer because the User Aggregate cannot determine global uniqueness by itself.

The database must enforce this invariant with a unique constraint.

---

### INV-04 - Password must not be stored in plain text

The User Aggregate must never store a plain-text password.

Only a previously generated password hash may be stored.

---

### INV-05 - Disabled users cannot authenticate

A User with status `DISABLED` cannot successfully authenticate.

---

## Value Objects

### Email

Represents a normalized and validated email address.

#### Responsibilities

* Validate email format.
* Normalize the email address according to the platform strategy.
* Prevent invalid email values from entering the domain.

Example:

```text
Gustavo@Example.com

↓

gustavo@example.com
```

#### Constraints

* Must not be empty.
* Must have a valid format.
* Must respect the maximum supported length.

---

### PasswordHash

Represents an already hashed password.

#### Responsibilities

* Prevent plain-text passwords from being represented as stored credentials.
* Encapsulate the persisted password hash.

#### Constraints

* Must not be empty.
* Must contain only a previously generated password hash.
* Must never expose the hash through public API responses.

---

## Aggregate Operations

### Register User

```text
User.register(
    name,
    email,
    passwordHash
)
```

Validation:

1. Name is valid.
2. Email is valid.
3. Password hash is provided.

Result:

```text
User
├── name
├── email
├── passwordHash
└── status = ACTIVE
```

The application layer must verify email uniqueness before persistence.

The database must additionally enforce email uniqueness.

---

### Change Name

```text
user.changeName(
    newName
)
```

Validation:

1. New name is valid.
2. New name is not empty.

---

### Change Email

```text
user.changeEmail(
    newEmail
)
```

Validation:

1. New email is valid.
2. New email is different from the current email.
3. New email is not already registered.

The global uniqueness check is coordinated outside the aggregate.

---

### Change Password

```text
user.changePassword(
    newPasswordHash
)
```

Validation:

1. New password hash is valid.
2. Plain-text password is never stored in the aggregate.

---

### Disable User

```text
user.disable()
```

Result:

```text
User
└── status = DISABLED
```

A disabled User cannot authenticate.

---

### Activate User

```text
user.activate()
```

Result:

```text
User
└── status = ACTIVE
```

---

## Domain Events

The aggregate may publish the following domain events:

```text
UserRegistered

UserNameChanged

UserEmailChanged

UserPasswordChanged

UserActivated

UserDisabled
```

---

## Persistence Model

```text
users
├── id
├── name
├── email
├── password_hash
├── status
├── created_at
└── updated_at
```

Recommended constraints:

```text
PRIMARY KEY (id)

UNIQUE (email)

NOT NULL (name)

NOT NULL (email)

NOT NULL (password_hash)

NOT NULL (status)
```

---

## Aggregate Boundary

```text
┌─────────────────────────────────────┐
│          Identity Aggregate         │
│                                     │
│  ┌───────────────────────────────┐  │
│  │ User                          │  │
│  │ Aggregate Root                │  │
│  └───────────────────────────────┘  │
│                                     │
└─────────────────────────────────────┘
```

---

## External Aggregate References

The User Aggregate does not contain Workspace membership.

Workspace membership belongs to the Workspace Aggregate:

```text
WorkspaceMember.userId
    └── UserId
```

The Workspace Aggregate references the User only by identifier.

The User Aggregate must not directly contain or load:

```text
Workspace

WorkspaceMember

Invitation

Project

Environment

FeatureFlag
```

---

## Authentication Boundary

Authentication is not modeled as behavior that receives a plain-text password inside the User Aggregate.

The authentication flow is orchestrated by the application layer:

```text
AuthenticateUserUseCase
        │
        ├── Load User by Email
        │
        ├── Verify Account Status
        │
        ├── PasswordEncoder.matches(
        │       rawPassword,
        │       passwordHash
        │   )
        │
        └── Issue Authentication Token
```

The User Aggregate protects account state.

The application and security layers coordinate credential verification and token generation.
