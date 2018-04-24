package com.onestoptech.exceptions;

public class CannotMoveForwardException extends Throwable {
    public CannotMoveForwardException() {
        super("I cannot move forward or I will fall off the tabletop");
    }
}
