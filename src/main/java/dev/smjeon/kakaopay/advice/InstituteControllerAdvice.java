package dev.smjeon.kakaopay.advice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class InstituteControllerAdvice {
    private static final Logger logger = LoggerFactory.getLogger(InstituteControllerAdvice.class);

    @ExceptionHandler({ConstraintViolationException.class, DataIntegrityViolationException.class})
    public ResponseEntity responseExceptionMessage(Exception e) {
        logger.error("SQL Exception", e);
        return ResponseEntity.badRequest().body("잘못된 요청입니다.");
    }

}