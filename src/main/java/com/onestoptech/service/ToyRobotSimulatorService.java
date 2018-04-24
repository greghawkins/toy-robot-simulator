package com.onestoptech.service;

import com.onestoptech.domain.ToyRobot;
import com.onestoptech.exceptions.CannotMoveForwardException;
import com.onestoptech.exceptions.FacingDirectionInvalidException;
import com.onestoptech.exceptions.InvalidPositionException;
import com.onestoptech.exceptions.PlaceCommandMustBeFirstException;

public interface ToyRobotSimulatorService {
	void left() throws PlaceCommandMustBeFirstException;
	void right() throws PlaceCommandMustBeFirstException;
	void place(ToyRobot toyRobot) throws FacingDirectionInvalidException, InvalidPositionException;
	void move() throws PlaceCommandMustBeFirstException, CannotMoveForwardException;
	String report() throws PlaceCommandMustBeFirstException;
}
