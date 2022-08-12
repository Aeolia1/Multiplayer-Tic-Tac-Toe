package edu.uob;
import edu.uob.OXOMoveException.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// PLEASE READ:
// The tests in this file will fail by default for a template skeleton, your job is to pass them
// and maybe write some more, read up on how to write tests at
// https://junit.org/junit5/docs/current/user-guide/#writing-tests
final class ControllerTests {
    OXOModel model;
    OXOModel model2;
    OXOController controller;
    OXOController controller2;

    // create your standard 3*3 OXO board (where three of the same symbol in a line wins) with the X
    // and O player
    private static OXOModel createStandardModel() {
        OXOModel model = new OXOModel(3, 3, 3);
        model.addPlayer(new OXOPlayer('X'));
        model.addPlayer(new OXOPlayer('O'));
        return model;
    }

    // we make a new board for every @Test (i.e. this method runs before every @Test test case)
    @BeforeEach
    void setup() {
        model = createStandardModel();
        controller = new OXOController(model);
    }


    // here's a basic test for the `controller.handleIncomingCommand` method
    @Test
    void testHandleIncomingCommand() throws OXOMoveException {
        // take note of whose gonna made the first move
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        controller.handleIncomingCommand("a1");
        OXOPlayer secondMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        controller.handleIncomingCommand("b1");

        // A move has been made for A1 (i.e. the [0,0] cell on the board), let's see if that cell is
        // indeed owned by the player
        assertEquals(firstMovingPlayer, controller.gameModel.getCellOwner(0, 0));
        assertEquals(secondMovingPlayer, controller.gameModel.getCellOwner(1, 0));
        //test for input length
        assertThrows(InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("aa1"));
        assertThrows(InvalidIdentifierLengthException.class, ()-> controller.handleIncomingCommand("123456"));
        //test for input type
        assertThrows(InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("SS"));
        assertThrows(InvalidIdentifierCharacterException.class, ()-> controller.handleIncomingCommand("11"));
        //test for case equality
        assertThrows(CellAlreadyTakenException.class, ()-> controller.handleIncomingCommand("A1"));
        assertThrows(CellAlreadyTakenException.class, ()-> controller.handleIncomingCommand("B1"));
        //test for invalid cell range
        assertThrows(OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("I0"));
        assertThrows(OutsideCellRangeException.class, ()-> controller.handleIncomingCommand("H1"));
    }

    // here's a complete game where we find out if someone won
    @Test
    void testBasicWinWithA1A2A3() throws OXOMoveException {
        // take note of whose gonna made the first move
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        controller.handleIncomingCommand("a1");
        controller.handleIncomingCommand("b1");
        controller.handleIncomingCommand("a2");
        controller.handleIncomingCommand("b2");
        controller.handleIncomingCommand("a3");

        // OK, so A1, A2, A3 is a win and that last A3 move is made by the first player (players
        // alternative between moves) let's make an assertion to see whether the first moving player is
        // the winner here
        assertEquals(
                firstMovingPlayer,
                model.getWinner(),
                "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
    }

    @Test
    void testGameWinVertical() throws OXOMoveException {
        // take note of whose gonna made the first move
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        controller.handleIncomingCommand("a1");
        controller.handleIncomingCommand("a2");
        controller.handleIncomingCommand("b1");
        controller.handleIncomingCommand("b2");
        controller.handleIncomingCommand("c1");

        assertEquals(
                firstMovingPlayer,
                model.getWinner(),
                "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
    }

    @Test
    void testGameWinDiagonal1() throws OXOMoveException {
        //left to right
        // take note of whose gonna made the first move
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        controller.handleIncomingCommand("a1");
        controller.handleIncomingCommand("a2");
        controller.handleIncomingCommand("b2");
        controller.handleIncomingCommand("b3");
        controller.handleIncomingCommand("c3");

        assertEquals(
                firstMovingPlayer,
                model.getWinner(),
                "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
    }
    @Test
    void testGameWinDiagonal2() throws OXOMoveException {
        //left to right
        // take note of whose gonna won the game
        OXOPlayer winner = model.getPlayerByNumber(model.getCurrentPlayerNumber()+1);
        controller.handleIncomingCommand("a1");
        controller.handleIncomingCommand("c1");
        controller.handleIncomingCommand("b1");
        controller.handleIncomingCommand("b2");
        controller.handleIncomingCommand("c3");
        controller.handleIncomingCommand("A3");

        assertEquals(
                winner,
                model.getWinner(),
                "Winner was expected to be %s but wasn't".formatted(winner.getPlayingLetter()));
    }

    @Test
    void testGameDrawn() throws OXOMoveException {
        controller.handleIncomingCommand("a1");
        controller.handleIncomingCommand("b1");
        controller.handleIncomingCommand("a2");
        controller.handleIncomingCommand("b2");
        controller.handleIncomingCommand("C1");
        controller.handleIncomingCommand("A3");
        controller.handleIncomingCommand("B3");
        controller.handleIncomingCommand("C2");
        controller.handleIncomingCommand("C3");
        assertTrue(model.isGameDrawn());
    }

    @Test
    void testGameWinWhenFull() throws OXOMoveException {
        // take note of whose gonna made the first move
        OXOPlayer firstMovingPlayer = model.getPlayerByNumber(model.getCurrentPlayerNumber());
        controller.handleIncomingCommand("a1");
        controller.handleIncomingCommand("b1");
        controller.handleIncomingCommand("b2");
        controller.handleIncomingCommand("a2");
        controller.handleIncomingCommand("c2");
        controller.handleIncomingCommand("c1");
        controller.handleIncomingCommand("B3");
        controller.handleIncomingCommand("a3");
        controller.handleIncomingCommand("C3");

        assertEquals(
                firstMovingPlayer,
                model.getWinner(),
                "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
        //game drawn must be false when winner appears
        assertFalse(model.isGameDrawn());
    }

    private OXOModel createModel2() {
        OXOModel model2 = new OXOModel(4, 4, 2);
        model2.addPlayer(new OXOPlayer('X'));
        model2.addPlayer(new OXOPlayer('O'));
        model2.addPlayer(new OXOPlayer('I'));
        return model2;
    }
    @BeforeEach
    void setup2() {
        this.model2 = createModel2();
        controller2 = new OXOController(model2);
    }

    @Test
    void testBasicWin() throws OXOMoveException {
        // take note of whose gonna made the first move
        OXOPlayer firstMovingPlayer = model2.getPlayerByNumber(model2.getCurrentPlayerNumber());
        controller2.handleIncomingCommand("a1");
        controller2.handleIncomingCommand("b1");
        OXOPlayer thirdMovingPlayer = model2.getPlayerByNumber(model2.getCurrentPlayerNumber());
        controller2.handleIncomingCommand("c1");
        controller2.handleIncomingCommand("a2");

        // OK, so A1, A2, A3 is a win and that last A3 move is made by the first player (players
        // alternative between moves) let's make an assertion to see whether the first moving player is
        // the winner here
        assertEquals(
                firstMovingPlayer,
                model2.getWinner(),
                "Winner was expected to be %s but wasn't".formatted(firstMovingPlayer.getPlayingLetter()));
        assertEquals(4,model2.getNumberOfColumns());
        assertEquals(4,model2.getNumberOfRows());
        assertEquals(2,model2.getWinThreshold());
        assertEquals('I',thirdMovingPlayer.getPlayingLetter());
    }


}
