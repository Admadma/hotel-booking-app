package com.application.hotelbooking.exceptions;

public class InvalidRoomException extends RuntimeException{
    public InvalidRoomException(String message) {
        super(message);
    }
}
