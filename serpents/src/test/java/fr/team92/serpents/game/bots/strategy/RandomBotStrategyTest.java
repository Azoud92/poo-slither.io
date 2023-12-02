package fr.team92.serpents.game.bots.strategy;

import fr.team92.serpents.game.model.*;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class RandomBotStrategyTest {

    @Test
    public void testGetNextMoveAllDirectionsSafe() {
        GameModel model = mock(GameModel.class);
        Segment segment = new Segment(new Position(5, 5));
        RandomBotStrategy strategy = new RandomBotStrategy();

        when(model.isValidPosition(any())).thenReturn(true);
        when(model.isOccupied(any())).thenReturn(false);

        Direction direction = strategy.getNextMove(model, segment);
        assertNotNull(direction);
    }

    @Test
    public void testGetNextMoveNoSafeDirections() {
        GameModel model = mock(GameModel.class);
        Segment segment = new Segment(new Position(5, 5));
        RandomBotStrategy strategy = new RandomBotStrategy();

        when(model.isValidPosition(any())).thenReturn(false);

        Direction direction = strategy.getNextMove(model, segment);
        assertNotNull(direction);
    }

    @Test
    public void testGetNextMoveSomeSafeDirections() {
        GameModel model = mock(GameModel.class);
        Segment segment = new Segment(new Position(5, 5));
        RandomBotStrategy strategy = new RandomBotStrategy();

        when(model.isValidPosition(any())).thenReturn(true);
        when(model.isOccupied(any())).thenAnswer(invocation -> {
            Position position = invocation.getArgument(0);
            return position.equals(new Position(5, 6));
        });

        Direction direction = strategy.getNextMove(model, segment);
        assertNotNull(direction);
    }
}