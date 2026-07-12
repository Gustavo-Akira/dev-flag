package br.com.gustavoakira.flag.identity.adapter.output.id;

import br.com.gustavoakira.flag.identity.domain.UserId;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class IdGeneratorAdapterTest {

    private final IdGeneratorAdapter adapter = new IdGeneratorAdapter();

    @Test
    void shouldGenerateUserId() {
        UserId id = adapter.generateUserId();

        assertNotNull(id);
    }

    @Test
    void shouldGenerateDifferentIds() {
        UserId first = adapter.generateUserId();
        UserId second = adapter.generateUserId();

        assertNotEquals(first, second);
    }

    @Test
    void shouldGenerateValidUuid() {
        UserId id = adapter.generateUserId();
        assertNotNull(id.value());
    }

    @Test
    void shouldGenerateUuidVersion7() {
        UserId id = adapter.generateUserId();
        assertEquals(7, id.value().version());
    }
}