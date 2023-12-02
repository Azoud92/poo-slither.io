package fr.team92.serpents.game.bots;

public interface BotControllerFactory {
    /**
     * Creates a new bot controller.
     * @return a new bot controller
     */
    BotController createBotController();
}
