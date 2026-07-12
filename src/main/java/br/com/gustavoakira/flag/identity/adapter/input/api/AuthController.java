package br.com.gustavoakira.flag.identity.adapter.input.api;

import br.com.gustavoakira.flag.identity.adapter.input.api.dto.input.UserRegisterRequest;
import br.com.gustavoakira.flag.identity.adapter.input.api.dto.output.UserResponseDTO;
import br.com.gustavoakira.flag.identity.application.usecase.create.CreateUserUseCase;
import br.com.gustavoakira.flag.identity.application.usecase.create.command.CreateUserCommand;
import br.com.gustavoakira.flag.identity.domain.User;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
  private final CreateUserUseCase useCase;

  public AuthController(CreateUserUseCase useCase) {
    this.useCase = useCase;
  }

  @PostMapping("register")
  public ResponseEntity<UserResponseDTO> register(@RequestBody @Valid UserRegisterRequest request) {
    CreateUserCommand userCommand =
        new CreateUserCommand(request.name(), request.email(), request.password());
    User user = useCase.execute(userCommand);
    UserResponseDTO responseDTO =
        new UserResponseDTO(
            user.getId().value().toString(),
            user.getName(),
            user.getEmail().value(),
            user.getStatus().name(),
            user.getCreatedAt(),
            user.getUpdatedAt());
    return ResponseEntity.created(URI.create("/api/v1/users/" + responseDTO.id()))
        .body(responseDTO);
  }
}
