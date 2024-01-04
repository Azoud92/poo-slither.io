package fr.team92.serpents.game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import fr.team92.serpents.snake.controller.HumanSnakeController;
import fr.team92.serpents.snake.controller.KeyboardControl;
import fr.team92.serpents.snake.model.BurrowingSegmentBehavior;
import fr.team92.serpents.snake.model.NormalSegmentBehavior;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.snake.model.SegmentBehavior;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.GameState;
import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;
import fr.team92.serpents.utils.Position;
import javafx.scene.input.KeyCode;

/**
 * Représente le modèle de jeu
 */
public final class GameModel implements Observable {

    /**
     * La liste des observateurs
     */
    private final List<Observer> observers;

    /**
     * La largeur du tableau et sa hauteur
     */
    private final int width, height;

    /**
     * Le tableau de la partie
     */
    private final Map<Position, Segment> grid;

    /**
     * La liste des serpents
     */
    private final List<Snake> snakes;

    /**
     * L'état de la partie
     */
    private GameState state;

    /**
     * Le nombre de nourriture à maintenir dans la partie
     */
    private static int FEED_IN_PARTY;

    private static int CELL_SIZE;

    /**
     * Constructeur du modèle de jeu
     * 
     * @param width   la largeur
     * @param height  la hauteur
     * @param players les joueurs
     */
    public GameModel(int width, int height, int cellSize, int nbFood) {
        CELL_SIZE = cellSize;
        FEED_IN_PARTY = nbFood;
        this.observers = new ArrayList<>();
        this.width = width / CELL_SIZE;
        this.height = height / CELL_SIZE;
        this.grid = new HashMap<>();
        this.state = GameState.WAITING;

        this.snakes = new ArrayList<>();
    }

    /**
     * Ajoute de la nourriture à la partie
     * 
     * @param nb le nombre de nourriture à ajouter
     */
    private void addFeed(int nb) {
        for (int i = 0; i < nb; i++) {
            int nbFeed = (int) grid.values().stream().filter(segment -> segment.isDead()).count();
            if (nbFeed >= FEED_IN_PARTY) {
                return;
            }
            Position pos = generateRandomPosition();

            SegmentBehavior behavior;
            if (Math.random() < 0.5) {
                behavior = new NormalSegmentBehavior();
            } else {
                behavior = new BurrowingSegmentBehavior();
            }
            Segment segment = new Segment(pos, 1, behavior);
            segment.die();
            grid.put(pos, segment);
        }
    }

    /**
     * Génère une position aléatoire
     * 
     * @return la position aléatoire
     */
    private Position generateRandomPosition() {
        Position pos = new Position((int) (Math.random() * width), (int) (Math.random() * height));
        while (isOccupied(pos, 1)) {
            pos = new Position((int) (Math.random() * width), (int) (Math.random() * height));
        }
        return pos;
    }

    /**
     * Vérifie si la position est valide
     * 
     * @param position la position
     * @return true si la position est valide, false sinon
     */
    public boolean isValidPosition(Position position, double diameter) {
        double radius = diameter / 2;
        return position.x() >= radius
                && position.x() < width - radius && position.y() >= radius
                && position.y() < height - radius
                && !isOccupied(position, diameter);
    }

    /**
     * Vérifie si la position est occupée
     * 
     * @param position la position
     * @return true si la position est occupée, false sinon
     */
    public boolean isOccupied(Position position, double diameter) {
        return snakes.stream()
                .anyMatch(snake -> snake.getSegments().stream().anyMatch(
                        segment -> segment.getPosition().distanceTo(position) < segment.getDiameter() / 2 + diameter / 2
                                && !segment.isDead()));
    }

    public boolean isInCollision(Snake snake) {
        Segment headSegment = snake.getHeadSegment();
        Position headPosition = snake.getHeadPosition();
        double headRadius = snake.getHeadDiameter() / 2;

        return snakes.stream()
                .filter(otherSnake -> !snake.equals(otherSnake))
                .flatMap(otherSnake -> otherSnake.getSegments().stream())
                .anyMatch(otherSegment ->
                !otherSegment.isDead() &&
                otherSegment.getPosition().distanceTo(headPosition) < otherSegment.getDiameter() / 2
                    + headRadius
                && headSegment.isInCollision(otherSegment));
    }

    /**
     * Vérifie si les keyCodes sont uniques
     * 
     * @param snake le serpent
     * @return true si les keyCodes sont uniques, false sinon
     */
    private boolean keyCodesUniques() {
        Set<KeyCode> keyCodes = new HashSet<>();

        snakes.stream()
                .map(Snake::getController)
                .filter(controller -> controller instanceof HumanSnakeController)
                .map(controller -> (HumanSnakeController) controller)
                .filter(humanController -> humanController.getSnakeEventControl() instanceof KeyboardControl)
                .map(humanController -> (KeyboardControl) humanController.getSnakeEventControl())
                .map(KeyboardControl::getKeyMap)
                .flatMap(keyMap -> keyMap.keySet().stream())
                .forEach(keyCode -> {
                    if (keyCodes.contains(keyCode)) {
                        throw new IllegalArgumentException("Key code is not unique");
                    }
                    keyCodes.add(keyCode);
                });

        return true;
    }

    private void grow(Snake snake) {
        for (Map.Entry<Position, Segment> entry : grid.entrySet()) {
            Position pos = entry.getKey();
            Segment segment = entry.getValue();
            if (segment.isDead() && snake.getHeadPosition().distanceTo(pos) < snake.getHeadDiameter() / 2
                    + segment.getDiameter() / 2) {
                SegmentBehavior behavior = segment.getBehavior();
                grid.remove(pos);
                snake.addSegment(behavior);
                addFeed(1);
                return;
            }
        }
    }

    /**
     * Déplace les serpents
     */
    public void moveSnakes(double lastUpdate) {
        List<Snake> snakesToRemove = new ArrayList<>();

        for (Snake snake : snakes) {
            snake.move(lastUpdate, width, height);

            if (isInCollision(snake)) {
                snake.die();
                rmSegments(snake);
                snakesToRemove.add(snake);
            }

            grow(snake);

            for (Segment segment : snake.getSegments()) {
                grid.put(segment.getPosition(), segment);
            }
        }

        for (Snake snake : snakesToRemove) {
            removeSnake(snake);
        }

        if (snakes.size() == 1) {
            state = GameState.FINISHED;
        }

        notifyObservers();
    }

    private void rmSegments(Snake snake) {
        for (int i = 0; i < snake.getSegments().size(); i++) {
            if (i % snake.getCoeff() != 0) {
                Position pos = snake.getSegments().get(i).getPosition();
                grid.remove(pos);
            }
        }
    }

    /**
     * Ajoute un serpent dans le tableau
     * 
     * @param snake le serpent
     */
    public void addSnake(Snake snake) {
        if (snake == null) {
            throw new IllegalArgumentException("Snake cannot be null");
        }
        for (Segment segment : snake.getSegments()) {
            if (!isValidPosition(segment.getPosition(), segment.getDiameter())) {
                throw new IllegalArgumentException("Invalid position");
            }
            grid.put(segment.getPosition(), segment);
        }
        snakes.add(snake);
    }

    /**
     * Supprime un serpent du tableau
     * 
     * @param snake le serpent
     */
    public void removeSnake(Snake snake) {
        if (!snakes.contains(snake)) {
            throw new IllegalArgumentException("Snake not found");
        }
        snakes.remove(snake);
    }

    /**
     * Démarre la partie
     */
    public void gameStart() {
        if (state != GameState.WAITING) {
            throw new IllegalStateException("Game already started");
        }
        if (!keyCodesUniques()) {
            throw new IllegalArgumentException("Les touches doivent être uniques");
        }
        addFeed(FEED_IN_PARTY);
        state = GameState.RUNNING;
        notifyObservers();
    }

    /**
     * Obtenir la largeur du tableau
     * 
     * @return la largeur du tableau
     */
    public int getWidth() {
        return width;
    }

    /**
     * Obtenir la hauteur du tableau
     * 
     * @return la hauteur du tableau
     */
    public int getHeight() {
        return height;
    }

    /**
     * Obtenir le tableau de la partie
     * 
     * @return le tableau de la partie
     */
    public Map<Position, Segment> getGrid() {
        return new HashMap<>(grid);
    }

    /**
     * Obtenir l'état de la partie
     * 
     * @return l'état de la partie
     */
    public GameState getState() {
        return state;
    }

    /**
     * Obtenir la liste des serpents
     * 
     * @return la liste des serpents
     */
    public List<Snake> getSnakes() {
        return snakes;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(observer -> observer.update());
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    public boolean collidesWithAnySnake(Position position, Snake currentSnake) {
        for (Snake snake : snakes) {
            if (snake != currentSnake && snake.collidesWith(position)) {
                return true;
            }
        }
        return false;
    }

    public Position getClosestFood(Position headPosition) {
        return null;
    }
}
