import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;
import fr.team92.serpents.utils.Direction;
import static org.junit.jupiter.api.Assertions.*;

public class DirectionTest {

    @Test
    public void testConstructor() {
        Direction dir = new Direction(370);
        assertEquals(10, dir.getAngle(), 0.001);
    }

    @Test
    public void testOpposite() {
        Direction dir = new Direction(45);
        Direction opp = dir.opposite();
        assertEquals(225, opp.getAngle(), 0.001);
    }

    @Test
    public void testRandom() {
        Direction dir = Direction.random();
        assertTrue(dir.getAngle() >= 0 && dir.getAngle() < 360);
    }

    @Test
    public void testSetAngle() {
        Direction dir = new Direction(45);
        dir.setAngle(370);
        assertEquals(10, dir.getAngle(), 0.001);
    }

    @Test
    public void testClone() {
        Direction dir1 = new Direction(45);
        Direction dir2 = dir1.clone();
        assertEquals(dir1.getAngle(), dir2.getAngle(), 0.001);
    }

    @Test
    public void testToJSON() {
        Direction dir = new Direction(90);
        JsonObject json = dir.toJSON();

        assertEquals("direction", json.get("type").getAsString());
        assertEquals(90, json.get("angle").getAsDouble(), 0.001);
    }
}