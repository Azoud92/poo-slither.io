package fr.team92.serpents.snake.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.team92.serpents.snake.bot.factory.ClassicBotFactory;
import fr.team92.serpents.snake.controller.HumanSnakeController;
import fr.team92.serpents.snake.controller.KeyboardControl;
import fr.team92.serpents.snake.controller.MouseControl;
import fr.team92.serpents.snake.controller.NetworkSnakeController;
import fr.team92.serpents.snake.controller.SnakeController;
import fr.team92.serpents.snake.model.segments.NormalSegmentBehavior;
import fr.team92.serpents.snake.model.segments.Segment;
import fr.team92.serpents.snake.model.segments.SegmentBehavior;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;
import fr.team92.serpents.utils.SerializableToJSON;
import javafx.scene.input.KeyCode;

/**
 * Classe représentant un serpent dans un jeu.
 * Un serpent est composé de plusieurs segments et se déplace dans une certaine direction.
 */
public final class Snake implements SerializableToJSON {

    /**
     * Les segments qui composent le serpent.
     */
    private final List<Segment> segments;

    /**
     * Le contrôleur qui gère les mouvements du serpent.
     */
    private final SnakeController controller;

    /**
     * La direction actuelle du serpent.
     */
    private Direction direction;

    /**
     * Indique si le serpent est mort.
     */
    private boolean isDead;

    /**
     * La longueur du serpent
     */
    private int length;

    /**
     * La vitesse actuelle du serpent
     */
    private double speed;

    /**
     * La vitesse du serpent lorsqu'il est en train d'accélérer
     */
    private double speedInAcceleration;

    /**
     * Indique si le serpent est en train d'accélérer
     */
    private boolean isAccelerating;

    /**
     * Indique si le serpent est initialisé
     */
    private boolean initialized;

    /**
     * Diamètre des segments du serpent
     */
    private double diameter;

    /**
     * Durée de vie d'un segment lorsqu'il est en train d'accélérer
     */
    private double segmentLifeInAcceleration;

    /**
     * Segments à supprimer lors de la prochaine mise à jour (lorsque le serpent accélère)
     */
    private List<Segment> segmentsToRemove;

    /**
     * Vitesse par défaut du serpent
     */
    public static final double DEFAULT_SPEED = 1.5;

    /**
     * Longueur minimale du serpent
     */
    public static final int INIT_LENGTH = 10;

    /**
     * Espacement entre les segments du serpent
     */
    public static final double SEGMENT_SPACING = 0.25;

    /**
     * Distance minimale avec les autres serpents lors de l'initialisation
     */
    public static final double MIN_DISTANCE_INIT = 20;

    /**
     * Durée de vie d'un segment lorsqu'il est en train d'accélérer
     */
    private static final double SEGMENT_LIFE_IN_ACCELERATION = 2.5;

    private Snake(SnakeController controller, int length, double diameter, Position position, Direction direction, double speed) {
        this.controller = controller;
        this.segments = new LinkedList<>();
        this.direction = direction;
        this.isDead = false;
        this.speed = speed;
        this.speedInAcceleration = speed * 2;
        this.length = length;
        this.isAccelerating = false;
        this.initialized = false;
        this.diameter = diameter;
        this.segmentLifeInAcceleration = SEGMENT_LIFE_IN_ACCELERATION;
        this.segmentsToRemove = new LinkedList<>();
        initSegments(position);
    }

    /**
     * Initialise les segments du serpent
     * @param length la longueur du serpent
     * @param startPosition la position de départ du serpent
     */
    private void initSegments(Position startPosition) {
        if (initialized) throw new IllegalStateException("Le serpent est déjà initialisé");
        if (startPosition == null) throw new IllegalArgumentException("La position de départ ne doit pas être null");
        Position segmentPos = startPosition;

        for (int i = 0; i < length; i++) {
            segments.add(new Segment(segmentPos, Segment.DEFAULT_DIAMETER, new NormalSegmentBehavior()));
            segmentPos = segmentPos.move(direction.opposite(), SEGMENT_SPACING);
        }
        initialized = true;
    }

    /**
     * Déplace le serpent et ajoute les nouveaux segments qu'il a mangés
     * @param lastUpdate le temps écoulé depuis la dernière mise à jour
     * @param width la largeur de la grille
     * @param height la hauteur de la grille
     */
    public void move(double lastUpdate, double width, double height) {
        moveSegments(lastUpdate, width, height);
    }

    /**
     * Déplace les segments du serpent
     * @param lastUpdate le temps écoulé depuis la dernière mise à jour
     * @param width la largeur de la grille
     * @param height la hauteur de la grille
     */
    private void moveSegments(double lastUpdate, double width, double height) {

        List<Position> oldPositions = segments.stream()
            .map(Segment::getPosition)
            .collect(Collectors.toList());

        ListIterator<Segment> it = segments.listIterator();

        while (it.hasNext()) {
            Segment seg = it.next();
            int index = it.previousIndex();

            Position oldPosition = oldPositions.get(index);
            double rayon = seg.getDiameter() / 2.0;
            Position newPosition;

            // Si c'est la tête du serpent, on la déplace dans la direction actuelle
            if (index == 0) {
                newPosition = oldPosition.move(direction, speed * lastUpdate);
            } else { // Sinon, on la déplace dans la direction du segment précédent (pour éviter que le serpent se contracte ou s'étire en prenannt la position du segment précédent)
                Position prevPosition = oldPositions.get(index - 1);
                double distanceToPrev = oldPosition.distanceTo(prevPosition);
                newPosition = oldPosition.move(oldPosition.angleTo(prevPosition), distanceToPrev - SEGMENT_SPACING);
            }

            // Si le segment atteint un bord, il apparaît de l'autre côté
            if (newPosition.x() - rayon < 0) {
                newPosition = new Position(width + newPosition.x(), newPosition.y());
            } else if (newPosition.x() > width) {
                newPosition = new Position(newPosition.x() - width, newPosition.y());
            }
            if (newPosition.y() - rayon < 0) {
                newPosition = new Position(newPosition.x(), height + newPosition.y());
            } else if (newPosition.y() > height) {
                newPosition = new Position(newPosition.x(), newPosition.y() - height);
            }

            seg.setPosition(newPosition);
        }
    }

    /**
     * Vérifie si le serpent est en collision avec un autre segment
     * @param segment le segment à tester
     * @return true si le serpent est en collision avec le segment, false sinon
     */
    public boolean isInCollisionWith(Segment segment) {
        if (segment == null) throw new IllegalArgumentException("Le segment ne doit pas être null");
        if (segments.size() < 2) throw new IllegalStateException("Le serpent doit avoir au moins 2 segments");
        if (segments.getFirst().isInCollision(segment)) return true;
        return false;
    }

    /**
     * Ajoute un nouveau segment au serpent
     * @param behavior le comportement du nouveau segment
     */
    public void addSegment(SegmentBehavior behavior) {
        if (behavior == null) throw new IllegalArgumentException("Le comportement ne doit pas être null");
        Position headPos = segments.getFirst().getPosition();
        Segment newSegment = new Segment(headPos, segments.getFirst().getDiameter(), behavior);
        segments.addFirst(newSegment);
        length++;
    }

    /**
     * Obtenir la direction du serpent
     * @return la direction du serpent
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Définit la direction du serpent
     * @param direction la nouvelle direction du serpent
     */
    public void setDirection(Direction direction) {
        if (direction == null) throw new IllegalArgumentException("La direction ne doit pas être null");
        this.direction = direction;
    }

    /**
     * Obtenir la position de la tête du serpent
     * @return la position de la tête du serpent
     */
    public Position getHeadPosition() {
        return segments.getFirst().getPosition();
    }

    /**
     * Obtenir les segments du serpent
     * @return la copie des segments du serpent
     */
    public List<Segment> getSegments() {
        return new ArrayList<>(segments);
    }

    /**
     * Obtenir le contrôleur du serpent
     * @return le contrôleur du serpent
     */
    public SnakeController getController() {
        return controller;
    }

    /**
     * Vérifie si le serpent est mort
     * @return true si le serpent est mort, false sinon
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * Tue le serpent
     */
    public void die() {
        if (isDead) {
            throw new IllegalStateException("Le serpent est déjà mort");
        }
        isDead = true;
        for (Segment segment : segments) {
            segment.die();
        }
    }

    /**
     * Obtenir la vitesse du serpent
     * @return la vitesse du serpent
     */
    public double getSpeed() {
        return speed;
    }

    /**
     * Définit la vitesse du serpent
     * @param speed la nouvelle vitesse du serpent
     */
    public void setSpeed(double speed) {
        if (speed <= 0) throw new IllegalArgumentException("La vitesse doit être strictement positive");
        this.speed = speed;
        this.speedInAcceleration = speed * 2;
    }

    /**
     * Obtenir le diamètre des segments du serpent
     * @return le diamètre des segments du serpent
     */
    public double getDiameter() {
        return diameter;
    }

    /**
     * Obtenir la longueur du serpent
     * @return la longueur du serpent
     */
    public int getLength() {
        return length;
    }

    /**
     * Accélère le serpent
     */
    public void accelerate() {
        if (length > INIT_LENGTH) {
            speed = speedInAcceleration;
            if (segmentLifeInAcceleration > 0) segmentLifeInAcceleration -= 0.1;
            else {
                segmentLifeInAcceleration = SEGMENT_LIFE_IN_ACCELERATION;
                segmentsToRemove.add(segments.getLast());
                length--;
            }            
        } else {
            decelerate();
        }
    }

    /**
     * Stoppe l'accélération du serpent
     */
    public void decelerate() {
        if (speed <= DEFAULT_SPEED) return;
        speed /= 2;
        speedInAcceleration = speed * 2;
    }

    /**
     * Définit si le serpent est en train d'accélérer
     * @param isAccelerating true si le serpent est en train d'accélérer, false sinon
     */
    public void setIsAccelerating(boolean isAccelerating) {
        this.isAccelerating = isAccelerating;
    }

    /**
     * Indique si le serpent est en train d'accélérer
     * @return true si le serpent est en train d'accélérer, false sinon
     */
    public boolean isAccelerating() {
        return isAccelerating;
    }

    /**
     * Obtenir les segments à supprimer lors de la prochaine mise à jour
     * @return les segments à supprimer lors de la prochaine mise à jour
     */
    public List<Segment> getSegmentsToRemove() {
        return segmentsToRemove;
    }

    /**
     * Obtenir la représentation JSON du serpent
     * @return la représentation JSON du serpent,
     * par exemple : <code>{"type":"snake","segments":[{"type":"segment","position":{"x":0.0,"y":0.0},"diameter":1.0,"behavior":{"type":"normal"}}],"direction":{"x":1.0,"y":0.0},"speed":1.5}</code>
     */
    @Override
    public JsonObject toJSON() {
        JsonObject json = new JsonObject();
        json.addProperty("type", "snake");

        JsonArray segmentsArray = new JsonArray();
        for (Segment segment : segments) {
            segmentsArray.add(segment.toJSON());
        }

        json.add("segments", segmentsArray);
        json.add("direction", direction.toJSON());
        json.addProperty("speed", speed);
        
        return json;
    }    

    /**
     * Crée un serpent humain contrôlé par le clavier
     * @param keyMap la map des touches du clavier
     * @param length la longueur du serpent
     * @param position la position de départ du serpent
     * @param direction la direction de départ du serpent
     * @return le serpent créé
     */
    public static Snake CreateHumanKeyboardSnake(Map<KeyCode, Double> keyMap, int length, Position position,
            Direction direction) {
        SnakeController controller = new HumanSnakeController(new KeyboardControl(keyMap));
        return new Snake.Builder()
            .setController(controller)
            .setLength(length)
            .setPosition(position)
            .setDirection(direction)
            .build();
    }

    /**
     * Crée un serpent contrôlé par l'IA classique
     * @param length la longueur du serpent
     * @param position la position de départ du serpent
     * @param direction la direction de départ du serpent
     * @return le serpent créé
     */
    public static Snake CreateAvoidWallsBotSnake(int length, Position position, Direction direction) {
        SnakeController controller = new ClassicBotFactory().createBotController();
        return new Snake.Builder()
            .setController(controller)
            .setLength(length)
            .setPosition(position)
            .setDirection(direction)
            .build();
    }

    /**
     * Crée un serpent contrôlé par la souris
     * @param length la longueur du serpent
     * @param position la position de départ du serpent
     * @param direction la direction de départ du serpent
     * @return le serpent créé
     */
    public static Snake CreateHumanMouseSnake(int length, Position position, Direction direction) {
        SnakeController controller = new HumanSnakeController(new MouseControl());
        return new Snake.Builder()
            .setController(controller)
            .setLength(length)
            .setPosition(position)
            .setDirection(direction)
            .build();
    }

    /**
     * Crée un serpent contrôlé par le réseau
     * @param length la longueur du serpent
     * @param position la position de départ du serpent
     * @param direction la direction de départ du serpent
     * @return le serpent créé
     */
    public static Snake CreateNetworkSnake(int length, Position position, Direction direction) {
        SnakeController controller = new NetworkSnakeController();
        return new Snake.Builder()
            .setController(controller)
            .setLength(length)
            .setPosition(position)
            .setDirection(direction)
            .build();
    }

    /**
     * Builder pour créer un serpent
     */
    public static class Builder {
        private SnakeController controller;
        private int length = INIT_LENGTH;
        private double diameter = Segment.DEFAULT_DIAMETER;
        private Position position;
        private Direction direction;
        private double speed = DEFAULT_SPEED;

        public Builder setController(SnakeController controller) {
            this.controller = controller;
            return this;
        }

        public Builder setLength(int length) {
            this.length = length;
            return this;
        }

        public Builder setDiameter(double diameter) {
            this.diameter = diameter;
            return this;
        }

        public Builder setPosition(Position position) {
            this.position = position;
            return this;
        }

        public Builder setDirection(Direction direction) {
            this.direction = direction;
            return this;
        }

        public Builder setSpeed(double speed) {
            this.speed = speed;
            return this;
        }

        public Snake build() {
            if (controller == null || position == null || direction == null || speed <= 0 || length < INIT_LENGTH)
                throw new IllegalStateException("Le contrôleur, la position et la direction doivent être définis. La vitesse doit être strictement positive et la longueur doit être supérieure ou égale à " + INIT_LENGTH + ".");
            return new Snake(controller, length, diameter, position, direction, speed);
        }
    }
}