package com.onstoptech.service;

import com.onestoptech.domain.CardinalDirection;
import com.onestoptech.domain.ToyRobot;
import com.onestoptech.exceptions.CannotMoveForwardException;
import com.onestoptech.exceptions.FacingDirectionInvalidException;
import com.onestoptech.exceptions.InvalidPositionException;
import com.onestoptech.exceptions.PlaceCommandMustBeFirstException;
import com.onestoptech.repository.ToyRobotRepository;
import com.onestoptech.service.impl.ToyRobotSimulatorServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ToyRobotSimulatorServiceTest {

    @Mock
    private ToyRobotRepository toyRobotRepository;
    @InjectMocks
    private ToyRobotSimulatorServiceImpl toyRobotSimulatorService;

    @Test
    public void testLeft() throws PlaceCommandMustBeFirstException {
        assertThatCardinalDirectionMethodWorksAppropriately(true, CardinalDirection.NORTH, CardinalDirection.WEST);
        assertThatCardinalDirectionMethodWorksAppropriately(true, CardinalDirection.EAST, CardinalDirection.NORTH);
        assertThatCardinalDirectionMethodWorksAppropriately(true, CardinalDirection.SOUTH, CardinalDirection.EAST);
        assertThatCardinalDirectionMethodWorksAppropriately(true, CardinalDirection.WEST, CardinalDirection.SOUTH);
    }

    @Test(expected = PlaceCommandMustBeFirstException.class)
    public void testLeftWhereThrowsException() throws PlaceCommandMustBeFirstException {
        when(toyRobotRepository.findAll()).thenReturn(new ArrayList<>());
        toyRobotSimulatorService.left();
    }

    @Test
    public void testRight() throws PlaceCommandMustBeFirstException {
        assertThatCardinalDirectionMethodWorksAppropriately(false, CardinalDirection.NORTH, CardinalDirection.EAST);
        assertThatCardinalDirectionMethodWorksAppropriately(false, CardinalDirection.EAST, CardinalDirection.SOUTH);
        assertThatCardinalDirectionMethodWorksAppropriately(false, CardinalDirection.SOUTH, CardinalDirection.WEST);
        assertThatCardinalDirectionMethodWorksAppropriately(false, CardinalDirection.WEST, CardinalDirection.NORTH);
    }

    @Test(expected = PlaceCommandMustBeFirstException.class)
    public void testRightWhereThrowsException() throws PlaceCommandMustBeFirstException {
        when(toyRobotRepository.findAll()).thenReturn(new ArrayList<>());
        toyRobotSimulatorService.right();
    }

    @Test
    public void testPlaceSuccess()  {
        LongAdder count = new LongAdder();
        count.increment();
        Arrays.stream(CardinalDirection.values()).forEach(cardinalDirection -> {
            getValidPositions(cardinalDirection.toString()).forEach(toyRobot -> {
                when(toyRobotRepository.findAll()).thenReturn(Arrays.asList(toyRobot));
                try {
                    toyRobotSimulatorService.place(toyRobot);
                    //the counter isused here to work out the number of invocations of deleteAll() as this increses with every iteration in the test
                    verify(toyRobotRepository, times(count.intValue())).deleteAll();
                    //this can default to be called 1 time as it is called with toyRobot object which is unique each iteration
                    verify(toyRobotRepository).save(toyRobot);
                    count.increment();
                }
                catch (FacingDirectionInvalidException e) {
                    fail("Test failed: a FacingDirectionInvalidException should not have been thrown");
                }
                catch (InvalidPositionException e) {
                    fail("Test failed: an InvalidPositionException should not have been thrown");
                }
            });
        });
    }

    @Test
    public void testPlaceFailInvalidPositions()  {
        LongAdder count = new LongAdder();
        count.increment();
        Arrays.stream(CardinalDirection.values()).forEach(cardinalDirection -> {
            getInvalidPositions(cardinalDirection).forEach(toyRobot -> {
                when(toyRobotRepository.findAll()).thenReturn(Arrays.asList(toyRobot));
                boolean invalidPositionExceptionThrown = false;
                try {
                    toyRobotSimulatorService.place(toyRobot);
                }
                catch (FacingDirectionInvalidException e) {
                    fail("Test failed: a FacingDirectionInvalidException should not have been thrown");
                }
                catch (InvalidPositionException e) {
                    invalidPositionExceptionThrown = true;
                }
                assertTrue(invalidPositionExceptionThrown);
            });
        });
    }

    @Test
    public void testPlaceFailInvalidFacingDirection()  {
        LongAdder count = new LongAdder();
        count.increment();
        getValidPositions("Invalid Direction").forEach(toyRobot -> {
            when(toyRobotRepository.findAll()).thenReturn(Arrays.asList(toyRobot));
            boolean invalidFacingDirectionInvalidExceptionThrown = false;
            try {
                toyRobotSimulatorService.place(toyRobot);
            }
            catch (FacingDirectionInvalidException e) {
                invalidFacingDirectionInvalidExceptionThrown = true;
            }
            catch (InvalidPositionException e) {
                fail("Test failed: an InvalidPositionException should not have been thrown");
            }
            assertTrue(invalidFacingDirectionInvalidExceptionThrown);
        });
    }

    @Test
    public void testMoveSuccess()  {
        List<ToyRobot> toyRobots = new ArrayList<>();
        List<ToyRobot> expectedToyRobots = new ArrayList<>();
        Arrays.stream(CardinalDirection.values()).forEach(cardinalDirection -> {
            getMovePositions(cardinalDirection, toyRobots, expectedToyRobots);
        });
        assertEquals(64, toyRobots.size());
        for (int i = 0; i < toyRobots.size(); i++) {
            when(toyRobotRepository.findAll()).thenReturn(Arrays.asList(toyRobots.get(i)));
            try {
                toyRobotSimulatorService.move();
                //verify(toyRobotRepository).save(toyRobots.get(i));
                assertEquals(expectedToyRobots.get(i), toyRobots.get(i));
            }
            catch (PlaceCommandMustBeFirstException e) {
                fail("Test failed: a PlaceCommandMustBeFirstException should not have been thrown");
            }
            catch (CannotMoveForwardException e) {
                fail("Test failed: a CannotMoveForwardException should not have been thrown");
            }
        }
    }

    @Test
    public void testMoveFailCannotMoveForwardException()  {
        getPositionsThatCannotMoveFrom().forEach(toyRobot -> {
            when(toyRobotRepository.findAll()).thenReturn(Arrays.asList(toyRobot));
            boolean cannotMoveForwardExceptioThrown = false;
            try {
                toyRobotSimulatorService.move();
            }
            catch (PlaceCommandMustBeFirstException e) {
                fail("Test failed: a PlaceCommandMustBeFirstException should not have been thrown");
            }
            catch (CannotMoveForwardException e) {
                cannotMoveForwardExceptioThrown = true;
            }
            assertTrue(cannotMoveForwardExceptioThrown);
        });
    }

    @Test
    public void testMoveFailPlaceCommandMustBeFirstException()  {
        when(toyRobotRepository.findAll()).thenReturn(new ArrayList<>());
        boolean placeCommandMustBeFirstExceptionThrown = false;
            try {
                toyRobotSimulatorService.move();
            }
            catch (PlaceCommandMustBeFirstException e) {
                placeCommandMustBeFirstExceptionThrown = true;
            }
            catch (CannotMoveForwardException e) {
                fail("Test failed: a CannotMoveForwardException should not have been thrown");
            }
            assertTrue(placeCommandMustBeFirstExceptionThrown);
    }

    @Test
    public void testReportSuccess() throws PlaceCommandMustBeFirstException  {
        ToyRobot toyRobot = new ToyRobot(3,4, CardinalDirection.EAST.toString());
        when(toyRobotRepository.findAll()).thenReturn(Arrays.asList(toyRobot));

        String result = toyRobotSimulatorService.report();

        assertEquals("Output: 3,4,EAST", result);
    }

    @Test
    public void testReportFailPlaceCommandMustBeFirstException()  {
        when(toyRobotRepository.findAll()).thenReturn(new ArrayList<>());
        boolean placeCommandMustBeFirstExceptionThrown = false;
        try {
            toyRobotSimulatorService.report();
        }
        catch (PlaceCommandMustBeFirstException e) {
            placeCommandMustBeFirstExceptionThrown = true;
        }
        assertTrue(placeCommandMustBeFirstExceptionThrown);
    }

    private void assertThatCardinalDirectionMethodWorksAppropriately(boolean methodTypeLeft,
                                                                     CardinalDirection startDirection,
                                                                     CardinalDirection resultDirection) throws PlaceCommandMustBeFirstException {
        ToyRobot robot = new ToyRobot(0, 0, startDirection.toString());
        assertEquals(startDirection.toString(), robot.getF());
        when(toyRobotRepository.findAll()).thenReturn(Arrays.asList(robot));
        if (methodTypeLeft) {
            toyRobotSimulatorService.left();
        }
        else {
            toyRobotSimulatorService.right();
        }
        assertEquals(resultDirection.toString(), robot.getF());
    }

    private void getMovePositions(CardinalDirection cardinalDirection, List<ToyRobot> toyRobots, List<ToyRobot> expectedToyRobots) {
        if (CardinalDirection.NORTH.equals(cardinalDirection) || CardinalDirection.EAST.equals(cardinalDirection)) {
            for (int xPos = 0; xPos < 4; xPos++) {
                for (int yPos = 0; yPos < 4; yPos++) {
                    toyRobots.add(new ToyRobot(xPos, yPos, cardinalDirection.toString()));
                    if (CardinalDirection.NORTH.equals(cardinalDirection)) {
                        expectedToyRobots.add(new ToyRobot(xPos, yPos + 1, cardinalDirection.toString()));
                    } else if (CardinalDirection.EAST.equals(cardinalDirection)) {
                        expectedToyRobots.add(new ToyRobot(xPos + 1, yPos, cardinalDirection.toString()));
                    }

                }
            }
        }
        else if (CardinalDirection.SOUTH.equals(cardinalDirection) || CardinalDirection.WEST.equals(cardinalDirection)) {
            for (int xPos = 4; xPos > 0; xPos--) {
                for (int yPos = 4; yPos > 0; yPos--) {
                    toyRobots.add(new ToyRobot(xPos, yPos, cardinalDirection.toString()));
                    if (CardinalDirection.SOUTH.equals(cardinalDirection)) {
                        expectedToyRobots.add(new ToyRobot(xPos, yPos-1, cardinalDirection.toString()));
                    }
                    else if (CardinalDirection.WEST.equals(cardinalDirection)) {
                        expectedToyRobots.add(new ToyRobot(xPos-1, yPos, cardinalDirection.toString()));
                    }
                }
            }
        }
    }

    private List<ToyRobot> getInvalidPositions(CardinalDirection cardinalDirection) {
        return Arrays.asList(
                new ToyRobot(5, 0, cardinalDirection.toString()),
                new ToyRobot(5, 1, cardinalDirection.toString()),
                new ToyRobot(5, 2, cardinalDirection.toString()),
                new ToyRobot(5, 3, cardinalDirection.toString()),
                new ToyRobot(5, 4, cardinalDirection.toString()),
                new ToyRobot(6, 0, cardinalDirection.toString()),
                new ToyRobot(6, 1, cardinalDirection.toString()),
                new ToyRobot(6, 2, cardinalDirection.toString()),
                new ToyRobot(6, 3, cardinalDirection.toString()),
                new ToyRobot(6, 4, cardinalDirection.toString()),
                new ToyRobot(0, 5, cardinalDirection.toString()),
                new ToyRobot(1, 5, cardinalDirection.toString()),
                new ToyRobot(2, 5, cardinalDirection.toString()),
                new ToyRobot(3, 5, cardinalDirection.toString()),
                new ToyRobot(4, 5, cardinalDirection.toString()),
                new ToyRobot(0, 6, cardinalDirection.toString()),
                new ToyRobot(1, 6, cardinalDirection.toString()),
                new ToyRobot(2, 6, cardinalDirection.toString()),
                new ToyRobot(3, 6, cardinalDirection.toString()),
                new ToyRobot(4, 6, cardinalDirection.toString())
        );
    }

    private List<ToyRobot> getPositionsThatCannotMoveFrom() {
        return Arrays.asList(
                new ToyRobot(0, 4, CardinalDirection.NORTH.toString()),
                new ToyRobot(1, 4, CardinalDirection.NORTH.toString()),
                new ToyRobot(2, 4, CardinalDirection.NORTH.toString()),
                new ToyRobot(3, 4, CardinalDirection.NORTH.toString()),
                new ToyRobot(4, 4, CardinalDirection.NORTH.toString()),
                new ToyRobot(4, 0, CardinalDirection.EAST.toString()),
                new ToyRobot(4, 1, CardinalDirection.EAST.toString()),
                new ToyRobot(4, 2, CardinalDirection.EAST.toString()),
                new ToyRobot(4, 3, CardinalDirection.EAST.toString()),
                new ToyRobot(4, 4, CardinalDirection.EAST.toString()),
                new ToyRobot(0, 0, CardinalDirection.SOUTH.toString()),
                new ToyRobot(1, 0, CardinalDirection.SOUTH.toString()),
                new ToyRobot(2, 0, CardinalDirection.SOUTH.toString()),
                new ToyRobot(3, 0, CardinalDirection.SOUTH.toString()),
                new ToyRobot(4, 0, CardinalDirection.SOUTH.toString()),
                new ToyRobot(0, 0, CardinalDirection.WEST.toString()),
                new ToyRobot(0, 1, CardinalDirection.WEST.toString()),
                new ToyRobot(0, 2, CardinalDirection.WEST.toString()),
                new ToyRobot(0, 3, CardinalDirection.WEST.toString()),
                new ToyRobot(0, 4, CardinalDirection.WEST.toString())
        );
    }

    private List<ToyRobot> getValidPositions(String cardinalDirection) {
        List<ToyRobot> toyRobots = new ArrayList<>();
        for (int xPos = 0; xPos < 5; xPos++) {
            for (int yPos = 0; yPos < 5; yPos++) {
                toyRobots.add(new ToyRobot(xPos, yPos, cardinalDirection));
            }
        }
        return toyRobots;
    }

}
