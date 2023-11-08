package com.application.hotelbooking.exceptions;

public class InvalidHotelException extends RuntimeException{
    public InvalidHotelException(String message) {
        super(message);
    }
}
