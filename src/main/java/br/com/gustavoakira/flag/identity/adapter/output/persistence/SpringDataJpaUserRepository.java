package br.com.gustavoakira.flag.identity.adapter.output.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataJpaUserRepository extends JpaRepository<UserJpaEntity, UUID> {
}
