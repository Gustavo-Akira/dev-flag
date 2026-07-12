package br.com.gustavoakira.flag.identity.adapter.output.persistence;

import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.User;
import br.com.gustavoakira.flag.identity.domain.UserId;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceMapper {

    public User toDomain(UserJpaEntity entity) {
        return User.reconstitute(
                UserId.of(entity.getId()),
                entity.getName(),
                new Email(entity.getEmail()),
                entity.getPasswordHash(),
                entity.getStatus(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    public UserJpaEntity toEntity(User user) {
        return new UserJpaEntity(
                user.getId().value(),
                user.getName(),
                user.getEmail().value(),
                user.getPasswordHash(),
                user.getStatus(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}