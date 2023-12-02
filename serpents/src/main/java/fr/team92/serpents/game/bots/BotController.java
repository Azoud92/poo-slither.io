package fr.team92.serpents.game.bots;

import fr.team92.serpents.game.bots.strategy.BotStrategy;
import fr.team92.serpents.game.controller.SegmentController;
import fr.team92.serpents.game.model.Direction;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.model.Segment;

public final class BotController implements SegmentController {

    private BotStrategy strategy;

    public BotController(BotStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public Direction getNextMove(GameModel model, Segment segment) {
        return strategy.getNextMove(model, segment);
    }
    
}
