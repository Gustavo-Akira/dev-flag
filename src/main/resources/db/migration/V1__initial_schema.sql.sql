-- ============================================================================
-- Feature Flag Platform
-- Migration: V1__initial_schema.sql
--
-- Description:
--   Creates the initial database schema for V1.
--
-- Author:
--   Gustavo Akira
--
-- ============================================================================


-- ============================================================================
-- TABLE: users
--
-- Represents users of the system.
--
-- A user can be on N workspaces relation table (workspace_member)
--
-- ============================================================================

CREATE TABLE users
(
    id            UUID PRIMARY KEY,
    name          VARCHAR(150) NOT NULL,
    email         VARCHAR(150) UNIQUE NOT NULL,
    status        VARCHAR(100) NOT NULL,
    password_hash VARCHAR(200) NOT NULL,
    created_at    TIMESTAMP NOT NULL ,
    updated_at    TIMESTAMP NOT NULL
);


-- ============================================================================
-- TABLE: workspaces
--
-- Represents company and organizations on system
--
-- A workspace can have N users relation table (workspace_member)
-- A workspace can have N invitations
-- A workspace can have N projects
-- ============================================================================

CREATE TABLE workspaces
(
    id         UUID PRIMARY KEY,
    name       VARCHAR(150) NOT NULL ,
    slug       VARCHAR(200) UNIQUE NOT NULL ,
    created_at TIMESTAMP NOT NULL ,
    updated_at TIMESTAMP NOT NULL
);


-- ============================================================================
-- TABLE: workspaces_invitations
--
-- Represents an invitation to a new/old user to an workspace
--
-- An invitation is from a workspace
-- ============================================================================

CREATE TABLE workspace_invitations
(
    id           UUID PRIMARY KEY,
    workspace_id UUID NOT NULL ,
    email        VARCHAR(200) NOT NULL ,
    role         VARCHAR(100) NOT NULL ,
    token        VARCHAR(200) UNIQUE NOT NULL ,
    status       VARCHAR(100) NOT NULL ,
    invited_by  UUID NOT NULL ,
    created_at    TIMESTAMP NOT NULL,
    expires_at    TIMESTAMP NOT NULL,
    accepted_at   TIMESTAMP
);

ALTER TABLE workspace_invitations
    ADD CONSTRAINT fk_workspace_invitations_workspace_id FOREIGN KEY (workspace_id) REFERENCES workspaces (id);
ALTER TABLE workspace_invitations
    ADD CONSTRAINT fk_workspace_invitations_invited_by FOREIGN KEY (invited_by) REFERENCES users (id);

-- ============================================================================
-- TABLE: workspaces_members
--
-- Represents an relation between the user and the workspaces
--
-- An user can be on N users
-- A Workspace can have N users
-- ============================================================================

CREATE TABLE workspace_members
(
    id        UUID PRIMARY KEY,
    workspace_id UUID NOT NULL,
    user_id   UUID NOT NULL,
    role VARCHAR(100) NOT NULL ,
    status    VARCHAR(100) NOT NULL ,
    joined_at TIMESTAMP NOT NULL
);

ALTER TABLE workspace_members
    ADD CONSTRAINT fk_workspace_members_workspace_id FOREIGN KEY (workspace_id) REFERENCES workspaces (id);

ALTER TABLE workspace_members
    ADD CONSTRAINT fk_workspace_members_user_id FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE  workspace_members ADD CONSTRAINT uq_workspace_members_workspace_user
        UNIQUE (workspace_id, user_id);

-- ============================================================================
-- TABLE: audit_logs
--
-- Represents the actions on the system for audit changes in the system
--
-- An audit log is related for 1 user and 1 workspace
-- Have an type and id of the entity that was changed
-- ============================================================================


CREATE TABLE audit_logs
(
    id            UUID PRIMARY KEY,
    workspace_id  UUID NOT NULL,
    user_id       UUID NOT NULL,
    resource_type VARCHAR(100) NOT NULL ,
    resource_name VARCHAR(100) NOT NULL ,
    resource_id   VARCHAR(200) NOT NULL,
    action        VARCHAR(100) NOT NULL ,
    changes       JSONB,
    created_at    TIMESTAMP NOT NULL
);

ALTER TABLE audit_logs
    ADD CONSTRAINT fk_audit_logs_workspace_id FOREIGN KEY (workspace_id) REFERENCES workspaces(id);

ALTER TABLE audit_logs
    ADD CONSTRAINT fk_audit_logs_user
        FOREIGN KEY (user_id)
            REFERENCES users(id);

-- ============================================================================
-- TABLE: projects
--
-- Represents the projects  a workspace
--
-- An project is only on a specific workspace
-- ============================================================================

CREATE TABLE projects
(
    id           UUID PRIMARY KEY,
    workspace_id UUID NOT NULL,
    name         VARCHAR(100) NOT NULL ,
    description  TEXT,
    created_at    TIMESTAMP NOT NULL ,
    updated_at    TIMESTAMP NOT NULL
);

ALTER TABLE projects
    ADD CONSTRAINT fk_projects_workspace_id FOREIGN KEY (workspace_id) REFERENCES workspaces(id);

-- ============================================================================
-- TABLE: environments
--
-- Represents the environment like homolog, production, development.
--
-- Every project has N environments
-- ============================================================================

CREATE TABLE environments
(
    id             UUID PRIMARY KEY,
    project_id     UUID NOT NULL,
    name           VARCHAR(100) NOT NULL ,
    sdk_key_hash   VARCHAR(200) NOT NULL ,
    sdk_key_prefix VARCHAR(200) NOT NULL ,
    created_at     TIMESTAMP NOT NULL
);

ALTER TABLE environments
    ADD CONSTRAINT fk_environments_project_id FOREIGN KEY (project_id) REFERENCES projects(id);

-- ============================================================================
-- TABLE: feature_flags
--
-- Represents the feature_flags on the system that is on a specific environment on an specific project
--
-- Every environment have N feature_flags
-- ============================================================================


CREATE TABLE feature_flags
(
    id             UUID PRIMARY KEY,
    environment_id UUID NOT NULL,
    name           VARCHAR(150) NOT NULL ,
    key            VARCHAR(200) NOT NULL ,
    description    TEXT,
    enabled        BOOLEAN NOT NULL ,
    archived       BOOLEAN NOT NULL ,
    default_value  BOOLEAN NOT NULL,
    created_at     TIMESTAMP NOT NULL ,
    updated_at     TIMESTAMP NOT NULL
);

ALTER TABLE feature_flags
    ADD CONSTRAINT fk_feature_flags_environment_id FOREIGN KEY (environment_id) REFERENCES environments(id);

ALTER TABLE feature_flags ADD CONSTRAINT uq_feature_flags_environment_key
    UNIQUE (environment_id, key);

-- ============================================================================
-- TABLE: rollout_configurations
--
-- Represents the rollout configuration of a feature_flag (Percentage in this version)
--
-- Every rollout configuration have only 1 feature flag
-- ============================================================================

CREATE TABLE rollout_configurations
(
    id              UUID PRIMARY KEY,
    feature_flag_id UUID NOT NULL,
    percentage      INTEGER NOT NULL
);

ALTER TABLE rollout_configurations
    ADD CONSTRAINT fk_rollout_configurations_feature_flag_id FOREIGN KEY (feature_flag_id) REFERENCES feature_flags(id);
ALTER TABLE rollout_configurations ADD  CONSTRAINT uq_rollout_feature_flag
    UNIQUE (feature_flag_id);

-- ============================================================================
-- TABLE: feature_flag_targets
--
-- Represents if the feature flag have specific targets on the client side.
--
-- Every feature flag can have N targets
-- ============================================================================

CREATE TABLE feature_flag_targets
(
    id                UUID PRIMARY KEY,
    feature_flag_id   UUID NOT NULL,
    target_type     VARCHAR(50) NOT NULL,
    target_key      VARCHAR(200) NOT NULL,
    created_at      TIMESTAMP NOT NULL
);

ALTER TABLE feature_flag_targets
    ADD CONSTRAINT fk_feature_flag_targets_feature_flag_id FOREIGN KEY (feature_flag_id) REFERENCES feature_flags(id);

ALTER TABLE feature_flag_targets ADD CONSTRAINT uq_feature_flag_targets
        UNIQUE (
            feature_flag_id,
            target_type,
            target_key
        );