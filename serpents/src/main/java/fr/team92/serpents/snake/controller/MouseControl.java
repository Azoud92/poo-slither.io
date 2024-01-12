package fr.team92.serpents.snake.controller;

import fr.team92.serpents.game.model.GameMode;
import fr.team92.serpents.game.model.SinglePlayerMode;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import javafx.scene.input.InputEvent;
import javafx.scene.input.MouseEvent;

/**
 * Contrôle du serpent par la souris
 */
public final class MouseControl implements SnakeEventControl {

    public void handleControl(Snake snake, InputEvent event, GameMode gameMode, int cellSize, double windowWidth,
            double windowHeight) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            double mouseX = mouseEvent.getX();
            double mouseY = mouseEvent.getY();

            double snakeX;
            double snakeY;

            // Si le mode de jeu est solo, la tête du serpent est toujours au centre de la
            // fenêtre
            // Sinon, elle peut être n'importe où dans la fenêtre
            if (gameMode instanceof SinglePlayerMode) {
                snakeX = windowWidth / 2;
                snakeY = windowHeight / 2;
            } else {
                snakeX = snake.getHeadPosition().x() * cellSize;
                snakeY = snake.getHeadPosition().y() * cellSize;
            }

            double dx = mouseX - snakeX;
            double dy = mouseY - snakeY;

            double targetAngleInDegrees = (Math.toDegrees(Math.atan2(dy, dx))) % 360;

            double currentAngle = snake.getDirection().angle();
            double angleDifference = targetAngleInDegrees - currentAngle;

            // Normalise la différence d'angle à l'intervalle [-180, 180)
            if (angleDifference > 180) {
                angleDifference -= 360;
            } else if (angleDifference < -180) {
                angleDifference += 360;
            }

            // Augmente ou diminue progressivement l'angle actuel du serpent pour atteindre
            // l'angle cible
            double angleChangeSpeed = 6; // Vitesse de changement d'angle
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