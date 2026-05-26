package nl.davefemi.webchess.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @ExceptionHandler(UnauthorizedException.class)
//    public ProblemDetail handleUnauthorizedExceptions(UnauthorizedException ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
//    }
//
//    @ExceptionHandler(SessionException.class)
//    public ProblemDetail handleSessionExceptions(SessionException ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ProblemDetail handleAllExceptions(Exception ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
//    }
}

