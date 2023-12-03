package fr.team92.serpents.snake.bot.factory;

import fr.team92.serpents.snake.bot.strategy.AvoidWallsStrategy;
import fr.team92.serpents.snake.controller.BotController;

/**
 * Fabrique de contrôleur de serpent évitant les murs
 */
public final class AvoidWallsBotFactory implements BotControllerFactory {

    @Override
    public BotController createBotController() {
        return new BotController(new AvoidWallsStrategy());
    }

}
