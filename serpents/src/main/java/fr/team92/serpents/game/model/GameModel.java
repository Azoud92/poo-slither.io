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
    
    private List<Segment> players;
    private int playerIndex;

    @SuppressWarnings("unchecked")
    public GameModel(int width, int height, List<Segment> players) {        
        this.observers = new ArrayList<>();
        this.width = width;
        this.height = height;
        this.grid = (Optional<Segment>[][]) new Optional[width][height];
        initGrid();
        
        this.players = new ArrayList<>();
        for (Segment player : players) {
            addPlayer(player);
        }
        this.playerIndex = 0;
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
     * Check if the position is valid
     * @param position the position
     * @return true if the position is valid, false otherwise
     */
    public boolean isValidPosition(Position position) {
        return position.getX() >= 0
        && position.getX() < width && position.getY() >= 0
        && position.getY() < height
        && !grid[position.getX()][position.getY()].isPresent();
    }

    /**
     * Move the current player in the given direction
     * @param direction the direction
     */
    public void movePlayer(Direction direction) {
        Segment player = players.get(playerIndex);
        Position newPosition = player.simulateMove(direction);
        
        if (isValidPosition(newPosition)) {
            grid[player.getPosition().getX()][player.getPosition().getY()] = Optional.empty();
            player.move(direction);
            grid[player.getPosition().getX()][player.getPosition().getY()] = Optional.of(player);
        }
        else {
            removePlayer(player);
        }

        playerIndex = (playerIndex + 1) % players.size();
        notifyObservers();
    }

    /**
     * Add the player to the grid
     * @param player the player
     */
    public void addPlayer(Segment player) {
        if (!isValidPosition(player.getPosition())) {
            throw new IllegalArgumentException("Invalid position");
        }
        players.add(player);
        grid[player.getPosition().getX()][player.getPosition().getY()] = Optional.of(player);        
    }

    /**
     * Remove the player from the grid
     * @param player the player
     */
    public void removePlayer(Segment player) {
        players.remove(player);
        grid[player.getPosition().getX()][player.getPosition().getY()] = Optional.empty();
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

    /**
     * Get current player
     * @return current player
     */
    public Segment getCurrentPlayer() {
        return players.get(playerIndex);
    }

    /**
     * Get the players
     * @return the players
     */
    public List<Segment> getPlayers() {
        return players;
    }

    public boolean isOccupied(Position position) {
        for (Segment player : players) {
            if (player.getPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }
    
}
