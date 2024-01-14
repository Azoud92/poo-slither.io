package fr.team92.serpents.snake.controller;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.bot.strategy.BotStrategy;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import javafx.scene.Scene;

/**
 * Cette classe est un contrôleur pour un serpent automatisé. Elle utilise une stratégie de déplacement
 * pour déterminer la direction du serpent.
 */
public final class BotController implements SnakeController {

    /**
     * Stratégie de déplacement du serpent
     */
    private final BotStrategy strategy;

    /**
     * Crée un nouveau contrôleur avec la stratégie de déplacement spécifiée.
     *
     * @param strategy la stratégie de déplacement à utiliser
     * @throws IllegalArgumentException si la stratégie est nulle
     */
    public BotController(BotStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("La stratégie ne peut pas être nulle");
        }
        this.strategy = strategy;
    }

    /**
     * Contrôle le serpent en utilisant la stratégie de déplacement. Met à jour la direction du serpent
     * en fonction de la stratégie.
     *
     * @param snake le serpent à contrôler
     * @param gameModel le modèle de jeu
     * @param lastUpdate le temps écoulé depuis la dernière mise à jour
     * @param scene la scène de jeu
     */
    @Override
    public void controlSnake(Snake snake, GameModel gameModel, double lastUpdate, Scene scene) {
        Direction move = strategy.detMove(snake, gameModel, lastUpdate);
        snake.setDirection(move);
    }

}
