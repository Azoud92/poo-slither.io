package fr.team92.serpents.snake.controller;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;

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

    /**
     * Constructeur du contrôleur du serpent humain
     * @param snakeEventControl contrôleur d'événement du serpent
     */
    public HumanSnakeController(SnakeEventControl snakeEventControl) {
        this.snakeEventControl = snakeEventControl;
    }

    /**
     * Récupérer le contrôleur d'événement du serpent
     * @return le contrôleur d'événement du serpent 
     */
    public SnakeEventControl getSnakeEventControl() {
        return snakeEventControl;
    }

    /**
     * Définir le dernier événement enregistré
     * @param event l'événement
     */
    public void setEvent(InputEvent event) {
        lastEvent = event;
    }

    @Override
    public void controlSnake(Snake snake, GameModel gameModel, double lastUpdate) {
        if (lastEvent != null) {
            if (lastEvent instanceof KeyEvent && snakeEventControl instanceof KeyboardControl) {
                snakeEventControl.handleControl(snake, lastEvent);
                lastEvent = null;
            }
        }
    }

}
