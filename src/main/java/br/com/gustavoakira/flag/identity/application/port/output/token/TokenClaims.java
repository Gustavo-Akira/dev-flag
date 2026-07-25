package br.com.gustavoakira.flag.identity.application.port.output.token;

import br.com.gustavoakira.flag.identity.domain.UserId;
import java.time.Instant;

public record TokenClaims(UserId userId, String email, Instant issuedAt, Instant expiresAt) {}
