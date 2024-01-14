package fr.team92.serpents.snake.bot.strategy;

import java.util.Random;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;

/**
 * Stratégie de déplacement du serpent classique (déplacement aléatoire)
 */
public final class ClassicStrategy implements BotStrategy {

    /**
     * Générateur de nombre aléatoire
     */
    private final Random random = new Random();

    /**
     * Probabilité de changer de direction
     */
    private static final double RANDOM_CHANGE_PROBABILITY = 0.1;

    @Override
    public Direction detMove(Snake snake, GameModel gameModel, double lastUpdate) {
        // si la direction actuelle est sure, on la retourne
        if (random.nextDouble() > RANDOM_CHANGE_PROBABILITY) {
            return snake.getDirection();
        }

        // On génére un angle aléatoire entre 0 et 360°
        double actualAngle = snake.getDirection().getAngle();
        actualAngle += random.nextInt(-45, 45);
        actualAngle %= 360;

        Direction newDirection = new Direction(actualAngle);

        return newDirection;
    }

}
