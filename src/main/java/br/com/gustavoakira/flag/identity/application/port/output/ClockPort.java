package br.com.gustavoakira.flag.identity.application.port.output;

import java.time.LocalDateTime;

public interface ClockPort {
  LocalDateTime now();
}
