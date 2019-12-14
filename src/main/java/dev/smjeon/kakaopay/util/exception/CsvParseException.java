package dev.smjeon.kakaopay.util.exception;

public class CsvParseException extends RuntimeException {
    public CsvParseException(Exception e) {
        super(e);
    }
}
