package br.com.gustavoakira.flag.identity.domain;

import java.util.Objects;
import java.util.UUID;

public record UserId(UUID value) {

    public UserId {
        Objects.requireNonNull(value, "User id cannot be null");
    }

    public static UserId of(UUID value) {
        return new UserId(value);
    }
}