package br.com.gustavoakira.flag.identity.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void shouldCreateValidEmail() {
        Email email = new Email("gustavo@email.com");

        assertEquals("gustavo@email.com", email.value());
    }

    @Test
    void shouldTrimEmail() {
        Email email = new Email("  gustavo@email.com  ");

        assertEquals("gustavo@email.com", email.value());
    }

    @Test
    void shouldThrowWhenEmailIsNull() {
        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> new Email(null)
        );

        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowWhenEmailIsBlank() {
        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> new Email("     ")
        );

        assertEquals("Email cannot be empty", exception.getMessage());
    }

    @Test
    void shouldThrowWhenEmailHasInvalidFormat() {
        DomainRuleException exception = assertThrows(
                DomainRuleException.class,
                () -> new Email("gustavo-email.com")
        );

        assertEquals("Invalid email format", exception.getMessage());
    }

    @Test
    void shouldThrowWhenEmailDoesNotContainDomain() {
        assertThrows(
                DomainRuleException.class,
                () -> new Email("gustavo@")
        );
    }

    @Test
    void shouldThrowWhenEmailDoesNotContainUser() {
        assertThrows(
                DomainRuleException.class,
                () -> new Email("@email.com")
        );
    }

    @Test
    void shouldBeEqualWhenValuesAreEqual() {
        Email email1 = new Email("gustavo@email.com");
        Email email2 = new Email("gustavo@email.com");

        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void shouldNormalizeEmailsBeforeComparing() {
        Email email1 = new Email("  gustavo@email.com");
        Email email2 = new Email("gustavo@email.com  ");

        assertEquals(email1, email2);
    }

    @Test
    void shouldNotBeEqualWhenValuesAreDifferent() {
        Email email1 = new Email("gustavo@email.com");
        Email email2 = new Email("akira@email.com");

        assertNotEquals(email1, email2);
    }

    @Test
    void shouldReturnValueInToString() {
        Email email = new Email("gustavo@email.com");

        assertEquals("Email[value=gustavo@email.com]", email.toString());
    }
}