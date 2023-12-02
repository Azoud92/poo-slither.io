package fr.team92.serpents.game.bots.strategy;

import fr.team92.serpents.game.model.Direction;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.model.Segment;

public interface BotStrategy {
    /**
     * Returns the next move to make.
     * @param model the game model
     * @param segment the segment to move
     * @return the next move to make
     */
    Direction getNextMove(GameModel model, Segment segment);
}
