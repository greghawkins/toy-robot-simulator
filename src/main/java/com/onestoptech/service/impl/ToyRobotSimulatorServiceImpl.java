package com.onestoptech.service.impl;

import com.onestoptech.domain.CardinalDirection;
import com.onestoptech.domain.ToyRobot;
import com.onestoptech.exceptions.CannotMoveForwardException;
import com.onestoptech.exceptions.FacingDirectionInvalidException;
import com.onestoptech.exceptions.InvalidPositionException;
import com.onestoptech.exceptions.PlaceCommandMustBeFirstException;
import com.onestoptech.repository.ToyRobotRepository;
import com.onestoptech.service.ToyRobotSimulatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToyRobotSimulatorServiceImpl implements ToyRobotSimulatorService {
	
	@Autowired
	private ToyRobotRepository toyRobotRepository;

	@Override
	public void left() throws PlaceCommandMustBeFirstException {
		ToyRobot toyRobot = getToyRobot();
		toyRobot.setF(CardinalDirection.valueOf(toyRobot.getF()).getLeft());
		toyRobotRepository.save(toyRobot);
	}

	@Override
	public void right() throws PlaceCommandMustBeFirstException {
		ToyRobot toyRobot = getToyRobot();
		toyRobot.setF(CardinalDirection.valueOf(toyRobot.getF()).getRight());
		toyRobotRepository.save(toyRobot);
	}

	@Override
	public void place(ToyRobot toyRobot) throws FacingDirectionInvalidException, InvalidPositionException {
		validateFValue(toyRobot.getF());
		validatePosition(toyRobot.getX(), toyRobot.getY());
		toyRobotRepository.deleteAll();
		toyRobotRepository.save(toyRobot);
	}

	@Override
	public void move() throws PlaceCommandMustBeFirstException, CannotMoveForwardException {
		ToyRobot toyRobot = getToyRobot();
		boolean movedSucessfully = false;
		if (toyRobot.getF().equals(CardinalDirection.NORTH.toString()) && toyRobot.getY() < 4) {
			toyRobot.setY(toyRobot.getY() + 1);
			movedSucessfully = true;
		}
		else if (toyRobot.getF().equals(CardinalDirection.EAST.toString()) && toyRobot.getX() < 4) {
			toyRobot.setX(toyRobot.getX() + 1);
			movedSucessfully = true;
		}
		else if (toyRobot.getF().equals(CardinalDirection.SOUTH.toString()) && toyRobot.getY() > 0) {
			toyRobot.setY(toyRobot.getY() - 1);
			movedSucessfully = true;
		}
		else if (toyRobot.getF().equals(CardinalDirection.WEST.toString()) && toyRobot.getX() > 0) {
			toyRobot.setX(toyRobot.getX() - 1);
			movedSucessfully = true;
		}
		if (movedSucessfully) {
			toyRobotRepository.save(toyRobot);
		}
		else {
			throw new CannotMoveForwardException();
		}
	}

	@Override
	public String report() throws PlaceCommandMustBeFirstException {
		ToyRobot toyRobot = getToyRobot();
		return toyRobot.getReport();
	}

	private void validateFValue(String f) throws FacingDirectionInvalidException {
		if (Arrays.stream(CardinalDirection.values())
				.map(CardinalDirection::toString)
				.filter(cd -> cd.equals(f))
				.collect(Collectors.toList()).isEmpty()) {
			throw new FacingDirectionInvalidException();
		}
	}

	private void validatePosition(int x, int y) throws InvalidPositionException {
		if (x < 0 || x > 4 || y < 0 || y > 4) {
			throw new InvalidPositionException();
		}
	}

	private ToyRobot getToyRobot() throws PlaceCommandMustBeFirstException {
		List<ToyRobot> toyRobots = toyRobotRepository.findAll();
		if (toyRobots.isEmpty()) {
			throw new PlaceCommandMustBeFirstException();
		}
		return toyRobots.get(0);
	}
}
