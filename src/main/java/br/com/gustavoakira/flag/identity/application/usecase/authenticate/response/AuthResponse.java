package br.com.gustavoakira.flag.identity.application.usecase.authenticate.response;

import java.time.Instant;

public record AuthResponse(String jwt, Instant expiresDate) {}
