package com.onestoptech.exceptions;

public class PlaceCommandMustBeFirstException extends Exception {
    public PlaceCommandMustBeFirstException() {
        super("You must run the 'Place' command first");
    }
}
