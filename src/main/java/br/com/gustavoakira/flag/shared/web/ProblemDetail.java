package br.com.gustavoakira.flag.shared.web;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public record ProblemDetail(
        int status,
        String title,
        String detail,
        String path,
        Instant timestamp
){
    public static ProblemDetail of(
            HttpStatus status,
            String detail,
            HttpServletRequest request) {

        return new ProblemDetail(
                status.value(),
                status.getReasonPhrase(),
                detail,
                request.getRequestURI(),
                Instant.now()
        );
    }
}
