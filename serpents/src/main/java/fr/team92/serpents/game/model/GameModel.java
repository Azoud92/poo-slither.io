package fr.team92.serpents.game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;

public final class GameModel implements Observable {

    private List<Observer> observers;

    private final int width;
    private final int height;
    private Optional<Segment>[][] grid;
    private Segment head;

    @SuppressWarnings("unchecked")
    public GameModel(int width, int height) {        
        this.observers = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.grid = (Optional<Segment>[][]) new Optional[width][height];
        initGrid();
        this.head = new Segment(new Position(0, 0));
        this.grid[0][0] = Optional.of(this.head);
    }

    /**
     * Initialize the grid
     */
    private void initGrid() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                grid[i][j] = Optional.empty();
            }
        }
    }

    /**
     * Move the snake in the given direction
     * @param direction the direction
     */
    public void move(Direction direction) {
        Position nextPosition = head.simulateMove(direction);
        if (isValidMove(nextPosition)) {
            grid[head.getPosition().getX()][head.getPosition().getY()] = Optional.empty();
            head.move(direction);
            grid[head.getPosition().getX()][head.getPosition().getY()] = Optional.of(head);
            notifyObservers();
        }
    }

    /**
     * Check if the move is valid
     * @param position the position
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMove(Position position) {
        return position.getX() >= 0
        && position.getX() < width && position.getY() >= 0
        && position.getY() < height
        && !grid[position.getX()][position.getY()].isPresent();
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
     * Get the head of the snake
     * @return the head of the snake
     */
    public Segment getHead() {
        return head;
    }

    /**
     * Get the width of the grid
     * @return the width of the grid
     */
    public int getWidth() {
        return width;
    }

    /**
     * Get the height of the grid
     * @return the height of the grid
     */
    public int getHeight() {
        return height;
    }

    /**
     * Get the grid
     * @return the grid
     */
    public Optional<Segment>[][] getGrid() {
        return grid.clone();
    }
    
}
