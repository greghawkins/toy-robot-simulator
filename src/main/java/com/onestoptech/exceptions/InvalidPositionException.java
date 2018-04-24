package com.onestoptech.exceptions;

public class InvalidPositionException extends Throwable {
    public InvalidPositionException() {
        super("The position requested is invalid");
    }
}
