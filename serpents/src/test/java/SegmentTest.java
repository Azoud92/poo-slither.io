// FILEPATH: /c:/Users/evanj/projet-cpo-2023-2024/serpents/src/test/java/SegmentTest.java

import org.junit.jupiter.api.Test;

import com.google.gson.JsonObject;

import fr.team92.serpents.snake.model.segments.NormalSegmentBehavior;
import fr.team92.serpents.snake.model.segments.Segment;
import fr.team92.serpents.snake.model.segments.SegmentBehavior;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;

import static org.junit.jupiter.api.Assertions.*;

public class SegmentTest {

    @Test
    public void testConstructor() {
        Position position = new Position(0, 0);
        SegmentBehavior behavior = new NormalSegmentBehavior();
        Segment segment = new Segment(position, 1.0, behavior);

        assertEquals(position, segment.getPosition());
        assertEquals(1.0, segment.getDiameter(), 0.001);
        assertEquals(behavior, segment.getBehavior());
        assertFalse(segment.isDead());
    }

    @Test
    public void testMove() {
        Position position = new Position(0, 0);
        SegmentBehavior behavior = new NormalSegmentBehavior();
        Segment segment = new Segment(position, 1.0, behavior);

        segment.move(new Direction(0), 1.0);

        assertEquals(1.0, segment.getPosition().x(), 0.001);
        assertEquals(0.0, segment.getPosition().y(), 0.001);
    }

    @Test
    public void testDie() {
        Position position = new Position(0, 0);
        SegmentBehavior behavior = new NormalSegmentBehavior();
        Segment segment = new Segment(position, 1.0, behavior);

        segment.die();

        assertTrue(segment.isDead());
    }

    @Test
    public void testIsInCollision() {
        Position position1 = new Position(0, 0);
        Position position2 = new Position(0, 0);
        SegmentBehavior behavior = new NormalSegmentBehavior();
        Segment segment1 = new Segment(position1, 1.0, behavior);
        Segment segment2 = new Segment(position2, 1.0, behavior);

        assertTrue(segment1.isInCollision(segment2));
    }

    @Test
    public void testToJSON() {
        Position position = new Position(0, 0);
        SegmentBehavior behavior = new NormalSegmentBehavior();
        Segment segment = new Segment(position, 1.0, behavior);

        JsonObject json = segment.toJSON();

        assertEquals("segment", json.get("type").getAsString());
        assertEquals(0.0, json.get("position").getAsJsonObject().get("x").getAsDouble(), 0.001);
        assertEquals(1.0, json.get("diameter").getAsDouble(), 0.001);
        assertEquals("normal", json.get("behavior").getAsString());
    }
}