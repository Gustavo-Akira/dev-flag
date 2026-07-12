package br.com.gustavoakira.flag.identity.adapter.output.persistence;

import br.com.gustavoakira.flag.identity.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JpaUserRepositoryAdapterTest {

    @Mock
    private SpringDataJpaUserRepository jpaRepository;

    @Mock
    private UserPersistenceMapper mapper;

    @Mock
    private User user;

    @Mock
    private UserJpaEntity entity;

    @Mock
    private UserJpaEntity savedEntity;

    @Mock
    private User savedUser;

    private JpaUserRepositoryAdapter adapter;

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

        verifyNoMoreInteractions(
                mapper,
                jpaRepository
        );
    }
}