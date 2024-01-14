package fr.team92.serpents.snake.controller;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import javafx.scene.Scene;

/**
 * Représente un contrôleur de serpent réseau pour le serveur.
 */
public final class NetworkSnakeController implements SnakeController {

    /**
     * File des commandes de direction reçues sur le réseau
     */
    private final Queue<Direction> commandQueue;

    /**
     * Initialise le contrôleur de serpent réseau
     */
    public NetworkSnakeController() {
        this.commandQueue = new ConcurrentLinkedQueue<>(); // Permet de parcourir la liste des commandes sans risque de ConcurrentModificationException
    }

    /**
     * Ajoute une direction à la file
     * @param command direction à ajouter
     */
    public void addCommand(Direction dir) {
        commandQueue.add(dir);
    }

    @Override
    public void controlSnake(Snake snake, GameModel gameModel, double lastUpdate, Scene scene) {
        Direction currentDirection = snake.getDirection();
        Direction targetDirection = commandQueue.peek(); // Récupère la prochaine direction sans la retirer

        if (targetDirection != null) {
            double currentAngle = currentDirection.getAngle();
            double targetAngle = targetDirection.getAngle();

            double angleDifference = targetAngle - currentAngle;
            // Normalisation de la différence d'angle dans l'intervalle [-180, 180]
            angleDifference = (angleDifference + 540) % 360 - 180;

            // Ajustement de l'angle de 6° maximum vers la nouvelle direction
            double angleAdjustment = Math.min(Math.max(angleDifference, -6), 6);
            currentAngle += angleAdjustment;

            // Mise à jour de la direction du serpent
            snake.setDirection(new Direction(currentAngle));

            // Si l'angle ajusté est suffisamment proche de la cible, on retire la commande de la file
            if (Math.abs(angleDifference) <= 6) {
                commandQueue.poll();
            }
        }
    }
}
