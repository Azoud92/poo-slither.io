package fr.team92.serpents.snake.controller;

import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;

/**
 * Contrôle du serpent par la souris
 */
public final class MouseControl implements SnakeEventControl {

    @Override
    public void handleControl(Snake snake, InputEvent event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            double dx = mouseEvent.getX() - snake.getHeadPosition().x();
            double dy = mouseEvent.getY() - snake.getHeadPosition().y();
            double angle = Math.atan2(dy, dx);

            Direction newDirection = new Direction(angle);
            if (newDirection != snake.getDirection().opposite()) {
                snake.setDirection(newDirection);
            }
        }
    }
}