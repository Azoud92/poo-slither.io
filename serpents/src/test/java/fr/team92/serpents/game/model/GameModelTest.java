package fr.team92.serpents.game.model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import fr.team92.serpents.utils.Observer;

public class GameModelTest {

    @Test
    public void testAddObserver() {
        GameModel gameModel = new GameModel(10, 10, new ArrayList<>());
        Observer observer = mock(Observer.class);
        gameModel.addObserver(observer);
        gameModel.notifyObservers();
        verify(observer, times(1)).update();
    }

    @Test
    public void testRemoveObserver() {
        GameModel gameModel = new GameModel(10, 10, new ArrayList<>());
        Observer observer = mock(Observer.class);
        gameModel.addObserver(observer);
        gameModel.removeObserver(observer);
        gameModel.notifyObservers();
        verify(observer, times(0)).update();
    }

    @Test
    public void testIsValidPosition() {
        GameModel gameModel = new GameModel(10, 10, new ArrayList<>());
        assertTrue(gameModel.isValidPosition(new Position(5, 5)));
        assertFalse(gameModel.isValidPosition(new Position(10, 10)));
    }

    @Test
    public void testMovePlayer() {
        List<Segment> players = new ArrayList<>();
        players.add(new Segment(new Position(5, 5)));
        GameModel gameModel = new GameModel(10, 10, players);
        gameModel.movePlayer(Direction.NORTH);
        assertEquals(5, gameModel.getCurrentPlayer().getPosition().getX());
        assertEquals(4, gameModel.getCurrentPlayer().getPosition().getY());
    }

    @Test
    public void testAddPlayer() {
        GameModel gameModel = new GameModel(10, 10, new ArrayList<>());
        gameModel.addPlayer(new Segment(new Position(5, 5)));
        assertEquals(1, gameModel.getPlayers().size());
    }

    @Test
    public void testRemovePlayer() {
        List<Segment> players = new ArrayList<>();
        Segment player = new Segment(new Position(5, 5));
        players.add(player);
        GameModel gameModel = new GameModel(10, 10, players);
        gameModel.removePlayer(player);
        assertEquals(0, gameModel.getPlayers().size());
    }
}
