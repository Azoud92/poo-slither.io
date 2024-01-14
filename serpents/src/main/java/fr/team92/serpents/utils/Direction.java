package fr.team92.serpents.utils;

import com.google.gson.JsonObject;

/**
 * Représente une direction en degrés.
 * L'angle est toujours normalisé entre 0 et 360.
 */
public final class Direction implements SerializableToJSON, Cloneable {

    /**
     * Angle en degrés de la direction.
     */
    private double angle;

    /**
     * Construit une direction à partir d'un angle en degrés.
     * @param angle l'angle en degrés
     */
    public Direction(double angle) {
        this.angle = (angle % 360 + 360) % 360;
    }

    /**
     * Retourne la direction opposée à la direction courante.
     * @return la direction opposée à la direction courante
     */
    public Direction opposite() {
        return new Direction((this.angle + 180) % 360);
    }

    /**
     * Renvoie une direction aléatoire entre 0 et 360 degrés.
     * @return une direction aléatoire entre 0 et 360 degrés
     */
    public static Direction random() {
        return new Direction(Math.random() * 360);
    }

    /**
     * Retourne l'angle en degrés de la direction.
     * @return l'angle en degrés de la direction
     */
    public double getAngle() {
        return angle;
    }

    /**
     * Modifie l'angle en degrés de la direction.
     * @param angle le nouvel angle en degrés de la direction
     */
    public void setAngle(double angle) {
        this.angle = (angle % 360 + 360) % 360;
    }

    /**
     * Retourne une copie de la direction courante.
     * @return une copie de la direction courante
     */
    @Override
    public Direction clone() {
        return new Direction(angle);
    }

    /**
     * Renvoie la direction courante au format JSON.
     * 
     * @return la direction courante au format JSON,
     * par exemple : <code>{"type": "direction", "angle": 90.0}</code>
     */
    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "direction");
        json.addProperty("angle", angle);

        return json;
    }

}
