package fr.team92.serpents.utils;

import com.google.gson.JsonObject;

public record Direction(double angle) implements SerializableToJSON {

    public Direction {
        if (angle < 0) {
            angle += 360;
        } else if (angle >= 360) {
            angle -= 360;
        }
        angle = angle % 360;
    }

    public Direction opposite() {
        return new Direction((this.angle + 180) % 360);
    }

    public static Direction random() {
        return new Direction(Math.random() * 360);
    }

    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "direction");
        json.addProperty("angle", angle);

        return json;
    }

}
