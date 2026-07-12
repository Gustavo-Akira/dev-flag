package br.com.gustavoakira.flag.identity.domain;

import java.util.regex.Pattern;

public record Email(String value) {
    // Intentionally uses a simplified email validation.
    // The goal is to accept common email formats rather than fully implementing RFC 5322.
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[^@]+@[^@]+\\.[^@]+$");

    public Email {
        if (value == null) {
            throw new DomainRuleException("Email cannot be empty");
        }

        value = value.trim();

        if (value.isBlank()) {
            throw new DomainRuleException("Email cannot be empty");
        }

        if (!EMAIL_PATTERN.matcher(value).matches()) {
            throw new DomainRuleException("Invalid email format");
        }
    }

}
