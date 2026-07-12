package br.com.gustavoakira.flag.identity.domain;


import java.time.LocalDateTime;

public class User {
    private final UserId id;
    private String name;
    private final Email email;
    private String passwordHash;
    private UserStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private User(UserId id, String name, Email email, String passwordHash, UserStatus status, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwordHash = passwordHash;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        validate();
    }

    public static User create(UserId id, String name, Email email, String passwordHash, LocalDateTime createdAt){
        return new User(
                id,
                name,
                email,
                passwordHash,
                UserStatus.ACTIVE,
                createdAt,
                null
        );
    }

    public static User reconstitute(
            UserId id,
            String name,
            Email email,
            String passwordHash,
            UserStatus status,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new User(
                id,
                name,
                email,
                passwordHash,
                status,
                createdAt,
                updatedAt
        );
    }

    public UserId getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Email getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void deactivate(LocalDateTime now){
        this.status = UserStatus.INACTIVE;
        touch(now);
    }

    public void changeName(String name, LocalDateTime now) {
        validateName(name);
        this.name = name;
        touch(now);
    }

    public void changePassword(String passwordHash, LocalDateTime now) {
        validatePasswordHash(passwordHash);
        this.passwordHash = passwordHash;
        touch(now);
    }

    private void validateName(String name){
        if(name == null  || name.isBlank()){
            throw new DomainRuleException("Name cannot be empty or null");
        }
    }

    private void validatePasswordHash(String passwordHash){
        if(passwordHash == null || passwordHash.isBlank()){
            throw new DomainRuleException("Password cannot be null or empty");
        }
    }

    private void validateUpdatedAt(LocalDateTime updatedAt){
        if (updatedAt != null && updatedAt.isBefore(createdAt)) {
            throw new DomainRuleException("UpdatedAt cannot be before createdAt");
        }
    }

    private void touch(LocalDateTime now) {
        if (now == null) {
            throw new DomainRuleException("UpdatedAt cannot be null");
        }
        validateUpdatedAt(now);
        this.updatedAt = now;
    }

    private void validate(){
        validateName(name);
        validatePasswordHash(passwordHash);

        if (email == null){
            throw new DomainRuleException("Email cannot be null");
        }

        if(status ==null){
            throw new DomainRuleException("Status cannot be null");
        }

        if (createdAt == null) {
            throw new DomainRuleException("CreatedAt cannot be null");
        }
        validateUpdatedAt(updatedAt);
    }
}
