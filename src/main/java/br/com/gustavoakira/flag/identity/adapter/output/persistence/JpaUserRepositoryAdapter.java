package br.com.gustavoakira.flag.identity.adapter.output.persistence;

import br.com.gustavoakira.flag.identity.application.port.output.UserRepositoryPort;
import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.User;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class JpaUserRepositoryAdapter implements UserRepositoryPort {

  private final SpringDataJpaUserRepository repository;
  private final UserPersistenceMapper mapper;

  public JpaUserRepositoryAdapter(
      SpringDataJpaUserRepository repository, UserPersistenceMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public User createUser(User user) {
    UserJpaEntity entity = mapper.toEntity(user);
    UserJpaEntity saved = repository.save(entity);

    return mapper.toDomain(saved);
  }

  @Override
  public Optional<User> findUserByEmail(Email email) {
    return repository.findByEmail(email.value()).map(mapper::toDomain);
  }
}
