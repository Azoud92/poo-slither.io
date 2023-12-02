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

    /**
     * Créer un nouveau segment à la position donnée
     * @param position la position du segment
     */
    public Segment(Position position) {
        this.position = position.clone();
    }

    /**
     * Déplacer le segment dans la direction donnée
     * @param direction la direction dans laquelle déplacer le segment
     */
    public void move(Direction direction) {
        position = position.move(direction);
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

    public boolean isDead() {
        return dead;
    }

    public void die() {
        if (dead) {
            throw new IllegalStateException("Segment already dead");
        }
        this.dead = true;
    }

}
