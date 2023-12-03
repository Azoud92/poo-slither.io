package fr.team92.serpents.snake.bot.strategy;

import java.util.Random;
import java.util.stream.Collectors;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;

import java.util.Arrays;
import java.util.List;

/**
 * Stratégie de déplacement du serpent évitant les murs et l'auto-collision
 */
public final class AvoidWallsStrategy implements BotStrategy {

    private final Random random = new Random();

    @Override
    public Direction detMove(Snake snake, GameModel gameModel) {
        Direction[] directions = Direction.values();

        List<Direction> validDirections = Arrays.stream(directions)
                .filter(dir -> !wallCollisionRisk(snake, dir, gameModel) && !autoCollisionRisk(snake, dir))
                .collect(Collectors.toList());

        if (!validDirections.isEmpty()) {
            return validDirections.get(random.nextInt(validDirections.size()));
        } else {
            return directions[random.nextInt(directions.length)];
        }
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
        return eventualPos.x() < 0 || eventualPos.x() >= gameModel.getWidth() || eventualPos.y() < 0 || eventualPos.y() >= gameModel.getHeight();
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
