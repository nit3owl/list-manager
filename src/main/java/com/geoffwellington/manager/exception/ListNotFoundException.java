package com.geoffwellington.manager.exception;

public class ListNotFoundException extends ResourceNotFoundException {
    public ListNotFoundException(String message) {
        super(message);
    }

    public ListNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
