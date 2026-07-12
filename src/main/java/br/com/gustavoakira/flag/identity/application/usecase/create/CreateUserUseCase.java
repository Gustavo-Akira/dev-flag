package br.com.gustavoakira.flag.identity.application.usecase.create;

import br.com.gustavoakira.flag.identity.application.port.output.ClockPort;
import br.com.gustavoakira.flag.identity.application.port.output.CryptographyPort;
import br.com.gustavoakira.flag.identity.application.port.output.IdGeneratorPort;
import br.com.gustavoakira.flag.identity.application.port.output.UserRepositoryPort;
import br.com.gustavoakira.flag.identity.application.usecase.create.command.CreateUserCommand;
import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.User;
import br.com.gustavoakira.flag.identity.domain.UserId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CreateUserUseCase {

    private final CryptographyPort cryptographyPort;
    private final UserRepositoryPort userRepositoryPort;
    private final IdGeneratorPort idGeneratorPort;
    private final ClockPort clockPort;

    public CreateUserUseCase(CryptographyPort cryptographyPort, UserRepositoryPort userRepositoryPort, IdGeneratorPort idGeneratorPort, ClockPort clockPort) {
        this.cryptographyPort = cryptographyPort;
        this.userRepositoryPort = userRepositoryPort;
        this.idGeneratorPort = idGeneratorPort;
        this.clockPort = clockPort;
    }

    @Transactional
    public User execute(CreateUserCommand command){
        UserId createdId = idGeneratorPort.generateUserId();
        String passwordHash = cryptographyPort.hash(command.password());
        Email email = new Email(command.email());
        LocalDateTime now = clockPort.now();
        User user =  User.create(createdId, command.name(), email, passwordHash, now);
        return userRepositoryPort.createUser(user);
    }
}
