package fr.team92.serpents.snake.bot.factory;

import fr.team92.serpents.snake.controller.BotController;

/**
 * Fabrique de contrôleur de serpent
 */
public interface BotControllerFactory {
    
    /**
     * Crée un contrôleur de serpent
     * @return contrôleur de serpent
     */
    BotController createBotController();
}
