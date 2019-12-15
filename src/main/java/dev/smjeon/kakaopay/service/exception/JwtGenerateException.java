package dev.smjeon.kakaopay.service.exception;

public class JwtGenerateException extends RuntimeException {
    public JwtGenerateException() {
        super("지원하지 않는 인코딩입니다.");
    }
}
