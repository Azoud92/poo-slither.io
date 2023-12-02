package fr.team92.serpents.game.controller;

import fr.team92.serpents.game.model.Direction;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.model.Segment;

public interface SegmentController {
    /**
     * Returns the next move to make.
     * @param model the game model
     * @param segment the segment to move
     * @return the next move to make
     */
    Direction getNextMove(GameModel model, Segment segment);
}
