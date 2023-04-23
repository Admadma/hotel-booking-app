package com.application.hotelbooking.exceptions;

public class CredentialMismatchException extends RuntimeException{
    public CredentialMismatchException(String message) {
        super(message);
    }
}
