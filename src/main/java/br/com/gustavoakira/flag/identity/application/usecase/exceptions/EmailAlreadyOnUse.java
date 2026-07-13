package br.com.gustavoakira.flag.identity.application.usecase.exceptions;

public class EmailAlreadyOnUse extends RuntimeException {
    public EmailAlreadyOnUse(String message) {
        super(message);
    }
}
