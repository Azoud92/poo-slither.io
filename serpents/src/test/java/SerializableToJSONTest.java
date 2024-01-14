import org.junit.jupiter.api.Test;
import com.google.gson.JsonObject;
import fr.team92.serpents.utils.SerializableToJSON;
import static org.junit.jupiter.api.Assertions.*;

public class SerializableToJSONTest {

    private class SerializableToJSONImpl implements SerializableToJSON {
        private String value;

        public SerializableToJSONImpl(String value) {
            this.value = value;
        }

        @Override
        public JsonObject toJSON() {
            JsonObject json = new JsonObject();
            json.addProperty("value", value);
            return json;
        }
    }

    @Test
    public void testToJSON() {
        SerializableToJSONImpl obj = new SerializableToJSONImpl("test");
        JsonObject json = obj.toJSON();

        assertEquals("test", json.get("value").getAsString());
    }
}