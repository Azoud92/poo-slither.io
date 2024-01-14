package fr.team92.serpents.game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import fr.team92.serpents.game.view.GameMode;
import fr.team92.serpents.snake.controller.HumanSnakeController;
import fr.team92.serpents.snake.controller.KeyboardControl;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.snake.model.segments.BurrowingSegmentBehavior;
import fr.team92.serpents.snake.model.segments.NormalSegmentBehavior;
import fr.team92.serpents.snake.model.segments.Segment;
import fr.team92.serpents.snake.model.segments.SegmentBehavior;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.GameState;
import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;
import fr.team92.serpents.utils.Position;
import javafx.scene.input.KeyCode;

/**
 * Représente le modèle de jeu. Cette classe contient la logique principale du jeu, y compris le mouvement des serpents,
 * la gestion des collisions, l'ajout de nourriture, etc. Elle utilise plusieurs autres classes, comme Snake, Segment,
 * et Position, pour représenter les différents éléments du jeu.
 */
public final class GameModel implements Observable {

    /**
     * La liste des observateurs. Ces observateurs sont notifiés lorsque l'état du jeu change.
     */
    private final List<Observer> observers;

    /**
     * La largeur et la hauteur du tableau de jeu. Ces valeurs sont utilisées pour déterminer si une position est valide
     * et pour générer des positions aléatoires.
     */
    private final int width, height;

    /**
     * Le tableau de la partie. Il s'agit d'une carte qui associe chaque position à un segment. Cette carte est utilisée
     * pour déterminer si une position est occupée et pour ajouter et supprimer des segments.
     */
    private final Map<Position, Segment> snakesSegmentsGrid;

    /**
     * La liste des serpents dans le jeu. Chaque serpent est représenté par une instance de la classe Snake.
     */
    private final List<Snake> snakes;

    /**
     * L'état actuel de la partie. Il peut être en attente, en cours ou terminé.
     */
    private GameState state;

    /**
     * La liste des segments morts. Ces segments sont utilisés pour faire réapparaître
     * de la nourriture lorsque les serpents en mangent.
     */
    private final Map<Position, Segment> deadSegments;

    /**
     * Le nombre de nourriture à maintenir dans la partie. Ce nombre est utilisé pour déterminer combien de nourriture
     * ajouter lorsque la partie commence et lorsqu'un serpent mange de la nourriture.
     */
    private int nbFood;

    /**
     * Le nombre par défaut de nourriture à ajouter lorsque la partie commence
     */
    private static final int DEFAULT_FEED_IN_PARTY = 50;

    /**
     * La taille d'une cellule du tableau de jeu. Cette valeur est utilisée pour déterminer la taille des segments
     */
    private static int CELL_SIZE;

    /**
     * Initialise le modèle de jeu avec la largeur et la hauteur du tableau, la taille d'une cellule et le nombre de nourriture
     * @param width
     * @param height
     * @param cellSize
     * @param nbFood
     */
    public GameModel(int width, int height, int cellSize, int nbFood) {
        if (width <= 0 || height <= 0 || cellSize <= 0 || nbFood <= 0) {
            throw new IllegalArgumentException("Width, height, cellSize et nbFood doivent être strictement positifs");
        }
        CELL_SIZE = cellSize;
        this.nbFood = nbFood;
        this.observers = new ArrayList<>();
        this.width = width / CELL_SIZE;
        this.height = height / CELL_SIZE;
        this.snakesSegmentsGrid = new HashMap<>();
        this.state = GameState.WAITING;

        this.snakes = new ArrayList<>();
        this.deadSegments = new HashMap<>();
    }

    /**
     * Initialise le modèle de jeu avec la largeur et la hauteur du tableau, la taille d'une cellule et le nombre de nourriture par défaut
     * @param width la largeur du tableau
     * @param height la hauteur du tableau
     * @param cellSize la taille d'une cellule
     */
    public GameModel(int width, int height, int cellSize) {
        this(width, height, cellSize, DEFAULT_FEED_IN_PARTY);
    }

    /**
     * Démarre la partie
     */
    public void gameStart() {
        if (state != GameState.WAITING) {
            throw new IllegalStateException("La partie a déjà été lancée");
        }
        if (!keyCodesUniques()) {
            throw new IllegalArgumentException("Les touches doivent être uniques");
        }
        addFeed(nbFood);
        state = GameState.RUNNING;
        notifyObservers();
    }

    /**
     * Ajoute de la nourriture dans le tableau
     * @param nb le nombre de nourriture à ajouter
     */
    private void addFeed(int nb) {
        if (nb < 0) throw new IllegalArgumentException("Le nombre de nourriture doit être positif");
    
        int nbFeed = deadSegments.size();
        for (int i = 0; i < nb; i++) {
            if (nbFeed >= nbFood) {
                return;
            }
            Position pos = generateRandomPosition();
    
            SegmentBehavior behavior;
            if (Math.random() < 0.5) {
                behavior = new NormalSegmentBehavior();
            } else {
                behavior = new BurrowingSegmentBehavior();
            }
            Segment segment = new Segment(pos, Segment.DEFAULT_DIAMETER, behavior);
            segment.die();
            deadSegments.put(pos, segment);
            nbFeed++;
        }
    }

    /**
     * Génère une position aléatoire
     * 
     * @return la position aléatoire
     */
    private Position generateRandomPosition() {
        Position pos = new Position((int) (Math.random() * width), (int) (Math.random() * height));
        while (isOccupied(pos, Segment.DEFAULT_DIAMETER)) {
            pos = new Position((int) (Math.random() * width), (int) (Math.random() * height));
        }
        return pos;
    }

    /**
     * Vérifie si la position est valide
     * @param position la position à vérifier
     * @param diameter le diamètre du segment
     * @return true si la position est valide, false sinon
     */
    private boolean isValidPosition(Position position, double diameter) {
        if (position == null || diameter < 0)
            throw new IllegalArgumentException("La position ne peut pas être nulle et le diamètre doit être positif");
        double radius = diameter / 2;
        return position.x() >= radius
                && position.x() < width - radius && position.y() >= radius
                && position.y() < height - radius
                && !isOccupied(position, diameter);
    }

    /**
     * Vérifie si la position est occupée
     * @param position la position à vérifier
     * @param diameter le diamètre du segment
     * @return true si la position est occupée, false sinon
     */
    private boolean isOccupied(Position position, double diameter) {
        for (Segment segment : snakesSegmentsGrid.values()) {
            if (segment.getPosition().distanceTo(position) < segment.getDiameter() / 2 + diameter / 2) {
                return true;
            }
        }    
        for (Segment segment : deadSegments.values()) {
            if (segment.getPosition().distanceTo(position) < segment.getDiameter() / 2 + diameter / 2) {
                return true;
            }
        }    
        return false;
    }

    /**
     * Vérifie si le serpent est en collision avec un autre serpent
     * @param snake le serpent à vérifier
     * @return true si le serpent est en collision avec un autre serpent, false sinon
     */
    private boolean isInCollision(Snake snake) {
        for (Snake s : snakes) {
            if (snake != s) {
                for (Segment segment : s.getSegments()) {
                    if (snake.isInCollisionWith(segment)) return true;
                }
            }
        }
        return false;
    }

    /**
     * Vérifie si les touches sont uniques pour chaque joueur
     * @return true si les touches sont uniques, false sinon
     */
    private boolean keyCodesUniques() {
        Set<KeyCode> keyCodes = new HashSet<>();
    
        boolean hasDuplicate = snakes.stream()
                .map(Snake::getController)
                .filter(controller -> controller instanceof HumanSnakeController)
                .map(controller -> (HumanSnakeController) controller)
                .filter(humanController -> humanController.getSnakeEventControl() instanceof KeyboardControl)
                .map(humanController -> (KeyboardControl) humanController.getSnakeEventControl())
                .map(KeyboardControl::getKeyMap)
                .flatMap(keyMap -> keyMap.keySet().stream())
                .anyMatch(keyCode -> !keyCodes.add(keyCode));
    
        if (hasDuplicate) {
            throw new IllegalArgumentException("Key code is not unique");
        }
    
        return true;
    }

    /**
     * Vérifie si le segment peut manger un segment;
     * le cas échéant, agrandit le serpent et fait réapparaître le segment mangé
     * à une position aléatoire s'il n'y a pas assez de segments morts
     * 
     * @param snake le serpent
     * 
     */
    private void grow(Snake snake) {
        if (snake == null) throw new IllegalArgumentException("Le serpent ne peut pas être null");
    
        Map<Position, Segment> copyOfDeadSegments = new HashMap<>(deadSegments);
        Iterator<Map.Entry<Position, Segment>> iterator = copyOfDeadSegments.entrySet().iterator();
    
        while (iterator.hasNext()) {
            Map.Entry<Position, Segment> entry = iterator.next();
            Position pos = entry.getKey();
            Segment segment = entry.getValue();
    
            if (snake.getHeadPosition().distanceTo(pos) < snake.getDiameter() / 2 + segment.getDiameter() / 2) {
                SegmentBehavior behavior = segment.getBehavior();
                snake.addSegment(behavior);
                addFeed(1);
                deadSegments.remove(pos);
                return;
            }
        }
    }

    /**
     * Déplace les serpents, vérifie les collisions et fait grandir les serpents
     * @param lastUpdate le temps écoulé depuis la dernière mise à jour
    */
    public void moveSnakes(double lastUpdate) {
        Iterator<Snake> iterator = snakes.iterator();
        while (iterator.hasNext()) {
            Snake snake = iterator.next();
            for (Segment segment : snake.getSegments()) {
                snakesSegmentsGrid.remove(segment.getPosition());
            }
    
            snake.move(lastUpdate, width, height);
    
            for (Segment segment : snake.getSegments()) {
                snakesSegmentsGrid.put(segment.getPosition(), segment);
            }
    
            if (isInCollision(snake)) {
                snake.die();
                removeSegments(snake);
                iterator.remove();
            } else {
                grow(snake);
            }

            if (!snake.getSegmentsToRemove().isEmpty()) {
                for (Segment segment : snake.getSegmentsToRemove()) {
                    snakesSegmentsGrid.remove(segment.getPosition());
                    snake.getSegments().remove(segment);
                }
            }
        }
    
        if (snakes.size() == 1 || snakes.stream().noneMatch(s -> s.getController() instanceof HumanSnakeController)) {
            state = GameState.FINISHED;
        }
    
        notifyObservers();
    }

    /**
     * Supprime les segments du serpent du tableau et les ajoute dans la liste des segments morts
     * @param snake le serpent à supprimer
     */
    private void removeSegments(Snake snake) {
        if (snake == null) throw new IllegalArgumentException("Le serpent ne peut pas être null");
        for (Segment segment : snake.getSegments()) {
            snakesSegmentsGrid.remove(segment.getPosition());
            segment.die();
            deadSegments.put(segment.getPosition(), segment);
        }
    }

    /**
     * Ajoute un serpent dans la partie
     * 
     * @param snake le serpent à ajouter
     */
    public void addSnake(Snake snake) {
        if (!isValidSnake(snake)) throw new IllegalArgumentException("Snake is not valid");

        // On ajoute chaque segment si tout est bon
        for (Segment segment : snake.getSegments()) {
            snakesSegmentsGrid.put(segment.getPosition(), segment);
        }
        snakes.add(snake);
    }

    /**
     * Vérifie si le serpent à ajouter est valide
     * @param snake le serpent à vérifier
     * @return true si le serpent est valide, false sinon
     */
    public boolean isValidSnake(Snake snake) {
        if (snake == null) {
            throw new IllegalArgumentException("Snake cannot be null");
        }
        for (Segment segment : snake.getSegments()) {
            if (!isValidPosition(segment.getPosition(), segment.getDiameter())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Obtenir une position libre pour un serpent (pour le placer lorsqu'il rejoint la partie)
     * @param minDistance Distance minimale entre la tête du serpent et les autres serpents
     * @param diameter Diamètre d'un segment du serpent
     * @param length Longueur initiale du serpent
     * @param dir Direction initiale du serpent
     * @return Position libre pour un serpent
     */
    public Position getFreePositionForSnake(double minDistance, double diameter, double length, Direction dir) {
        if (minDistance < 0 || diameter < 0 || length < 0 || Snake.SEGMENT_SPACING < 0 || dir == null) {
            throw new IllegalArgumentException("Invalid parameters");
        }
    
        Set<Position> occupiedPositions = getOccupiedPositions();
    
        Random random = new Random();
        Position potentialPos;
    
        for (int i = 0; i < 1000; i++) {
            potentialPos = new Position(random.nextDouble() * width, random.nextDouble() * height);
    
            if (isPositionFreeForSnake(potentialPos, minDistance, diameter, length, dir, occupiedPositions)) {
                return potentialPos;
            }
        }
    
        throw new IllegalStateException("Aucune position libre trouvée");
    }
    
    /**
     * Obtenir la liste des positions occupées par les serpents
     * @return la liste des positions occupées par les serpents
     */
    private Set<Position> getOccupiedPositions() {
        Set<Position> occupiedPositions = new HashSet<>();
        occupiedPositions.addAll(snakesSegmentsGrid.keySet());
        occupiedPositions.addAll(deadSegments.keySet());
        return occupiedPositions;
    }

    /**
     * Vérifie si la position est libre pour un serpent
     * @param headPosition la position de la tête du serpent
     * @param minDistance la distance minimale entre la tête du serpent et les autres serpents
     * @param diameter le diamètre d'un segment du serpent
     * @param length la longueur du serpent
     * @param dir la direction du serpent
     * @param occupiedPositions la liste des positions occupées par les serpents
     * @return true si la position est libre, false sinon
     */
    private boolean isPositionFreeForSnake(Position headPosition, double minDistance, double diameter, double length, Direction dir, Set<Position> occupiedPositions) {
        for (int i = 0; i < length; i++) {
            double totalDistance = i * (diameter + Snake.SEGMENT_SPACING);
            Position segmentPosition = headPosition.move(dir.opposite(), totalDistance);
            if (occupiedPositions.contains(segmentPosition) || isPositionTooCloseToOtherSnakes(segmentPosition, minDistance)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Vérifie si la position est trop proche d'un autre serpent
     * @param position la position à vérifier
     * @param minDistance la distance minimale entre la tête du serpent et les autres serpents
     * @return true si la position est trop proche d'un autre serpent, false sinon
     */
    private boolean isPositionTooCloseToOtherSnakes(Position position, double minDistance) {
        return snakes.stream()
            .flatMap(snake -> snake.getSegments().stream())
            .anyMatch(segment -> segment.getPosition().distanceTo(position) < minDistance);
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
    public Map<Position, Segment> getSnakesSegmentsGrid() {
        Map<Position, Segment> combined = new HashMap<>();
        combined.putAll(snakesSegmentsGrid);
        combined.putAll(deadSegments);
        return combined;
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

    /**
     * Obtenir la taille d'une cellule
     * @return la taille d'une cellule
     */
    public int getCellSize() {
        return CELL_SIZE;
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

    public GameMode getGameMode() {
        return observers.get(0).getGameMode();
    }

}
