package dev.smjeon.kakaopay.service.exception;

public class NotFoundMaxAmountException extends RuntimeException {
    public NotFoundMaxAmountException() {
        super("최대 값을 찾을 수 없습니다.");
    }
}
