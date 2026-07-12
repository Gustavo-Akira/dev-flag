package br.com.gustavoakira.flag.identity.adapter.output.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BcryptCryptographyAdapterTest {

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private BcryptCryptographyAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new BcryptCryptographyAdapter(passwordEncoder);
    }

    @Test
    void shouldHashString() {
        String password = "myPassword";
        String hash = "$2a$10$hashedPassword";

        when(passwordEncoder.encode(password)).thenReturn(hash);

        String result = adapter.hash(password);

        assertEquals(hash, result);
        verify(passwordEncoder).encode(password);
    }

    @Test
    void shouldReturnTrueWhenPasswordsMatch() {
        String password = "myPassword";
        String hash = "$2a$10$hashedPassword";

        when(passwordEncoder.matches(password, hash)).thenReturn(true);

        boolean result = adapter.compare(password, hash);

        assertTrue(result);
        verify(passwordEncoder).matches(password, hash);
    }

    @Test
    void shouldReturnFalseWhenPasswordsDoNotMatch() {
        String password = "myPassword";
        String hash = "$2a$10$hashedPassword";

        when(passwordEncoder.matches(password, hash)).thenReturn(false);

        boolean result = adapter.compare(password, hash);

        assertFalse(result);
        verify(passwordEncoder).matches(password, hash);
    }
}