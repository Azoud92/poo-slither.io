package fr.team92.serpents.game.controller;

import java.util.Optional;

import fr.team92.serpents.game.model.Direction;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.model.Segment;
import javafx.scene.Scene;

public class GameController {
    private GameModel model;

    public GameController(GameModel model, Scene scene) {
        this.model = model;
        setKeyListeners(scene);
    }

    /**
     * Set the key listeners
     * @param scene
     */
    private void setKeyListeners(Scene scene) {
        scene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case UP:
                    model.move(Direction.NORTH);
                    break;
                case DOWN:
                    model.move(Direction.SOUTH);
                    break;
                case LEFT:
                    model.move(Direction.WEST);
                    break;
                case RIGHT:
                    model.move(Direction.EAST);
                    break;
                default:
                    break;
            }
        });
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
