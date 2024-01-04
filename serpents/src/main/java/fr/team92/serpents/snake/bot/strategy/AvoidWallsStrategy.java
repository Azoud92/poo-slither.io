package fr.team92.serpents.snake.bot.strategy;

import java.util.Random;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;

/**
 * Stratégie de déplacement du serpent évitant les murs et l'auto-collision
 */
public final class AvoidWallsStrategy implements BotStrategy {

    private final Random random = new Random();
    private static final double RANDOM_CHANGE_PROBABILITY = 0.1;

    @Override
    public Direction detMove(Snake snake, GameModel gameModel, double lastUpdate) {
        // si la direction actuelle est sure, on la retourne
        if (random.nextDouble() > RANDOM_CHANGE_PROBABILITY
                &&
                !wallCollisionRisk(snake, snake.getDirection(), gameModel, lastUpdate)) {
            return snake.getDirection();
        }

        // On génére un angle aléatoire entre 0 et 360°
        double actualAngle = snake.getDirection().angle();
        actualAngle += random.nextInt(-45, 45);
        actualAngle %= 360;

        Direction newDirection = new Direction(actualAngle);

        // on vérifie si la nouvelle direction est sûre
        if (!wallCollisionRisk(snake, newDirection, gameModel, lastUpdate)) {
            // Si la nouvelle direction est sûre, on la retourne
            return newDirection;
        } else {
            // Si la nouvelle direction n'est pas sûre, on retourne la direction actuelle
            return snake.getDirection();
        }

    }

    /**
     * Vérifie si le serpent risque de rentrer dans un joueur
     * 
     * @param snake     Le serpent à déplacer
     * @param dir       La direction dans laquelle le serpent tenterait d'aller
     * @param gameModel Le modèle du jeu
     * @return true si le serpent risque de rentrer dans un joueur, false sinon
     */
    private boolean wallCollisionRisk(Snake snake, Direction dir, GameModel gameModel, double lastUpdate) {
        Position eventualPos = snake.getHeadPosition().move(dir, snake.getSpeed() * lastUpdate);
        return gameModel.collidesWithAnySnake(eventualPos, snake);

    }

}
