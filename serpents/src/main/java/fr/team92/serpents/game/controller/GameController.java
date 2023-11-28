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

    public int getWidth() {
        return model.getWidth();
    }

    public int getHeight() {
        return model.getHeight();
    }

    public Optional<Segment>[][] getGrid() {
        return model.getGrid();
    }
}
