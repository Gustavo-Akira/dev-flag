package br.com.gustavoakira.flag.identity.adapter.output.time;

import br.com.gustavoakira.flag.identity.application.port.output.ClockPort;
import java.time.Clock;
import java.time.LocalDateTime;
import org.springframework.stereotype.Component;

@Component
public class SystemClockAdapter implements ClockPort {
  private final Clock clock;

  public SystemClockAdapter(Clock clock) {
    this.clock = clock;
  }

  @Override
  public LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}
