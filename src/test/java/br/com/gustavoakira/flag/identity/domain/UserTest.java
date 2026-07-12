package br.com.gustavoakira.flag.identity.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void shouldCreateUser() {
        UserId id = new UserId(UUID.randomUUID());
        LocalDateTime createdAt = LocalDateTime.now();

        User user = User.create(
                id,
                "Gustavo",
                new Email("gustavo@email.com"),
                "hashed-password",
                createdAt
        );

        assertEquals(id, user.getId());
        assertEquals("Gustavo", user.getName());
        assertEquals("gustavo@email.com", user.getEmail().value());
        assertEquals("hashed-password", user.getPasswordHash());
        assertEquals(UserStatus.ACTIVE, user.getStatus());
        assertEquals(createdAt, user.getCreatedAt());
        assertNull(user.getUpdatedAt());
    }

    @Test
    void shouldReconstituteUser() {
        UserId id = new UserId(UUID.randomUUID());
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        LocalDateTime updatedAt = LocalDateTime.now();

        User user = User.reconstitute(
                id,
                "Gustavo",
                new Email("gustavo@email.com"),
                "hashed-password",
                UserStatus.INACTIVE,
                createdAt,
                updatedAt
        );

        assertEquals(id, user.getId());
        assertEquals(UserStatus.INACTIVE, user.getStatus());
        assertEquals(createdAt, user.getCreatedAt());
        assertEquals(updatedAt, user.getUpdatedAt());
    }

    @Test
    void shouldChangeName() {
        User user = createUser();

        LocalDateTime now = LocalDateTime.now();

        user.changeName("Akira", now);

        assertEquals("Akira", user.getName());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldChangePassword() {
        User user = createUser();

        LocalDateTime now = LocalDateTime.now();

        user.changePassword("new-password-hash", now);

        assertEquals("new-password-hash", user.getPasswordHash());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldDeactivateUser() {
        User user = createUser();

        LocalDateTime now = LocalDateTime.now();

        user.deactivate(now);

        assertEquals(UserStatus.INACTIVE, user.getStatus());
        assertEquals(now, user.getUpdatedAt());
    }

    @Test
    void shouldThrowWhenNameIsNull() {
        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> User.create(
                        new UserId(UUID.randomUUID()),
                        null,
                        new Email("gustavo@email.com"),
                        "hashed-password",
                        LocalDateTime.now()
                )
        );

        assertEquals("Name cannot be empty or null", exception.getMessage());
    }

    @Test
    void shouldThrowWhenNameIsBlank() {
        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> User.create(
                        new UserId(UUID.randomUUID()),
                        " ",
                        new Email("gustavo@email.com"),
                        "hashed-password",
                        LocalDateTime.now()
                )
        );

        assertEquals("Name cannot be empty or null", exception.getMessage());
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> User.create(
                        new UserId(UUID.randomUUID()),
                        "Gustavo",
                        null,
                        "hashed-password",
                        LocalDateTime.now()
                )
        );

        assertEquals("Email cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowWhenPasswordIsNull() {
        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> User.create(
                        new UserId(UUID.randomUUID()),
                        "Gustavo",
                        new Email("gustavo@email.com"),
                        null,
                        LocalDateTime.now()
                )
        );

        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowWhenPasswordIsBlank() {
        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> User.create(
                        new UserId(UUID.randomUUID()),
                        "Gustavo",
                        new Email("gustavo@email.com"),
                        "",
                        LocalDateTime.now()
                )
        );

        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowWhenStatusIsNullWhenReconstituting() {
        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> User.reconstitute(
                        new UserId(UUID.randomUUID()),
                        "Gustavo",
                        new Email("gustavo@email.com"),
                        "hashed-password",
                        null,
                        LocalDateTime.now(),
                        null
                )
        );

        assertEquals("Status cannot be null", exception.getMessage());
    }

    @Test
    void shouldThrowWhenUpdatedAtIsBeforeCreatedAt() {
        LocalDateTime createdAt = LocalDateTime.now();

        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> User.reconstitute(
                        new UserId(UUID.randomUUID()),
                        "Gustavo",
                        new Email("gustavo@email.com"),
                        "hashed-password",
                        UserStatus.ACTIVE,
                        createdAt,
                        createdAt.minusSeconds(1)
                )
        );

        assertEquals("UpdatedAt cannot be before createdAt", exception.getMessage());
    }

    @Test
    void shouldThrowWhenChangingNameToBlank() {
        User user = createUser();

        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> user.changeName("", LocalDateTime.now())
        );

        assertEquals("Name cannot be empty or null", exception.getMessage());
    }

    @Test
    void shouldThrowWhenChangingPasswordToBlank() {
        User user = createUser();

        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> user.changePassword("", LocalDateTime.now())
        );

        assertEquals("Password cannot be null or empty", exception.getMessage());
    }

    @Test
    void shouldThrowWhenTouchingWithNullDate() {
        User user = createUser();

        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> user.changeName("Akira", null)
        );

        assertEquals("UpdatedAt cannot be null", exception.getMessage());
    }

    private User createUser() {
        return User.create(
                new UserId(UUID.randomUUID()),
                "Gustavo",
                new Email("gustavo@email.com"),
                "hashed-password",
                LocalDateTime.now()
        );
    }
}