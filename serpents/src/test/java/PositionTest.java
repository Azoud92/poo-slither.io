import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    public void testMove() {
        Position pos = new Position(0, 0);
        Direction dir = new Direction(45); // direction at 45 degrees
        Position newPos = pos.move(dir, Math.sqrt(2));

        assertEquals(1, newPos.x(), 0.001);
        assertEquals(1, newPos.y(), 0.001);
    }

    @Test
    public void testDistanceTo() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(3, 4);
        double distance = pos1.distanceTo(pos2);

        assertEquals(5, distance, 0.001);
    }

    @Test
    public void testInter() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(2, 2);
        Position interPos = pos1.inter(pos2, 0.5);

        assertEquals(1, interPos.x(), 0.001);
        assertEquals(1, interPos.y(), 0.001);
    }

    @Test
    public void testAngleTo() {
        Position pos1 = new Position(0, 0);
        Position pos2 = new Position(1, 1);
        Direction direction = pos1.angleTo(pos2);

        assertEquals(45, direction.getAngle(), 0.001);
    }

    @Test
    public void testToJSON() {
        Position pos = new Position(10, 20);
        JsonObject json = pos.toJSON();

        assertEquals("position", json.get("type").getAsString());
        assertEquals(10, json.get("x").getAsDouble(), 0.001);
        assertEquals(20, json.get("y").getAsDouble(), 0.001);
    }
}