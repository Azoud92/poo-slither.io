package fr.team92.serpents.snake.model.segments;

import com.google.gson.JsonObject;

import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;
import fr.team92.serpents.utils.SerializableToJSON;

/**
 * Cette classe représente un segment d'un serpent.
 * Un segment a une position, un diamètre, un état de vie et un comportement.
 */
public final class Segment implements SerializableToJSON {

    /**
     * La position actuelle du segment
     */
    private Position position;

    /**
     * L'état de vie du segment
     */
    private boolean dead = false;

    /**
     * Le diamètre du segment
     */
    private double diameter;

    /**
     * Le comportement du segment
     */
    private final SegmentBehavior behavior;

    /**
     * Le diamètre par défaut d'un segment
     */
    public static final double DEFAULT_DIAMETER = 1;

    /**
     * Crée un nouveau segment à la position donnée, avec le diamètre et le comportement spécifiés.
     * @param position la position du segment.
     * @param diameter le diamètre du segment.
     * @param behavior le comportement du segment.
     */
    public Segment(Position position, double diameter, SegmentBehavior behavior) {
        if (position == null || behavior == null) throw new IllegalArgumentException("Les arguments ne doivent pas être null");
        this.position = position;
        if (diameter <= 0) throw new IllegalArgumentException("Le diamètre doit être strictement positif");
        this.diameter = diameter;
        this.behavior = behavior;
    }

    /**
     * Crée un nouveau segment à la position donnée, avec le diamètre par défaut et le comportement spécifié.
     * @param position la position du segment.
     * @param behavior le comportement du segment.
     */
    public Segment(Position position, SegmentBehavior behavior) {
        this(position, DEFAULT_DIAMETER, behavior);
    }

    /**
     * Déplace le segment dans la direction donnée.
     * @param direction la direction dans laquelle déplacer le segment.
     * @param delta la distance à déplacer.
     */
    public void move(Direction direction, double delta) {
        if (direction == null) throw new IllegalArgumentException("L'argument ne doit pas être null");
        position = position.move(direction, delta);
    }

    /**
     * Obtenir la position du segment
     * @return la position du segment
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Modifier la position du segment
     * @param position la nouvelle position du segment
     */
    public void setPosition(Position position) {
        if (position == null) throw new IllegalArgumentException("L'argument ne doit pas être null");
        this.position = position;
    }

    /**
     * Savoir si le segment est mort
     * @return true si le segment est mort, false sinon
     */
    public boolean isDead() {
        return dead;
    }

    /**
     * Tuer le segment (pour qu'il soit transformé en nourriture et puisse être mangé)
     */
    public void die() {
        this.dead = true;
    }

    /**
     * Obtenir le diamètre du segment
     * @return le diamètre du segment
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * Savoir si le segment est en collision avec un autre segment
     * @param otherSegment l'autre segment
     * @return true si les deux segments sont en collision, false sinon
     */
    public boolean isInCollision(Segment otherSegment) {
        if (otherSegment == null) throw new IllegalArgumentException("L'argument ne doit pas être null");
        return behavior.isInCollision(this, otherSegment);
    }

    /**
     * Obtenir le comportement du segment
     * @return le comportement du segment
     */
    public SegmentBehavior getBehavior() {
        return behavior;
    }

    /**
     * Modifier le diamètre du segment
     * @param d le nouveau diamètre du segment
     */
    public void setDiameter(double d) {
        this.diameter = d;
    }

    /**
     * Convertit l'objet Segment en un objet JSON.
     * @return un objet JSON qui représente le segment,
     * par exemple : <code>{"type": "segment", "position": {"x": 0, "y": 0}, "diameter": 10, "behavior": "normal"}</code>
     */
    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "segment");
        json.add("position", position.toJSON());
        json.addProperty("diameter", diameter);
        json.addProperty("behavior", behavior.getBehaviorType().toString().toLowerCase());
        
        return json;
    }

}
