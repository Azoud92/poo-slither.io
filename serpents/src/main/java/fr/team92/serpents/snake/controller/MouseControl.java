package fr.team92.serpents.snake.controller;

import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;

/**
 * Contrôle du serpent par la souris
 */
public final class MouseControl implements SnakeEventControl {

    public void handleControl(Snake snake, InputEvent event, int cellSize) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();

            double snakeX = snake.getHeadPosition().x() * cellSize;
            double snakeY = snake.getHeadPosition().y() * cellSize;

            double dx = mouseX - snakeX;
            double dy = mouseY - snakeY;

            double targetAngle = Math.atan2(dy, dx);
            double targetAngleInDegrees = Math.toDegrees(targetAngle) - 90;
            if (targetAngleInDegrees < 0) {
                targetAngleInDegrees += 360;
            }

            double currentAngle = snake.getDirection().getAngle();
            double angleDifference = targetAngleInDegrees - currentAngle;

            // Normalise la différence d'angle à l'intervalle [-180, 180)
            if (angleDifference > 180) {
                angleDifference -= 360;
            } else if (angleDifference < -180) {
                angleDifference += 360;
            }

            // Augmente ou diminue progressivement l'angle actuel du serpent pour atteindre
            // l'angle cible
            double angleChangeSpeed = 6; // Vitesse de changement d'angle (en degrés par appel de la méthode)
            if (angleDifference > 0) {
                currentAngle += Math.min(angleChangeSpeed, angleDifference);
            } else if (angleDifference < 0) {
                currentAngle -= Math.min(angleChangeSpeed, -angleDifference);
            }

            // Normalise l'angle actuel à l'intervalle [0, 360)
            if (currentAngle < 0) {
                currentAngle += 360;
            } else if (currentAngle >= 360) {
                currentAngle -= 360;
            }

            snake.setDirection(new Direction(currentAngle));
        }
    }
}