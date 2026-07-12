package br.com.gustavoakira.flag.identity.application.port.output;

import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.User;
import java.util.Optional;

public interface UserRepositoryPort {
  User createUser(User user);

  Optional<User> findUserByEmail(Email email);
}
