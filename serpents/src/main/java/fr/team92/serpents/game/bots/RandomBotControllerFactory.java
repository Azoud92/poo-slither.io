package fr.team92.serpents.game.bots;

import fr.team92.serpents.game.bots.strategy.RandomBotStrategy;

public final class RandomBotControllerFactory implements BotControllerFactory {

    @Override
    public BotController createBotController() {
        return new BotController(new RandomBotStrategy());
    }
    
}
