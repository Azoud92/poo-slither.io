package fr.team92.serpents.snake.controller;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import javafx.scene.Scene;

/**
 * Contrôleur de serpent responsable de mettre à jour un serpent en fonction de la logique
 */
public sealed interface SnakeController permits HumanSnakeController, BotController, NetworkSnakeController {

    /**
     * Contrôle un serpent. Cette méthode est appelée pour mettre à jour le serpent en fonction de la logique de contrôle
     * spécifique.
     *
     * @param snake le serpent à contrôler
     * @param gameModel le modèle du jeu
     * @param lastUpdate le temps écoulé depuis la dernière mise à jour
     * @param scene la scène de jeu
     */
    void controlSnake(Snake snake, GameModel gameModel, double lastUpdate, Scene scene);
}
