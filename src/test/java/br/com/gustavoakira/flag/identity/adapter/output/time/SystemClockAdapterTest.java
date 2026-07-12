package br.com.gustavoakira.flag.identity.adapter.output.time;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.assertThat;

class SystemClockAdapterTest {

    @Test
    void shouldReturnTimeFromClock() {
        Clock clock = Clock.fixed(
                Instant.parse("2026-07-11T14:30:00Z"),
                ZoneOffset.UTC
        );

        SystemClockAdapter adapter = new SystemClockAdapter(clock);

        assertThat(adapter.now())
                .isEqualTo(LocalDateTime.of(2026, 7, 11, 14, 30));
    }
}