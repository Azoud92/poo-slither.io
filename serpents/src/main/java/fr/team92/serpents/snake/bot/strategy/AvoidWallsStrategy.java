package fr.team92.serpents.snake.bot.strategy;

import java.util.Random;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;

import java.util.Arrays;

/**
 * Stratégie de déplacement du serpent évitant les murs et l'auto-collision
 */
public final class AvoidWallsStrategy implements BotStrategy {

    private Random random = new Random();

    @Override
    public Direction detMove(Snake snake, GameModel gameModel) {
        Direction[] directions = Direction.values();

        return Arrays.stream(directions)
                .filter(dir -> !wallCollisionRisk(snake, dir, gameModel) && !autoCollisionRisk(snake, dir))
                .findFirst()
                .orElse(directions[random.nextInt(directions.length)]);
    }

    /**
     * Vérifie si le serpent risque de rentrer dans un mur
     * @param snake Le serpent à déplacer
     * @param dir La direction dans laquelle le serpent tenterait d'aller
     * @param gameModel Le modèle du jeu
     * @return true si le serpent risque de rentrer dans un mur, false sinon
     */
    private boolean wallCollisionRisk(Snake snake, Direction dir, GameModel gameModel) {
        Position eventualPos = snake.getHeadPosition().move(dir);
        return eventualPos.getX() < 0 || eventualPos.getX() >= gameModel.getWidth() || eventualPos.getY() < 0 || eventualPos.getY() >= gameModel.getHeight();
    }

    /**
     * Vérifie si le serpent risque de rentrer dans lui-même
     * @param snake Le serpent à déplacer
     * @param dir La direction dans laquelle le serpent tenterait d'aller
     * @return true si le serpent risque de rentrer dans lui-même, false sinon
     */
    private boolean autoCollisionRisk(Snake snake, Direction dir) {
        Position eventualPos = snake.getHeadPosition().move(dir);
        return snake.getSegments().stream().anyMatch(s -> s.getPosition().equals(eventualPos));
    }

}
