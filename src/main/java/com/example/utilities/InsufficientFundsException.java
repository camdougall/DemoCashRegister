package com.example.utilities;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(Throwable cause) {
        super(cause);
    }

    public InsufficientFundsException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
