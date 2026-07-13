package br.com.gustavoakira.flag.shared.web;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import org.springframework.http.HttpStatus;

public record ProblemDetail(
    int status, String title, String detail, String path, Instant timestamp) {
  public static ProblemDetail of(HttpStatus status, String detail, HttpServletRequest request) {

    return new ProblemDetail(
        status.value(), status.getReasonPhrase(), detail, request.getRequestURI(), Instant.now());
  }
}
