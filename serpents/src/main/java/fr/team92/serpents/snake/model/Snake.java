package fr.team92.serpents.snake.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.team92.serpents.snake.bot.factory.AvoidWallsBotFactory;
import fr.team92.serpents.snake.controller.HumanSnakeController;
import fr.team92.serpents.snake.controller.KeyboardControl;
import fr.team92.serpents.snake.controller.SnakeController;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;
import javafx.scene.input.KeyCode;

/**
 * Représente un serpent
 */
public final class Snake {

    /**
     * La liste des segments du serpent
     */
    private final LinkedList<Segment> segments;

    /**
     * La liste des segments à ajouter au serpent
     */
    private final LinkedList<Segment> segmentsToAdd;

    /**
     * Le contrôleur du serpent
     */
    private final SnakeController controller;
    
    /**
     * La direction du serpent
     */
    private Direction direction;

    private boolean isDead;

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
        this.segmentsToAdd = new LinkedList<>();
        this.isDead = false;
        initSegments(length, position, direction);
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
            segments.add(new Segment(segmentPos));
            segmentPos = segmentPos.move(startDirection.opposite());
        }
    }    

    /**
     * Déplacer le serpent
     */
    public void move() {
        moveSegments();
        addNewSegments();
    }

    /**
     * Déplacer les segments du serpent
     */
    private void moveSegments() {
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
     * Ajouter les nouveaux segments en attente au serpent
     */
    private void addNewSegments() {
        if (!segmentsToAdd.isEmpty()) {
            segments.addLast(segmentsToAdd.getFirst());
            segmentsToAdd.removeFirst();
        }
    }

    /**
     * Ajouter un nouveau segment au serpent
     */
    public void addSegment() {
        Position tailPos = segments.getLast().getPosition();
        Segment newSegment = new Segment(tailPos);
        segmentsToAdd.add(newSegment);
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

    public boolean isDead() {
        return isDead;
    }

    /**
     * Tuer le serpent
     */
    public void die() {
        if (isDead) {
            throw new IllegalStateException("Le serpent est déjà mort");
        }
        for (Segment segment : segments) {
            segment.die();
        }
    }

    public static Snake CreateHumanKeyboardSnake(Map<KeyCode, Direction> keyMap, int length, Position position, Direction direction) {
        SnakeController controller = new HumanSnakeController(new KeyboardControl(keyMap));
        return new Snake(controller, length, position, direction);
    }

    public static Snake CreateAvoidWallsBotSnake(int length, Position position, Direction direction) {
        SnakeController controller = new AvoidWallsBotFactory().createBotController();
        return new Snake(controller, length, position, direction);
    }

}