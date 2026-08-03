package br.com.gustavoakira.flag.identity.application.port.output.token;

import java.text.ParseException;

public interface TokenProviderPort {
  GeneratedToken generate(TokenPayload tokenPayload);

  TokenClaims validate(String token) throws ParseException;
}
