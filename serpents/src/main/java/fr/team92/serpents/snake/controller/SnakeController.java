package fr.team92.serpents.snake.controller;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;

/**
 * Contrôleur de serpent
 */
public interface SnakeController {

    /**
     * Contrôle du serpent
     * @param snake serpent à contrôler
     * @param gameModel modèle du jeu
     */
    void controlSnake(Snake snake, GameModel gameModel);
}
