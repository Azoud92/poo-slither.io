package fr.team92.serpents.game.view;

import java.util.LinkedList;
import java.util.List;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.snake.model.segments.BurrowingSegmentBehavior;
import fr.team92.serpents.snake.model.segments.Segment;
import fr.team92.serpents.utils.Position;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * Cette classe représente le mode de jeu solo.
 */
public final class SinglePlayerMode implements GameMode {

    /**
     * Liste des cercles des serpents. Utilisée pour les dessiner après les segments
     */
    private List<Circle> snakeCircles;

    /**
     * Initialise le mode de jeu solo.
     */
    public SinglePlayerMode() {
        snakeCircles = new LinkedList<>();
    }

    /**
     * Dessine les segments du serpent sur le plateau de jeu.
     */
    @Override
    public void drawSegments(Pane pane, GameController controller) {
        pane.getChildren().removeIf(node -> node instanceof Circle);

        if (controller.getHumanSnake() == null) return;
        
        Segment headSegment = controller.getHumanSnake().getSegments().getFirst();
        Position headPos = headSegment.getPosition();
        
        int cellSize = controller.getCellSize();

        for (Segment segment : controller.getGrid().values()) {
            Position pos = segment.getPosition();
            double x = (pos.x() - headPos.x()) * cellSize + pane.getWidth() / 2.0;
            double y = (pos.y() - headPos.y()) * cellSize + pane.getHeight() / 2.0;
            double radius = segment.getDiameter() * cellSize / 2.0;

            // Si une partie du segment est à un bord de la grille, on le dessine aussi
            // de l'autre côté
            boolean left = x - radius < 0;
            boolean right = x + radius > pane.getWidth();
            boolean top = y - radius < 0;
            boolean bottom = y + radius > pane.getHeight();

            if (left) {
                drawSegment(pane, segment, cellSize, x + pane.getWidth(), y);
            } else if (right) {
                drawSegment(pane, segment, cellSize, x - pane.getWidth(), y);
            }
            if (top) {
                drawSegment(pane, segment, cellSize, x, y + pane.getHeight());
            } else if (bottom) {
                drawSegment(pane, segment, cellSize, x, y - pane.getHeight());
            }

            // Si le segment est dans un coin, on le dessine aussi dans le coin opposé
            if (left && top) {
                drawSegment(pane, segment, cellSize, x + pane.getWidth(), y + pane.getHeight());
            } else if (right && top) {
                drawSegment(pane, segment, cellSize, x - pane.getWidth(), y + pane.getHeight());
            } else if (right && bottom) {
                drawSegment(pane, segment, cellSize, x - pane.getWidth(), y - pane.getHeight());
            } else if (left && bottom) {
                drawSegment(pane, segment, cellSize, x + pane.getWidth(), y - pane.getHeight());
            }

            // Ensuite, on dessine le segment à sa position actuelle
            drawSegment(pane, segment, cellSize, x, y);
        }

        // ajout des cercles des serpents après les segments morts pour qu'ils soient au
        // dessus dans l'affichage
        for (Circle circle : snakeCircles) {
            pane.getChildren().add(circle);
        }
        snakeCircles.clear();
    }

    /**
     * Dessine un segment à une position donnée.
     * @param pane le Pane sur lequel dessiner le segment.
     * @param segment le Segment à dessiner.
     * @param cellSize la taille d'une cellule de la grille.
     * @param x la position x du segment.
     * @param y la position y du segment.
     */
    private void drawSegment(Pane pane, Segment segment, int cellSize, double x, double y) {
        double diameter = segment.getDiameter() * cellSize;
        Circle circle = new Circle(x, y, diameter / 2.0);
        if (segment.getBehavior() instanceof BurrowingSegmentBehavior) {
            circle.setFill(Color.BLUE);
        } else if (segment.isDead()) {
            circle.setFill(Color.ORANGE);
        }
        if (!(segment.getBehavior() instanceof BurrowingSegmentBehavior) && !segment.isDead()) {
            circle.setFill(Color.RED);
        }

        if (!segment.isDead()) {
            snakeCircles.add(circle);
        } else {
            pane.getChildren().add(circle);
        }
    }

    /**
     * Met à jour le score affiché.
     */
    @Override
    public void updateScore(Text scoreText, GameController controller, Pane pane) {
        scoreText.setText("Score : " + controller.getScore());
    }

}
