package ru.practicum.explore.exception;

public class IntegrityConflictException extends RuntimeException {
    public IntegrityConflictException(String message) {
        super(message);
    }
}
