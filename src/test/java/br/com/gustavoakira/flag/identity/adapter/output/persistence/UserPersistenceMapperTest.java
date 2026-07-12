package br.com.gustavoakira.flag.identity.adapter.output.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.User;
import br.com.gustavoakira.flag.identity.domain.UserId;
import br.com.gustavoakira.flag.identity.domain.UserStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class UserPersistenceMapperTest {

  private static final UserPersistenceMapper MAPPER = new UserPersistenceMapper();

  @Test
  void shouldMapJpaEntityToDomain() {
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = createdAt.plusSeconds(60);

    UserJpaEntity entity =
        new UserJpaEntity(
            id,
            "Gustavo",
            "gustavo@email.com",
            "hashed-password",
            UserStatus.ACTIVE,
            createdAt,
            updatedAt);

    User user = MAPPER.toDomain(entity);

    assertEquals(id, user.getId().value());
    assertEquals("Gustavo", user.getName());
    assertEquals("gustavo@email.com", user.getEmail().value());
    assertEquals("hashed-password", user.getPasswordHash());
    assertEquals(UserStatus.ACTIVE, user.getStatus());
    assertEquals(createdAt, user.getCreatedAt());
    assertEquals(updatedAt, user.getUpdatedAt());
  }

  @Test
  void shouldMapDomainToJpaEntity() {
    UUID id = UUID.randomUUID();
    LocalDateTime createdAt = LocalDateTime.now();
    LocalDateTime updatedAt = createdAt.plusSeconds(60);

    User user =
        User.reconstitute(
            UserId.of(id),
            "Gustavo",
            new Email("gustavo@email.com"),
            "hashed-password",
            UserStatus.ACTIVE,
            createdAt,
            updatedAt);

    UserJpaEntity entity = MAPPER.toEntity(user);

    assertEquals(id, entity.getId());
    assertEquals("Gustavo", entity.getName());
    assertEquals("gustavo@email.com", entity.getEmail());
    assertEquals("hashed-password", entity.getPasswordHash());
    assertEquals(UserStatus.ACTIVE, entity.getStatus());
    assertEquals(createdAt, entity.getCreatedAt());
    assertEquals(updatedAt, entity.getUpdatedAt());
  }
}
