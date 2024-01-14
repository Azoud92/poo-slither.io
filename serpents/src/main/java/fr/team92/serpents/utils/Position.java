package fr.team92.serpents.utils;

import com.google.gson.JsonObject;

/**
 * Représente une position dans le jeu (x, y)
 */
public record Position(double x, double y) implements SerializableToJSON {

    /**
     * Déplace la position dans la direction donnée et renvoie la nouvelle position.
     * @param direction la direction du déplacement.
     * @param delta la distance du déplacement.
     * @return la nouvelle position après le déplacement.
     */
    public Position move(Direction direction, double delta) {
        double angleInRadians = Math.toRadians(direction.getAngle());
        double dx = Math.cos(angleInRadians) * delta;
        double dy = Math.sin(angleInRadians) * delta;
    
        return new Position(this.x + dx, this.y + dy);
    }
    
    /**
     * Calcule et renvoie la distance entre la position courante et une autre position.
     * @param other l'autre position.
     * @return la distance entre la position courante et l'autre position.
     */
    public double distanceTo(Position other) {
        double dx = this.x - other.x;
        double dy = this.y - other.y;
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Calcule et renvoie une position intermédiaire entre cette position et une autre position.
     * @param other l'autre position.
     * @param t le coefficient pour calculer la position intermédiaire.
     * @return la position intermédiaire entre cette position et l'autre position.
     */
    public Position inter(Position other, double t) {
        double newX = x + t * (other.x - x);
        double newY = y + t * (other.y - y);
        return new Position(newX, newY);
    }

    /**
     * Calcule et renvoie la direction de la position courante vers une autre position.
     * @param other l'autre position.
     * @return la direction de la position courante vers l'autre position.
     */
    public Direction angleTo(Position other) {
        double dx = other.x - this.x;
        double dy = other.y - this.y;
        return new Direction(Math.toDegrees(Math.atan2(dy, dx)));
    }

    /**
     * Convertit cette position en un objet JSON.
     * @return un objet JSON qui représente cette position,
     * par exemple : <code>{"type": "position", "x": 10.0, "y": 20.0}</code>
     */
    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "position");
        json.addProperty("x", x);
        json.addProperty("y", y);

        return json;
    }

}
