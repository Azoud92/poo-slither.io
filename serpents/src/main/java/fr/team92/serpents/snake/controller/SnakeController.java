package fr.team92.serpents.snake.controller;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import javafx.scene.Scene;

/**
 * Contrôleur de serpent
 */
public sealed interface SnakeController permits HumanSnakeController, BotController, NetworkSnakeController {

    /**
     * Contrôle du serpent
     * 
     * @param snake     serpent à contrôler
     * @param gameModel modèle du jeu
     */
    void controlSnake(Snake snake, GameModel gameModel, double lastUpdate, Scene scene);
}
