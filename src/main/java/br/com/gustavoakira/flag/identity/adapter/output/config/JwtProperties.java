package br.com.gustavoakira.flag.identity.adapter.output.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(String secret, Duration expiration) {}
