package br.com.gustavoakira.flag.identity.adapter.input.api;

import br.com.gustavoakira.flag.shared.web.ProblemDetail;
import br.com.gustavoakira.flag.identity.application.usecase.exceptions.EmailAlreadyOnUse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {AuthController.class})
public class AuthRestControllerAdvice{
    @ExceptionHandler(EmailAlreadyOnUse.class)
    public ResponseEntity<ProblemDetail> emailAlreadyOnUse(EmailAlreadyOnUse ex, HttpServletRequest request){
        HttpStatus status = HttpStatus.CONFLICT;
        return ResponseEntity.status(status)
                .body(ProblemDetail.of(status, ex.getMessage(), request));
    }
}
