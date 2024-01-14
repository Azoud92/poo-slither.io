package fr.team92.serpents.snake.controller;

import java.util.HashMap;
import java.util.Map;

import fr.team92.serpents.game.view.GameMode;
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
     * Association entre une touche et un double
     */
    private final Map<KeyCode, Double> keyMap;

    /**
     * Constructeur pour un contrôle par clavier d'un serpent
     * 
     * @param keyMap
     */
    public KeyboardControl(Map<KeyCode, Double> keyMap) {
        this.keyMap = new HashMap<>(keyMap);
    }

    @Override
    public void handleControl(Snake snake, InputEvent event, GameMode gameMode, int cellSize, double windowWidth,
            double windowHeight) {
        KeyEvent keyEvent = (KeyEvent) event;
        Double angleChange = keyMap.get(keyEvent.getCode());
        if (angleChange != null) {
            if (angleChange == 0.0) {
                if (keyEvent.getEventType() == KeyEvent.KEY_PRESSED) {
                    snake.setIsAccelerating(true);
                } else if (keyEvent.getEventType() == KeyEvent.KEY_RELEASED) {
                    snake.setIsAccelerating(false);
                }
            } else {
                double newAngle = snake.getDirection().getAngle() + angleChange;
                snake.setDirection(new Direction(newAngle));
            }
        }
    }

    /**
     * Récupérer la map des touches
     * 
     * @return la map des touches
     */
    public Map<KeyCode, Double> getKeyMap() {
        return new HashMap<>(keyMap);
    }

}
