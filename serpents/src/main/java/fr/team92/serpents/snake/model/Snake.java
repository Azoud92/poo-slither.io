package fr.team92.serpents.snake.model;

import java.util.LinkedList;
import java.util.List;

import fr.team92.serpents.snake.controller.SnakeController;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;

/**
 * Représente un serpent
 */
public final class Snake {

    /**
     * La liste des segments du serpent
     */
    private LinkedList<Segment> segments;

    /**
     * Le contrôleur du serpent
     */
    private SnakeController controller;
    
    /**
     * La direction du serpent
     */
    private Direction direction;

    /**
     * Constructeur du serpent
     * @param length la longueur du serpent
     * @param position la position de départ
     * @param direction la direction de départ
     */
    public Snake(SnakeController controller, int length, Position position, Direction direction) {
        this.controller = controller;
        this.segments = new LinkedList<>();
        this.direction = direction;
        initSegments(length, position.clone(), direction);
    }

    /**
     * Initialiser les segments du serpent
     * @param length la longueur du serpent
     * @param startPosition la position de départ
     * @param startDirection la direction de départ
     */
    private void initSegments(int length, Position startPosition, Direction startDirection) {
        Position segmentPos = startPosition;
    
        for (int i = 0; i < length; i++) {
            segments.add(new Segment(segmentPos.clone()));
            segmentPos = segmentPos.move(startDirection);
        }
    }    

    /**
     * Déplacer le serpent
     */
    public void move() {
        Position prev = segments.getFirst().getPosition();
        Position tmpPos;

        segments.getFirst().move(direction);

        for (int i = 1; i < segments.size(); i++) {
            tmpPos = segments.get(i).getPosition();
            segments.get(i).setPosition(prev);
            prev = tmpPos;
        }
    }

    /**
     * Définir la direction du serpent
     * @param direction la nouvelle direction du serpent
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }


    /**
     * Obtenir la direction du serpent
     * @return la direction du serpent
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Obtenir la position de la tête du serpent
     * @return la position de la tête du serpent
     */
    public Position getHeadPosition() {
        return segments.getFirst().getPosition();
    }

    /**
     * Obtenir la liste des segments du serpent
     * @return la liste des segments du serpent
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Obtenir la position de la queue du serpent
     * @return la position de la queue du serpent
     */
    public Position getTailPosition() {
        return segments.getLast().getPosition();
    }

    /**
     * Obtenir le contrôleur du serpent
     * @return le contrôleur du serpent
     */
    public SnakeController getController() {
        return controller;
    }

}