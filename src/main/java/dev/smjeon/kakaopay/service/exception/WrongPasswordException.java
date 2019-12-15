package dev.smjeon.kakaopay.service.exception;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super("잘못된 패스워드입니다.");
    }
}
