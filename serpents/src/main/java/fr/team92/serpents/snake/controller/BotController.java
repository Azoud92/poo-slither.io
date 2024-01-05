package fr.team92.serpents.snake.controller;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.bot.strategy.BotStrategy;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import javafx.scene.Scene;

/**
 * Contrôleur de serpent automatisé utilisant une stratégie de déplacement
 */
public final class BotController implements SnakeController {

    /**
     * Stratégie de déplacement du serpent
     */
    private final BotStrategy strategy;

    /**
     * Constructeur du contrôleur de serpent automatisé
     * 
     * @param strategy stratégie de déplacement du serpent
     */
    public BotController(BotStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("La stratégie ne peut pas être nulle");
        }
        this.strategy = strategy;
    }

    @Override
    public void controlSnake(Snake snake, GameModel gameModel, double lastUpdate, Scene scene) {
        Direction move = strategy.detMove(snake, gameModel, lastUpdate);
        snake.setDirection(move);
    }

}
