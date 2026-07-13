package br.com.gustavoakira.flag.identity.application.usecase.create;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import br.com.gustavoakira.flag.identity.application.port.output.ClockPort;
import br.com.gustavoakira.flag.identity.application.port.output.CryptographyPort;
import br.com.gustavoakira.flag.identity.application.port.output.IdGeneratorPort;
import br.com.gustavoakira.flag.identity.application.port.output.UserRepositoryPort;
import br.com.gustavoakira.flag.identity.application.usecase.create.command.CreateUserCommand;
import br.com.gustavoakira.flag.identity.application.usecase.exceptions.EmailAlreadyOnUse;
import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.User;
import br.com.gustavoakira.flag.identity.domain.UserId;
import br.com.gustavoakira.flag.identity.domain.UserStatus;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

  @Mock private CryptographyPort cryptographyPort;

  @Mock private UserRepositoryPort userRepositoryPort;

  @Mock private IdGeneratorPort idGeneratorPort;

  @Mock private ClockPort clockPort;

  @InjectMocks private CreateUserUseCase useCase;

  @Test
  void shouldCreateUserSuccessfully() {
    UserId userId = new UserId(UUID.randomUUID());
    LocalDateTime now = LocalDateTime.of(2026, 7, 11, 12, 0);

    CreateUserCommand command = new CreateUserCommand("Gustavo", "gustavo@email.com", "123456");

    when(idGeneratorPort.generateUserId()).thenReturn(userId);
    when(cryptographyPort.hash("123456")).thenReturn("hashed-password");
    when(clockPort.now()).thenReturn(now);
    when(userRepositoryPort.createUser(any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(userRepositoryPort.findUserByEmail(new Email("gustavo@email.com"))).thenReturn(Optional.empty());

    User result = useCase.execute(command);

    assertNotNull(result);
    assertEquals(userId, result.getId());
    assertEquals("Gustavo", result.getName());
    assertEquals("gustavo@email.com", result.getEmail().value());
    assertEquals("hashed-password", result.getPasswordHash());
    assertEquals(UserStatus.ACTIVE, result.getStatus());
    assertEquals(now, result.getCreatedAt());
    assertNull(result.getUpdatedAt());

    verify(idGeneratorPort).generateUserId();
    verify(cryptographyPort).hash("123456");
    verify(clockPort).now();
    verify(userRepositoryPort).createUser(any(User.class));
  }

  @Test
  void shouldPersistCorrectUser() {
    UserId userId = new UserId(UUID.randomUUID());
    LocalDateTime now = LocalDateTime.now();

    CreateUserCommand command = new CreateUserCommand("Gustavo", "gustavo@email.com", "123456");

    when(idGeneratorPort.generateUserId()).thenReturn(userId);
    when(cryptographyPort.hash("123456")).thenReturn("hashed-password");
    when(clockPort.now()).thenReturn(now);
    when(userRepositoryPort.createUser(any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));
    when(userRepositoryPort.findUserByEmail(new Email("gustavo@email.com"))).thenReturn(Optional.empty());

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);

    useCase.execute(command);

    verify(userRepositoryPort).createUser(captor.capture());

    User user = captor.getValue();

    assertEquals(userId, user.getId());
    assertEquals("Gustavo", user.getName());
    assertEquals("gustavo@email.com", user.getEmail().value());
    assertEquals("hashed-password", user.getPasswordHash());
    assertEquals(UserStatus.ACTIVE, user.getStatus());
    assertEquals(now, user.getCreatedAt());
    assertNull(user.getUpdatedAt());
  }

  @Test
  void shouldNotPersistWhenEmailAlreadyInUse(){
    UserId userId = new UserId(UUID.randomUUID());
    LocalDateTime now = LocalDateTime.of(2026, 7, 11, 12, 0);

    CreateUserCommand command = new CreateUserCommand("Gustavo", "gustavo@email.com", "123456");

    when(idGeneratorPort.generateUserId()).thenReturn(userId);
    when(cryptographyPort.hash("123456")).thenReturn("hashed-password");
    when(clockPort.now()).thenReturn(now);
    when(userRepositoryPort.findUserByEmail(new Email("gustavo@email.com"))).thenReturn(Optional.of(User.reconstitute(
            userId,
            "akira",
            new Email("gustavo@email.com"),
            "sfdasdf",
            UserStatus.ACTIVE,
            LocalDateTime.now(),
            null
    )));

    assertThrows(EmailAlreadyOnUse.class,()->useCase.execute(command));
  }
}
