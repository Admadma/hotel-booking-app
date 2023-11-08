package com.application.hotelbooking.exceptions;

public class OutdatedReservationException extends RuntimeException{
    public OutdatedReservationException(String message) {
        super(message);
    }
}
