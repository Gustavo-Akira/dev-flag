# Schema Decisions (V1)

## Tenant Isolation

All business data belongs to a workspace.

Tenant boundary:

```txt
workspace_id
```

Used in:
- projects
- audit_logs

Indirectly inherited:
- environments
- feature_flags

---

## UUIDs

All entities use UUID identifiers.

Reason:
- safer public exposure
- distributed friendly
- easier future scaling

---

## Environment SDK Key

Each environment contains:

```txt
sdk_key
```

Purpose:
- SDK authentication
- environment isolation

Example:

```txt
sdk_prod_abc123
```

---

## Unique Constraints

### Workspace

```sql
UNIQUE(slug)
```

### Project

```sql
UNIQUE(workspace_id, name)
```

### Feature Flag

```sql
UNIQUE(environment_id, key)
```

### Workspace Member

```sql
UNIQUE(workspace_id, user_id)
```

### User Targeting

```sql
UNIQUE(feature_flag_id, user_id)
```

---

## Index Recommendations

### Feature evaluation

```sql
INDEX(environment_id, key)
```

### Audit

```sql
INDEX(workspace_id, created_at)
```

### SDK fetch

```sql
INDEX(project_id)
```

---

## Future Evolution

### V2
- ETag versioning
- Config versions

### V3
- SSE/WebSocket sync

### V4
- Incremental updates
