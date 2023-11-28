package fr.team92.serpents.game.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SegmentTest {

    @Test
    public void testMoveNorth() {
        Segment segment = new Segment(new Position(5, 5));
        segment.move(Direction.NORTH);
        assertEquals(5, segment.getPosition().getX());
        assertEquals(4, segment.getPosition().getY());
    }

    @Test
    public void testMoveSouth() {
        Segment segment = new Segment(new Position(5, 5));
        segment.move(Direction.SOUTH);
        assertEquals(5, segment.getPosition().getX());
        assertEquals(6, segment.getPosition().getY());
    }

    @Test
    public void testMoveEast() {
        Segment segment = new Segment(new Position(5, 5));
        segment.move(Direction.EAST);
        assertEquals(6, segment.getPosition().getX());
        assertEquals(5, segment.getPosition().getY());
    }

    @Test
    public void testMoveWest() {
        Segment segment = new Segment(new Position(5, 5));
        segment.move(Direction.WEST);
        assertEquals(4, segment.getPosition().getX());
        assertEquals(5, segment.getPosition().getY());
    }

    @Test
    public void testSimulateMoveNorth() {
        Segment segment = new Segment(new Position(5, 5));
        Position newPosition = segment.simulateMove(Direction.NORTH);
        assertEquals(5, newPosition.getX());
        assertEquals(4, newPosition.getY());
    }

    @Test
    public void testSimulateMoveSouth() {
        Segment segment = new Segment(new Position(5, 5));
        Position newPosition = segment.simulateMove(Direction.SOUTH);
        assertEquals(5, newPosition.getX());
        assertEquals(6, newPosition.getY());
    }

    @Test
    public void testSimulateMoveEast() {
        Segment segment = new Segment(new Position(5, 5));
        Position newPosition = segment.simulateMove(Direction.EAST);
        assertEquals(6, newPosition.getX());
        assertEquals(5, newPosition.getY());
    }

    @Test
    public void testSimulateMoveWest() {
        Segment segment = new Segment(new Position(5, 5));
        Position newPosition = segment.simulateMove(Direction.WEST);
        assertEquals(4, newPosition.getX());
        assertEquals(5, newPosition.getY());
    }
}