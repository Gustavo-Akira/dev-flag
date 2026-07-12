package br.com.gustavoakira.flag.identity.application.usecase.create.command;

public record CreateUserCommand(String name, String email, String password) {}
