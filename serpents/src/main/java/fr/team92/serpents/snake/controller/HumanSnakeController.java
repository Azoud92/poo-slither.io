package fr.team92.serpents.snake.controller;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import javafx.scene.Scene;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * Contrôleur du serpent humain
 */
public final class HumanSnakeController implements SnakeController {

    /**
     * Contrôleur d'événement du serpent
     */
    private final SnakeEventControl snakeEventControl;

    /*
     * Dernier événement enregistré
     */
    private InputEvent lastEvent;
    private MouseEvent otherMouseEvent;

    /**
     * Constructeur du contrôleur du serpent humain
     * 
     * @param snakeEventControl contrôleur d'événement du serpent
     */
    public HumanSnakeController(SnakeEventControl snakeEventControl) {
        this.snakeEventControl = snakeEventControl;
    }

    /**
     * Récupérer le contrôleur d'événement du serpent
     * 
     * @return le contrôleur d'événement du serpent
     */
    public SnakeEventControl getSnakeEventControl() {
        return snakeEventControl;
    }

    /**
     * Définir le dernier événement enregistré
     * 
     * @param event l'événement
     */
    public void setEvent(InputEvent event) {
        lastEvent = event;
    }

    public void setOtherMouseEvent(MouseEvent event) {
        otherMouseEvent = event;
    }

    @Override
    public void controlSnake(Snake snake, GameModel gameModel, double lastUpdate, Scene scene) {
        if (lastEvent instanceof KeyEvent && snakeEventControl instanceof KeyboardControl) {
            if (lastEvent != null) {
                snakeEventControl.handleControl(snake, lastEvent, gameModel.getGameMode(), gameModel.getCellSize(),
                        scene.getWidth(),
                        scene.getHeight());
                lastEvent = null;
            }
        } else if (lastEvent instanceof MouseEvent && snakeEventControl instanceof MouseControl) {
            if (lastEvent != null) {
                snakeEventControl.handleControl(snake, lastEvent, gameModel.getGameMode(), gameModel.getCellSize(),
                        scene.getWidth(),
                        scene.getHeight());
                lastEvent = null;
            }
        } else if (otherMouseEvent != null && snakeEventControl instanceof MouseControl) {

            // Gère l'accélération et la décélération du serpent
            if (otherMouseEvent.getEventType() == MouseEvent.MOUSE_PRESSED
                    && otherMouseEvent.getButton() == MouseButton.PRIMARY) {
                snake.setIsAccelerating(true);
                otherMouseEvent = null;
            } else if (otherMouseEvent.getEventType() == MouseEvent.MOUSE_RELEASED
                    && otherMouseEvent.getButton() == MouseButton.PRIMARY) {
                snake.setIsAccelerating(false);
                otherMouseEvent = null;
            }

        }
    }

}
