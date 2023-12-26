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
    public Direction detMove(Snake snake, GameModel gameModel, double lastUpdate) {
        Direction[] directions = Direction.values();
    
        // Obtenir la direction actuelle du serpent
        Direction currentDirection = snake.getDirection();
    
        // Filtrer les directions pour éviter les collisions avec les murs
        List<Direction> validDirections = Arrays.stream(directions)
                .filter(dir -> !wallCollisionRisk(snake, dir, gameModel, lastUpdate))
                .collect(Collectors.toList());
    
        // Si la direction actuelle est sûre, continuer dans cette direction
        if (validDirections.contains(currentDirection)) {
            return currentDirection;
        }
    
        // Si la direction actuelle n'est pas sûre, choisir une autre direction
        if (!validDirections.isEmpty()) {
            return validDirections.get(random.nextInt(validDirections.size()));
        } else {
            // Si aucune direction n'est sûre, choisir au hasard (comportement de secours)
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
    private boolean wallCollisionRisk(Snake snake, Direction dir, GameModel gameModel, double lastUpdate) {
        Position eventualPos = snake.getHeadPosition().move(dir, snake.getSpeed() * lastUpdate);
        return eventualPos.x() < 0 || eventualPos.x() >= gameModel.getWidth() || eventualPos.y() < 0 || eventualPos.y() >= gameModel.getHeight();
    }

}
