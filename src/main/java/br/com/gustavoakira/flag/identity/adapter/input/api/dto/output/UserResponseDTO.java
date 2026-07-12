package br.com.gustavoakira.flag.identity.adapter.input.api.dto.output;

import java.time.LocalDateTime;

public record UserResponseDTO(
        String id,
        String name,
        String email,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
