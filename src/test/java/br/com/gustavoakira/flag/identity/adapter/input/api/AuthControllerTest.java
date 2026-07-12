package br.com.gustavoakira.flag.identity.adapter.input.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import br.com.gustavoakira.flag.identity.adapter.output.config.SecurityConfig;
import br.com.gustavoakira.flag.identity.application.usecase.create.CreateUserUseCase;
import br.com.gustavoakira.flag.identity.application.usecase.create.command.CreateUserCommand;
import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.User;
import br.com.gustavoakira.flag.identity.domain.UserId;
import br.com.gustavoakira.flag.identity.domain.UserStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(SecurityConfig.class)
class AuthControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private CreateUserUseCase createUserUseCase;

  @Test
  void shouldRegisterUserSuccessfully() throws Exception {

    LocalDateTime now = LocalDateTime.now();

    User user =
        User.reconstitute(
            UserId.of(UUID.randomUUID()),
            "Gustavo",
            new Email("gustavo@email.com"),
            "$2a$10$abcdefghijklmnopqrstuv",
            UserStatus.ACTIVE,
            now,
            now.plusSeconds(60));

    when(createUserUseCase.execute(any(CreateUserCommand.class))).thenReturn(user);

    String request =
        """
                {
                  "name":"Gustavo",
                  "email":"gustavo@email.com",
                  "password":"12345678"
                }
                """;

    mockMvc
        .perform(
            post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(request))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/v1/users/" + user.getId().value()))
        .andExpect(jsonPath("$.id").value(user.getId().value().toString()))
        .andExpect(jsonPath("$.name").value("Gustavo"))
        .andExpect(jsonPath("$.email").value("gustavo@email.com"))
        .andExpect(jsonPath("$.status").value("ACTIVE"));

    ArgumentCaptor<CreateUserCommand> captor = ArgumentCaptor.forClass(CreateUserCommand.class);

    verify(createUserUseCase).execute(captor.capture());

    CreateUserCommand command = captor.getValue();

    assertThat(command.name()).isEqualTo("Gustavo");
    assertThat(command.email()).isEqualTo("gustavo@email.com");
    assertThat(command.password()).isEqualTo("12345678");
  }

  @Test
  void shouldReturnBadRequestWhenRequestIsInvalid() throws Exception {

    String request =
        """
                {
                  "name":"",
                  "email":"email-invalido",
                  "password":""
                }
                """;

    mockMvc
        .perform(
            post("/api/v1/auth/register").contentType(MediaType.APPLICATION_JSON).content(request))
        .andExpect(status().isBadRequest());
  }
}
