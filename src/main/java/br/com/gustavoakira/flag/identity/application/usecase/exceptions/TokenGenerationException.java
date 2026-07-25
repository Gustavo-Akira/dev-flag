package br.com.gustavoakira.flag.identity.application.usecase.exceptions;

public class TokenGenerationException extends RuntimeException {

    public TokenGenerationException(String message, Throwable cause) {
        super(message, cause);
    }
}