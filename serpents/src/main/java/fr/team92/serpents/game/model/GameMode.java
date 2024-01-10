package fr.team92.serpents.game.model;

import fr.team92.serpents.game.controller.GameController;
import javafx.scene.layout.Pane;

public sealed interface GameMode permits SinglePlayerMode, TwoPlayersMode {
    void drawSegments(Pane pane, GameController controller, int cellSize);
}
