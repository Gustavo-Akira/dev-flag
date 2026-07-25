package br.com.gustavoakira.flag.identity.application.port.output.token;

import java.time.Instant;

public record GeneratedToken(String token, Instant expiresAt) {}
