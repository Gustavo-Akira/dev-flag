package br.com.gustavoakira.flag.identity.application.usecase.exceptions;

public class InvalidCredentialException extends RuntimeException {
  public InvalidCredentialException() {
    super("Invalid credential");
  }
}
