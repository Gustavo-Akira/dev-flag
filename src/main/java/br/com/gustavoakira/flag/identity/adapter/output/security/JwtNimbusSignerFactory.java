package br.com.gustavoakira.flag.identity.adapter.output.security;

import br.com.gustavoakira.flag.identity.adapter.output.config.JwtProperties;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import java.nio.charset.StandardCharsets;
import org.springframework.stereotype.Component;

@Component
public class JwtNimbusSignerFactory {

  private final byte[] secret;

  public JwtNimbusSignerFactory(JwtProperties properties) {
    this.secret = properties.secret().getBytes(StandardCharsets.UTF_8);
  }

  public JWSSigner signer() throws JOSEException {
    return new MACSigner(secret);
  }

  public JWSVerifier verifier() throws JOSEException {
    return new MACVerifier(secret);
  }
}
