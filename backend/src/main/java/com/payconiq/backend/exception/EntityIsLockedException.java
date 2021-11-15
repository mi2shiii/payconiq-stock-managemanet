package com.payconiq.backend.exception;

public class EntityIsLockedException extends RuntimeException {
    public EntityIsLockedException(String message) {
        super(message);
    }
}
