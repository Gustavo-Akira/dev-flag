package br.com.gustavoakira.flag.identity.adapter.input.api.dto.input;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record UserRegisterRequest(
        @NotNull
        @NotEmpty
        String name,
        @NotNull
        @NotEmpty
        String email,
        @NotNull
        @NotEmpty
        String password
) {
}
