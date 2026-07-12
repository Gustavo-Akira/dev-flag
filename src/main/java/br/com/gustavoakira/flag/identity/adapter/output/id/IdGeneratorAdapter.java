package br.com.gustavoakira.flag.identity.adapter.output.id;

import br.com.gustavoakira.flag.identity.application.port.output.IdGeneratorPort;
import br.com.gustavoakira.flag.identity.domain.UserId;
import java.util.UUID;
import org.hibernate.id.uuid.UuidVersion7Strategy;
import org.springframework.stereotype.Service;

@Service
public class IdGeneratorAdapter implements IdGeneratorPort {

  private final UuidVersion7Strategy strategy = UuidVersion7Strategy.INSTANCE;

  @Override
  public UserId generateUserId() {
    UUID uuid = strategy.generateUUID(null);
    return UserId.of(uuid);
  }
}
