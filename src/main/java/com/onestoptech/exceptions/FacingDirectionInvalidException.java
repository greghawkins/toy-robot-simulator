package com.onestoptech.exceptions;

public class FacingDirectionInvalidException extends Exception {
    public FacingDirectionInvalidException() {
        super("The value submitted for 'f' is invalid. Must be one of: NORTH, EAST, SOUTH, WEST");
    }
}
