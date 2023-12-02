package fr.team92.serpents.game.bots.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.team92.serpents.game.model.Direction;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.model.Position;
import fr.team92.serpents.game.model.Segment;

public final class RandomBotStrategy implements BotStrategy {

    private Random random;

    public RandomBotStrategy() {
        this.random = new Random();
    }

    @Override
    public Direction getNextMove(GameModel model, Segment segment) {
        List<Direction> possibleMoves = new ArrayList<>();
        for (Direction direction : Direction.values()) {
            if (isSafeMove(model, segment, direction)) {
                possibleMoves.add(direction);
            }
        }

        if (possibleMoves.isEmpty()) {
            return Direction.values()[random.nextInt(Direction.values().length)];
        }
        return possibleMoves.get(random.nextInt(possibleMoves.size()));
    }

    /**
     * Returns true if the given move is safe.
     * @param model the game model
     * @param segment the segment to move
     * @param direction the direction to move
     * @return true if the given move is safe
     */
    private boolean isSafeMove(GameModel model, Segment segment, Direction direction) {
        Position newPosition = segment.simulateMove(direction);
        return newPosition.getX() >= 0 && newPosition.getX() < model.getWidth() &&
               newPosition.getY() >= 0 && newPosition.getY() < model.getHeight();
    }

}
