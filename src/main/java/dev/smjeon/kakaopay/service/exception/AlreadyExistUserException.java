package dev.smjeon.kakaopay.service.exception;

public class AlreadyExistUserException extends RuntimeException {
    public AlreadyExistUserException() {
        super("이미 존재하는 아이디입니다.");
    }
}
