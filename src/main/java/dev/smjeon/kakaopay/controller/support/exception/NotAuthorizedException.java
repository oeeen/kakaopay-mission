package dev.smjeon.kakaopay.controller.support.exception;

public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException() {
        super("권한이 없는 요청입니다.");
    }
}
