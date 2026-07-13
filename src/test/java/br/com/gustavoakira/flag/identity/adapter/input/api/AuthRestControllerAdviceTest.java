package br.com.gustavoakira.flag.identity.adapter.input.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import br.com.gustavoakira.flag.identity.application.usecase.exceptions.EmailAlreadyOnUse;
import br.com.gustavoakira.flag.shared.web.ProblemDetail;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class AuthRestControllerAdviceTest {

  private final AuthRestControllerAdvice advice = new AuthRestControllerAdvice();

  @Test
  void shouldReturnConflictProblemDetailWhenEmailAlreadyOnUse() {
    HttpServletRequest request = mock(HttpServletRequest.class);
    when(request.getRequestURI()).thenReturn("/api/v1/auth/register");

    EmailAlreadyOnUse exception = new EmailAlreadyOnUse("Email already in use");

    ResponseEntity<ProblemDetail> response = advice.emailAlreadyOnUse(exception, request);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody()).isNotNull();
    assertThat(response.getBody().status()).isEqualTo(HttpStatus.CONFLICT.value());
    assertThat(response.getBody().detail()).isEqualTo("Email already in use");
    assertThat(response.getBody().path()).isEqualTo("/api/v1/auth/register");
  }
}
