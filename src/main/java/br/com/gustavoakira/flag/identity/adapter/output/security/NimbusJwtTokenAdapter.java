package br.com.gustavoakira.flag.identity.adapter.output.security;

import br.com.gustavoakira.flag.identity.adapter.output.config.JwtProperties;
import br.com.gustavoakira.flag.identity.application.port.output.ClockPort;
import br.com.gustavoakira.flag.identity.application.port.output.token.GeneratedToken;
import br.com.gustavoakira.flag.identity.application.port.output.token.TokenClaims;
import br.com.gustavoakira.flag.identity.application.port.output.token.TokenPayload;
import br.com.gustavoakira.flag.identity.application.port.output.token.TokenProviderPort;
import br.com.gustavoakira.flag.identity.application.usecase.exceptions.InvalidTokenException;
import br.com.gustavoakira.flag.identity.application.usecase.exceptions.TokenGenerationException;
import br.com.gustavoakira.flag.identity.domain.Email;
import br.com.gustavoakira.flag.identity.domain.UserId;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;
import org.springframework.stereotype.Service;

@Service
public final class NimbusJwtTokenAdapter implements TokenProviderPort {
  private final JwtProperties properties;
  private final JWSVerifier verifier;
  private final JWSSigner signer;
  private final ClockPort clockPort;

  public NimbusJwtTokenAdapter(
      JwtProperties properties, JwtNimbusSignerFactory jwtNimbusSignerFactory, ClockPort port)
      throws JOSEException {
    this.properties = properties;
    verifier = jwtNimbusSignerFactory.verifier();
    signer = jwtNimbusSignerFactory.signer();
    this.clockPort = port;
  }

  @Override
  public GeneratedToken generate(TokenPayload tokenPayload) {

    Instant now = clockPort.now().atZone(ZoneId.systemDefault()).toInstant();
    Instant expiration = now.plus(properties.expiration());
    if (tokenPayload.userId() == null || tokenPayload.email() == null) {
      throw new TokenGenerationException("Email or user id null", null);
    }
    JWTClaimsSet claims =
        new JWTClaimsSet.Builder()
            .subject(tokenPayload.userId().value().toString())
            .claim("email", tokenPayload.email().value())
            .issueTime(Date.from(now))
            .expirationTime(Date.from(expiration))
            .build();

    SignedJWT jwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claims);
    try {
      jwt.sign(signer);
    } catch (JOSEException e) {
      throw new TokenGenerationException(e.getMessage(), e);
    }
    return new GeneratedToken(jwt.serialize(), expiration);
  }

  @Override
  public TokenClaims validate(String token) {

    try {
      SignedJWT jwt = SignedJWT.parse(token);

      if (!jwt.verify(verifier)) {
        throw new InvalidTokenException();
      }

      JWTClaimsSet claims = jwt.getJWTClaimsSet();

      Instant expiration = claims.getExpirationTime().toInstant();
      Instant now = clockPort.now().toInstant(ZoneOffset.UTC);

      if (expiration.isBefore(now)) {
        throw new InvalidTokenException();
      }

      return new TokenClaims(
          UserId.of(UUID.fromString(claims.getSubject())),
          new Email(claims.getStringClaim("email")).value(),
          claims.getIssueTime().toInstant(),
          expiration);

    } catch (InvalidTokenException e) {
      throw e;
    } catch (Exception e) {
      throw new InvalidTokenException();
    }
  }
}
