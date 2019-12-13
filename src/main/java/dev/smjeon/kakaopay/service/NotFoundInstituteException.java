package dev.smjeon.kakaopay.service;

public class NotFoundInstituteException extends RuntimeException {
    public NotFoundInstituteException() {
        super("존재하지 않는 기관입니다.");
    }
}
