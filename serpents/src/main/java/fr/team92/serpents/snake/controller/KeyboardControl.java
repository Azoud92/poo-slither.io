package fr.team92.serpents.snake.controller;

import java.util.Map;

import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Contrôle du serpent par le clavier
 */
public final class KeyboardControl implements SnakeEventControl {

    /**
     * Association entre une touche et une direction
     */
    private final Map<KeyCode, Direction> keyMap;

    /**
     * Constructeur pour un contrôle par clavier d'un serpent
     * @param keyMap
     */
    public KeyboardControl(Map<KeyCode, Direction> keyMap) {
        this.keyMap = keyMap;
    }

    @Override
    public void handleControl(Snake snake, InputEvent event) {
        Direction newDirection = keyMap.get(((KeyEvent) event).getCode());
        if (newDirection != null) {
            snake.setDirection(newDirection);
        }
        else throw new IllegalArgumentException("La touche n'est pas associée à une direction");
    }

}
