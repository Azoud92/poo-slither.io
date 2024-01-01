package fr.team92.serpents.snake.bot.strategy;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;

/**
 * Stratégie de déplacement du serpent évitant les murs et l'auto-collision
 */
public final class AvoidWallsStrategy implements BotStrategy {

    @Override
    public Direction detMove(Snake snake, GameModel gameModel, double lastUpdate) {
        // si la direction actuelle est sure, on la retourne
        if (!wallCollisionRisk(snake, snake.getDirection(), gameModel, lastUpdate)) {
            return snake.getDirection();
        }

        // On génére un nouvel angle qui est soit l'angle actuel plus 0.1, soit l'angle
        // actuel moins 0.1
        double currentAngle = snake.getDirection().getAngle();
        double newAnglePlus = (currentAngle + 0.1) % (2 * Math.PI);
        double newAngleMinus = (currentAngle - 0.1) % (2 * Math.PI);
        if (newAngleMinus < 0) {
            newAngleMinus += 2 * Math.PI;
        }

        Direction newDirectionPlus = new Direction(newAnglePlus);
        Direction newDirectionMinus = new Direction(newAngleMinus);

        // on vérifie si la nouvelle direction est sûre
        if (!wallCollisionRisk(snake, newDirectionPlus, gameModel, lastUpdate)) {
            // Si la nouvelle direction est sûre, on la retourne
            return newDirectionPlus;
        } else {
            return newDirectionMinus;
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
