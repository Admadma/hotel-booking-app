package com.application.hotelbooking.exceptions;

public class CancellationErrorException extends RuntimeException{
    public CancellationErrorException(String message) {
        super(message);
    }
}
