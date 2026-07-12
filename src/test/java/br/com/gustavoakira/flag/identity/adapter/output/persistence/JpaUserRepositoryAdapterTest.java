package br.com.gustavoakira.flag.identity.adapter.output.persistence;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.User;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JpaUserRepositoryAdapterTest {

  @Mock private SpringDataJpaUserRepository jpaRepository;

  @Mock private UserPersistenceMapper mapper;

  @Mock private User user;

  @Mock private UserJpaEntity entity;

  @Mock private UserJpaEntity savedEntity;

  @Mock private User savedUser;

  private JpaUserRepositoryAdapter adapter;

  private final Email email = new Email("gustavo@email.com");

  @BeforeEach
  void setUp() {
    adapter = new JpaUserRepositoryAdapter(jpaRepository, mapper);
  }

  @Test
  void shouldCreateUser() {
    when(mapper.toEntity(user)).thenReturn(entity);
    when(jpaRepository.save(entity)).thenReturn(savedEntity);
    when(mapper.toDomain(savedEntity)).thenReturn(savedUser);

    User result = adapter.createUser(user);

    assertSame(savedUser, result);

    verify(mapper).toEntity(user);
    verify(jpaRepository).save(entity);
    verify(mapper).toDomain(savedEntity);

    verifyNoMoreInteractions(mapper, jpaRepository);
  }

  @Test
  void shouldGetUserWhenEmailIsFound() {
    when(jpaRepository.findByEmail(email.value())).thenReturn(Optional.of(savedEntity));
    when(mapper.toDomain(savedEntity)).thenReturn(savedUser);

    Optional<User> result = adapter.findUserByEmail(email);
    assertTrue(result.isPresent());
    assertSame(savedUser, result.get());

    verify(jpaRepository).findByEmail(email.value());
    verify(mapper).toDomain(savedEntity);

    verifyNoMoreInteractions(mapper, jpaRepository);
  }

  @Test
  void shouldReturnEmptyOptionalWhenEmailsIsNotFound() {
    when(jpaRepository.findByEmail(email.value())).thenReturn(Optional.empty());

    Optional<User> result = adapter.findUserByEmail(email);
    assertTrue(result.isEmpty());

    verify(jpaRepository).findByEmail(email.value());

    verifyNoMoreInteractions(jpaRepository);
  }
}
