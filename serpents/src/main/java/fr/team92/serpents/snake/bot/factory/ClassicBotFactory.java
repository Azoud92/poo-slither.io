package fr.team92.serpents.snake.bot.factory;

import fr.team92.serpents.snake.bot.strategy.ClassicStrategy;
import fr.team92.serpents.snake.controller.BotController;

/**
 * Fabrique de contrôleur de serpent classique (déplacement aléatoire)
 */
public final class ClassicBotFactory implements BotControllerFactory {

    @Override
    public BotController createBotController() {
        return new BotController(new ClassicStrategy());
    }

}
