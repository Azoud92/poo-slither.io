package fr.team92.serpents.game.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testGetX() {
        Position position = new Position(5, 10);
        assertEquals(5, position.getX());
    }

    @Test
    public void testSetX() {
        Position position = new Position(5, 10);
        position.setX(15);
        assertEquals(15, position.getX());
    }

    @Test
    public void testGetY() {
        Position position = new Position(5, 10);
        assertEquals(10, position.getY());
    }

    @Test
    public void testSetY() {
        Position position = new Position(5, 10);
        position.setY(20);
        assertEquals(20, position.getY());
    }

    @Test
    public void testClone() {
        Position position1 = new Position(5, 10);
        Position position2 = position1.clone();
        assertNotSame(position1, position2);
        assertEquals(position1.getX(), position2.getX());
        assertEquals(position1.getY(), position2.getY());
    }
}