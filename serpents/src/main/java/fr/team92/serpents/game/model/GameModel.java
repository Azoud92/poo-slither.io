package fr.team92.serpents.game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.GameState;
import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;
import fr.team92.serpents.utils.Position;

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
     * L'index du serpent courant
     */
    private int snakeIndex;

    /**
     * L'état de la partie
     */
    private GameState state;

    /**
     * Constructeur du modèle de jeu
     * @param width la largeur
     * @param height la hauteur
     * @param players les joueurs
     */
    public GameModel(int width, int height) {        
        this.observers = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.grid = new HashMap<>();
        this.state = GameState.WAITING;
        
        this.snakes = new ArrayList<>();
        this.snakeIndex = 0;
        this.state = GameState.RUNNING;
        addFeed(50);
    }

    /**
     * Ajoute de la nourriture à la partie
     * @param nb le nombre de nourriture à ajouter
     */
    private void addFeed(int nb) {
        for (int i = 0; i < nb; i++) {
            Position pos = generateRandomPosition();
            Segment segment = new Segment(pos);
            segment.die();
            grid.put(pos, segment);
        }
    }
    /**
     * Génère une position aléatoire
     * @return la position aléatoire
     */
    private Position generateRandomPosition() {
        Position pos = new Position((int) (Math.random() * width), (int) (Math.random() * height));
        while (isOccupied(pos)) {
            pos = new Position((int) (Math.random() * width), (int) (Math.random() * height));
        }
        return pos;
    }

    /**
     * Vérifie si la position est valide
     * @param position la position
     * @return true si la position est valide, false sinon
     */
    public boolean isValidPosition(Position position) {
        return position.x() >= 0
        && position.x() < width && position.y() >= 0
        && position.y() < height
        && !isOccupied(position);
    }

    /**
     * Vérifie si la position est occupée
     * @param position la position
     * @return true si la position est occupée, false sinon
     */
    public boolean isOccupied(Position position) {
        for (Snake snake : snakes) {
            for (Segment segment : snake.getSegments()) {
                if (segment.getPosition().equals(position) && !segment.isDead()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Déplace un serpent
     */
    public void moveSnake() {
        Snake snake = snakes.get(snakeIndex);
        Direction direction = snake.getDirection();
        Position newHeadPos = snake.getHeadPosition().move(direction);      

        if (isValidPosition(newHeadPos)) {

            Segment deadSegment = grid.get(newHeadPos);
            if (deadSegment != null && deadSegment.isDead()) {
                grid.remove(newHeadPos);
                snake.addSegment();
                addFeed(1);
            }

            grid.remove(snake.getTailPosition());
            
            snake.move();

            for (Segment segment : snake.getSegments()) {
                grid.put(segment.getPosition(), segment);
            }
        }
        else {
            snake.die();
            snakes.remove(snake);
            if (snakes.size() == 1) {
                state = GameState.FINISHED;
            }
        }

        snakeIndex = (snakeIndex + 1) % snakes.size();
        notifyObservers();
    }
    
    /**
     * Ajoute un serpent dans le tableau
     * @param snake le serpent
     */
    public void addSnake(Snake snake) {
        if (snake == null) {
            throw new IllegalArgumentException("Snake cannot be null");
        }
        for (Segment segment : snake.getSegments()) {
            if (!isValidPosition(segment.getPosition())) {
                throw new IllegalArgumentException("Invalid position");
            }
            grid.put(segment.getPosition(), segment);
        }
        snakes.add(snake);
    }

    /**
     * Supprime un serpent du tableau
     * @param snake le serpent
     */
    public void removeSnake(Snake snake) {
        if (!snakes.contains(snake)) {
            throw new IllegalArgumentException("Snake not found");
        }
        for (Segment segment : snake.getSegments()) {
            grid.remove(segment.getPosition());
        }
        snakes.remove(snake);
    }

    public void gameStart() {
        if (state != GameState.WAITING) {
            throw new IllegalStateException("Game already started");
        }
        state = GameState.RUNNING;
        notifyObservers();
    }

    /**
     * Obtenir la largeur du tableau
     * @return la largeur du tableau
     */
    public int getWidth() {
        return width;
    }

    /**
     * Obtenir la hauteur du tableau
     * @return la hauteur du tableau
     */
    public int getHeight() {
        return height;
    }

    /**
     * Obtenir le tableau de la partie
     * @return le tableau de la partie
     */
    public Map<Position, Segment> getGrid() {
        return new HashMap<>(grid);
    }

    /**
     * Obtenir le serpent courant
     * @return le serpent courant
     */
    public Snake getCurrentPlayer() {
        return snakes.get(snakeIndex);
    }

    /**
     * Obtenir l'état de la partie
     * @return l'état de la partie
     */
    public GameState getState() {
        return state;
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
        for (Observer observer : this.observers) {
            observer.update();
        }
    }
}
