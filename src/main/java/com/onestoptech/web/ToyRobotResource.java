package com.onestoptech.web;

import com.onestoptech.domain.ResponseMessage;
import com.onestoptech.domain.ToyRobot;
import com.onestoptech.exceptions.CannotMoveForwardException;
import com.onestoptech.exceptions.FacingDirectionInvalidException;
import com.onestoptech.exceptions.InvalidPositionException;
import com.onestoptech.exceptions.PlaceCommandMustBeFirstException;
import com.onestoptech.service.ToyRobotSimulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/toy-robot")
public class ToyRobotResource extends BaseResource {

	@Autowired
	private ToyRobotSimulatorService toyRobotService;

    @RequestMapping(value = "/place", method = RequestMethod.POST)
    public ResponseEntity<ToyRobot> place(@RequestBody ToyRobot toyRobot) throws FacingDirectionInvalidException, InvalidPositionException {
        toyRobotService.place(toyRobot);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(value = "/left", method = RequestMethod.PUT)
    public ResponseEntity left() throws PlaceCommandMustBeFirstException {
        toyRobotService.left();
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/right", method = RequestMethod.PUT)
    public ResponseEntity right() throws PlaceCommandMustBeFirstException {
        toyRobotService.right();
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/move", method = RequestMethod.PUT)
    public ResponseEntity move() throws PlaceCommandMustBeFirstException, CannotMoveForwardException {
        toyRobotService.move();
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping(value = "/report", method = RequestMethod.GET)
    public ResponseEntity<ResponseMessage> report() throws PlaceCommandMustBeFirstException {
        String report = toyRobotService.report();
        return new ResponseEntity<>(new ResponseMessage(report), HttpStatus.OK);
    }
}
