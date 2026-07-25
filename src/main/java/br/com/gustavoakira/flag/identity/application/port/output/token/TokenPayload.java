package br.com.gustavoakira.flag.identity.application.port.output.token;

import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.UserId;

public record TokenPayload(
        UserId userId,
        Email email
) {
}