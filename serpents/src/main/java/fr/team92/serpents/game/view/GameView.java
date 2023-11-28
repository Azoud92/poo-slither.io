package fr.team92.serpents.game.view;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.utils.Observable;
import fr.team92.serpents.utils.Observer;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public final class GameView implements Observer {

    private GameController controller;
    private Pane pane;
    private static final int CELL_SIZE = 10;

    public GameView(Observable model, GameController controller, Pane pane) {
        this.pane = pane;
        this.controller = controller;

        model.addObserver(this);
        this.update();
    }

    @Override
    public void update() {
        this.pane.getChildren().clear();
        int width = this.controller.getWidth();
        int height = this.controller.getHeight();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Rectangle rect = new Rectangle(i * CELL_SIZE, j * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                rect.setFill(Color.TRANSPARENT);
                rect.setStroke(Color.RED);

                if (this.controller.getGrid()[i][j].isPresent()) {
                    rect.setFill(Color.RED);
                }
                this.pane.getChildren().add(rect);
            }
        }
    }
    
}
