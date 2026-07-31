package br.com.gustavoakira.flag.identity.application.usecase.authenticate;

import br.com.gustavoakira.flag.identity.application.port.output.CryptographyPort;
import br.com.gustavoakira.flag.identity.application.port.output.UserRepositoryPort;
import br.com.gustavoakira.flag.identity.application.port.output.token.GeneratedToken;
import br.com.gustavoakira.flag.identity.application.port.output.token.TokenPayload;
import br.com.gustavoakira.flag.identity.application.port.output.token.TokenProviderPort;
import br.com.gustavoakira.flag.identity.application.usecase.authenticate.command.AuthCommand;
import br.com.gustavoakira.flag.identity.application.usecase.authenticate.response.AuthResponse;
import br.com.gustavoakira.flag.identity.application.usecase.exceptions.InvalidCredentialException;
import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.User;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateUseCase {
  private final CryptographyPort cryptographyPort;
  private final UserRepositoryPort userRepositoryPort;
  private final TokenProviderPort tokenProviderPort;

  public AuthenticateUseCase(
      CryptographyPort cryptographyPort,
      UserRepositoryPort userRepositoryPort,
      TokenProviderPort tokenProviderPort) {
    this.cryptographyPort = cryptographyPort;
    this.userRepositoryPort = userRepositoryPort;
    this.tokenProviderPort = tokenProviderPort;
  }

  public AuthResponse execute(AuthCommand command) {
    Email email = new Email(command.email());
    User userEmail =
        userRepositoryPort.findUserByEmail(email).orElseThrow(InvalidCredentialException::new);

    if (!cryptographyPort.compare(command.password(), userEmail.getPasswordHash())) {
      throw new InvalidCredentialException();
    }
    GeneratedToken token = tokenProviderPort.generate(new TokenPayload(userEmail.getId(), email));
    return new AuthResponse(token.token(), token.expiresAt());
  }
}
