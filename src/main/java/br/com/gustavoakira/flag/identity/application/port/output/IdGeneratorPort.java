package br.com.gustavoakira.flag.identity.application.port.output;

import br.com.gustavoakira.flag.identity.domain.UserId;

public interface IdGeneratorPort {
  UserId generateUserId();
}
