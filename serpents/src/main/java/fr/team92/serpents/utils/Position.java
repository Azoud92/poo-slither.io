package fr.team92.serpents.utils;

import com.google.gson.JsonObject;

/**
 * Représente une position dans le jeu
 */
public record Position(double x, double y) implements SerializableToJSON {

    /**
     * Déplace la position dans la direction donnée
     * 
     * @param direction la direction
     * @return la nouvelle position
     */
    public Position move(Direction direction, double delta) {
        double angleInRadians = Math.toRadians(direction.angle());
        double dx = Math.cos(angleInRadians) * delta;
        double dy = Math.sin(angleInRadians) * delta;

        return new Position(this.x + dx, this.y + dy);
    }

    public double distanceTo(Position other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Renvoi une position entre deux positions
     * 
     * @param other l'autre position
     * @param t     le coefficient
     * @return la position entre les deux positions
     */
    public Position inter(Position other, double t) {
        double newX = x + t * (other.x - x);
        double newY = y + t * (other.y - y);
        return new Position(newX, newY);
    }

    public double distance(Position newPosition) {
        double dx = this.x - newPosition.x;
        double dy = this.y - newPosition.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "position");
        json.addProperty("x", x);
        json.addProperty("y", y);

        return json;
    }

    public static Position fromJSON(JsonObject json) {
        return new Position(json.get("x").getAsDouble(), json.get("y").getAsDouble());
    }

}
