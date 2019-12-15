package dev.smjeon.kakaopay.advice;

import dev.smjeon.kakaopay.service.exception.AlreadyExistUserException;
import dev.smjeon.kakaopay.service.exception.NotFoundUserException;
import dev.smjeon.kakaopay.service.exception.WrongPasswordException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserApiControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(UserApiControllerAdvice.class);

    @ExceptionHandler({AlreadyExistUserException.class, NotFoundUserException.class, WrongPasswordException.class})
    public ResponseEntity responseExceptionMessage(Exception e) {
        logger.error("User Exception", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}