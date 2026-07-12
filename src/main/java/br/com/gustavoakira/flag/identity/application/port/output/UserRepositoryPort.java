package br.com.gustavoakira.flag.identity.application.port.output;

import br.com.gustavoakira.flag.identity.domain.User;

public interface UserRepositoryPort {
  User createUser(User user);
}
