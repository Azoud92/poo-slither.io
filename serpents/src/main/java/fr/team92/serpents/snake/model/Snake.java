package fr.team92.serpents.snake.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import fr.team92.serpents.snake.bot.factory.AvoidWallsBotFactory;
import fr.team92.serpents.snake.controller.HumanSnakeController;
import fr.team92.serpents.snake.controller.KeyboardControl;
import fr.team92.serpents.snake.controller.MouseControl;
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

    public static final double DEFAULT_SPEED = 1;

    private double speed;

    /**
     * Constructeur du serpent
     * 
     * @param length    la longueur du serpent
     * @param position  la position de départ
     * @param direction la direction de départ
     */
    public Snake(SnakeController controller, int length, Position position, Direction direction, double speed) {
        this.controller = controller;
        this.segments = new LinkedList<>();
        this.direction = direction;
        this.segmentsToAdd = new LinkedList<>();
        this.isDead = false;
        this.speed = speed;
        initSegments(length, position, direction);
    }

    public Snake(SnakeController controller, int length, Position position, Direction direction) {
        this(controller, length, position, direction, DEFAULT_SPEED);
    }

    /**
     * Initialiser les segments du serpent
     * 
     * @param length         la longueur du serpent
     * @param startPosition  la position de départ
     * @param startDirection la direction de départ
     */
    private void initSegments(int length, Position startPosition, Direction startDirection) {
        Position segmentPos = startPosition;

        for (int i = 0; i < length * 30; i++) {
            segments.add(new Segment(segmentPos, 1));
            segmentPos = segmentPos.move(startDirection.opposite(), 0.005);
        }
    }

    /**
     * Déplacer le serpent
     */
    public void move(double lastUpdate, double width, double height) {
        moveSegments(lastUpdate, width, height);
        addNewSegments();
    }

    /**
     * Déplacer les segments du serpent
     */
    private void moveSegments(double lastUpdate, double width, double height) {
        double maxMoveDistance = segments.getFirst().getDiameter() * speed * lastUpdate;

        for (int i = segments.size() - 1; i >= 0; i--) {
            Position oldPosition = segments.get(i).getPosition();
            Position newPosition;

            if (i > 0) {
                newPosition = new Position(segments.get(i - 1).getPosition().x(),
                        segments.get(i - 1).getPosition().y());
            } else {
                newPosition = oldPosition.move(direction, maxMoveDistance);
            }

            // Vérifie si le segment est sorti des limites de l'écran
            if (newPosition.x() < 0) {
                newPosition = new Position(width + newPosition.x(), newPosition.y());
            } else if (newPosition.x() > width) {
                newPosition = new Position(newPosition.x() - width, newPosition.y());
            }
            if (newPosition.y() < 0) {
                newPosition = new Position(newPosition.x(), height + newPosition.y());
            } else if (newPosition.y() > height) {
                newPosition = new Position(newPosition.x(), newPosition.y() - height);
            }

            segments.get(i).setPosition(newPosition);
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
        Segment newSegment = new Segment(tailPos, segments.getLast().getDiameter());
        segmentsToAdd.add(newSegment);
    }

    /**
     * Définir la direction du serpent
     * 
     * @param direction la nouvelle direction du serpent
     */
    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    /**
     * Obtenir la direction du serpent
     * 
     * @return la direction du serpent
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Obtenir la position de la tête du serpent
     * 
     * @return la position de la tête du serpent
     */
    public Position getHeadPosition() {
        return segments.getFirst().getPosition();
    }

    /**
     * Obtenir la liste des segments du serpent
     * 
     * @return la liste des segments du serpent
     */
    public List<Segment> getSegments() {
        return segments;
    }

    /**
     * Obtenir la position de la queue du serpent
     * 
     * @return la position de la queue du serpent
     */
    public Position getTailPosition() {
        return segments.getLast().getPosition();
    }

    /**
     * Obtenir le contrôleur du serpent
     * 
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

    public double getSpeed() {
        return speed;
    }

    public static Snake CreateHumanKeyboardSnake(Map<KeyCode, Double> keyMap, int length, Position position,
            Direction direction) {
        SnakeController controller = new HumanSnakeController(new KeyboardControl(keyMap));
        return new Snake(controller, length, position, direction);
    }

    public static Snake CreateAvoidWallsBotSnake(int length, Position position, Direction direction) {
        SnakeController controller = new AvoidWallsBotFactory().createBotController();
        return new Snake(controller, length, position, direction);
    }

    public static Snake CreateHumanMouseSnake(int length, Position position, Direction direction) {
        SnakeController controller = new HumanSnakeController(new MouseControl());
        return new Snake(controller, length, position, direction);
    }

    public double getHeadDiameter() {
        return segments.getFirst().getDiameter();
    }

    public Segment getHeadSegment() {
        return segments.getFirst();
    }

    public void setHeadPosition(Position position) {
        if (!segments.isEmpty()) {
            segments.set(0, new Segment(position, segments.get(0).getDiameter()));
        }
    }

}