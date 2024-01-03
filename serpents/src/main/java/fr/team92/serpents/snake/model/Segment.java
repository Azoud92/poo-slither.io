package fr.team92.serpents.snake.model;

import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;

/**
 * Un segment représente une partie d'un serpent
 */
public final class Segment {

    /**
     * La position actuelle du segment
     */
    private Position position;

    private boolean dead = false;

    private double diameter;

    private SegmentBehavior behavior;

    /**
     * Créer un nouveau segment à la position donnée
     * @param position la position du segment
     */
    public Segment(Position position, double diameter, SegmentBehavior behavior) {
        this.position = position;
        this.diameter = diameter;
        this.behavior = behavior;
    }

    /**
     * Déplacer le segment dans la direction donnée
     * @param direction la direction dans laquelle déplacer le segment
     */
    public void move(Direction direction, double delta) {
        position = position.move(direction, delta);
    }

    /**
     * Obtenir la position du segment
     * @return la position actuelle du segment
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Modifier la position du segment
     * @param position la nouvelle position du segment
     */
    public void setPosition(Position position) {
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
     * Tuer le segment
     */
    public void die() {
        if (dead) {
            throw new IllegalStateException("Segment already dead");
        }
        this.dead = true;
    }

    public double getDiameter() {
        return diameter;
    }

    public boolean isInCollision(Segment otherSegment) {
        return behavior.isInCollision(this, otherSegment);
    }

    public SegmentBehavior getBehavior() {
        return behavior;
    }

}
