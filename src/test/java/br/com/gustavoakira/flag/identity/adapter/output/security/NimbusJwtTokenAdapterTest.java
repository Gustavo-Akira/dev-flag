package br.com.gustavoakira.flag.identity.adapter.output.security;

import static org.junit.jupiter.api.Assertions.*;

import br.com.gustavoakira.flag.identity.adapter.output.config.JwtProperties;
import br.com.gustavoakira.flag.identity.application.port.output.ClockPort;
import br.com.gustavoakira.flag.identity.application.port.output.token.GeneratedToken;
import br.com.gustavoakira.flag.identity.application.port.output.token.TokenPayload;
import br.com.gustavoakira.flag.identity.application.usecase.exceptions.TokenGenerationException;
import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.UserId;
import com.nimbusds.jose.JOSEException;
import java.time.*;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class NimbusJwtTokenAdapterTest {

  private static final Instant NOW = Instant.parse("2026-07-26T12:00:00Z");

  private static final Duration EXPIRATION = Duration.ofHours(1);
  @Mock private ClockPort clockPort;

  private NimbusJwtTokenAdapter adapter;

  @BeforeEach
  void setUp() throws JOSEException {

    JwtProperties properties =
        new JwtProperties("a-very-long-secret-with-at-least-32-bytes", EXPIRATION);

    JwtNimbusSignerFactory jwtNimbusSignerFactory = new JwtNimbusSignerFactory(properties);

    Mockito.when(clockPort.now()).thenReturn(LocalDateTime.ofInstant(NOW, ZoneId.systemDefault()));
    adapter = new NimbusJwtTokenAdapter(properties, jwtNimbusSignerFactory, clockPort);
  }

  @Test
  void shouldGenerateTokenWithSuccess() throws JOSEException {
    Email email = new Email("akirauekita200@gmail.com");
    UserId userId = new UserId(UUID.randomUUID());
    TokenPayload payload = new TokenPayload(userId, email);
    GeneratedToken token = adapter.generate(payload);
    assertNotNull(token.token());
    assertNotEquals("", token.token());
    assertEquals(NOW.plus(EXPIRATION), token.expiresAt());
  }

  @Test
  void shouldThrowTokenExceptionWhenUserIdIsNull() {
    Email email = new Email("akirauekita200@gmail.com");
    TokenPayload payload = new TokenPayload(null, email);
    assertThrows(
        TokenGenerationException.class,
        () -> {
          adapter.generate(payload);
        });
  }

  @Test
  void shouldThrowTokenExceptionWhenEmailIsNull() {
    UserId id = new UserId(UUID.randomUUID());
    TokenPayload payload = new TokenPayload(id, null);
    assertThrows(
        TokenGenerationException.class,
        () -> {
          adapter.generate(payload);
        });
  }
}
