package fr.team92.serpents.game.controller;

import java.util.Optional;

import fr.team92.serpents.game.bots.BotController;
import fr.team92.serpents.game.model.Direction;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.model.Segment;
import javafx.scene.Scene;

public final class GameController {
    private GameModel model;

    public GameController(GameModel model, Scene scene) {
        this.model = model;
        setKeyListeners(scene);
        gameLoop();
    }

    /**
     * Set the key listeners
     * @param scene
     */
    private void setKeyListeners(Scene scene) {
        scene.setOnKeyReleased(event -> {
            Segment currentPlayer = model.getCurrentPlayer();
            if (currentPlayer.getController() instanceof BotController) return;          

            switch (event.getCode()) {
                case UP:
                    model.movePlayer(Direction.NORTH);
                    gameLoop();
                    break;
                case DOWN:
                    model.movePlayer(Direction.SOUTH);
                    gameLoop();
                    break;
                case LEFT:
                    model.movePlayer(Direction.WEST);
                    gameLoop();
                    break;
                case RIGHT:
                    model.movePlayer(Direction.EAST);
                    gameLoop();
                    break;
                default:
                    break;
            }
        });
    }

    private void gameLoop() {
        Segment currentPlayer = model.getCurrentPlayer();
        if (currentPlayer.getController() instanceof BotController) {
            Direction botMove = currentPlayer.getController().getNextMove(model, currentPlayer);
            model.movePlayer(botMove);
        }        
    }

    /**
     * Get the width of the grid
     * @return the width of the grid
     */
    public int getWidth() {
        return model.getWidth();
    }

    /**
     * Get the height of the grid
     * @return the height of the grid
     */
    public int getHeight() {
        return model.getHeight();
    }

    /**
     * Get the grid
     * @return the grid
     */
    public Optional<Segment>[][] getGrid() {
        return model.getGrid();
    }
}
