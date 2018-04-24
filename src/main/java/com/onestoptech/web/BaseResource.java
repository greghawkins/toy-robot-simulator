package com.onestoptech.web;

import com.onestoptech.domain.ResponseMessage;
import com.onestoptech.exceptions.CannotMoveForwardException;
import com.onestoptech.exceptions.FacingDirectionInvalidException;
import com.onestoptech.exceptions.InvalidPositionException;
import com.onestoptech.exceptions.PlaceCommandMustBeFirstException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class BaseResource {

    @ExceptionHandler({ FacingDirectionInvalidException.class, PlaceCommandMustBeFirstException.class, CannotMoveForwardException.class, InvalidPositionException.class })
    public ResponseEntity<ResponseMessage> handleException(Exception e) {
        return new ResponseEntity<>(new ResponseMessage(e.getCause() != null ? e.getCause().getMessage() : e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
