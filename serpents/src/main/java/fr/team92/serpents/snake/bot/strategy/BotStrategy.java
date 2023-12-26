package fr.team92.serpents.snake.bot.strategy;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;

/**
 * Stratégie de déplacement du serpent
 */
public interface BotStrategy {

    /**
     * Détermine le déplacement du serpent
     * @param snake serpent à contrôler
     * @param gameModel modèle du jeu
     * @return direction du déplacement
     */
    Direction detMove(Snake snake, GameModel gameModel, double lastUpdate);
}
