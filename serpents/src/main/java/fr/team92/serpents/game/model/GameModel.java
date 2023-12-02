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
    private List<Observer> observers;

    /**
     * La largeur du tableau et sa hauteur
     */
    private final int width, height;

    /**
     * Le tableau de la partie
     */
    private Map<Position, Segment> grid;
    
    /**
     * La liste des serpents
     */
    private List<Snake> snakes;

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
    public GameModel(int width, int height, List<Snake> players) {        
        this.observers = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.grid = new HashMap<>();
        this.state = GameState.WAITING;
        
        this.snakes = new ArrayList<>();
        for (Snake snake : players) {
            addSnake(snake);
        }
        this.snakeIndex = 0;

        this.state = GameState.RUNNING;
    }

    /**
     * Vérifie si la position est valide
     * @param position la position
     * @return true si la position est valide, false sinon
     */
    public boolean isValidPosition(Position position) {
        return position.getX() >= 0
        && position.getX() < width && position.getY() >= 0
        && position.getY() < height
        && !isOccupied(position);
    }

    /**
     * Déplace un serpent
     */
    public void moveSnake() {
        Snake snake = snakes.get(snakeIndex);
        Direction direction = snake.getDirection();
        Position newHeadPos = snake.getHeadPosition().move(direction);

        if (isValidPosition(newHeadPos)) {
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
        return grid;
    }

    /**
     * Get current player
     * @return current player
     */
    public Snake getCurrentPlayer() {
        return snakes.get(snakeIndex);
    }

    /**
     * Get the players
     * @return the players
     */
    public List<Snake> getPlayers() {
        return snakes;
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

    /**
     * Obtenir l'état de la partie
     * @return l'état de la partie
     */
    public GameState getState() {
        return state;
    }    
}
