package fr.team92.serpents.main;

import java.util.HashMap;
import java.util.Map;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.view.GameView;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public final class App extends Application {

    @Override
    public void start(Stage stage) {
        Pane root = new Pane();
        Scene scene = new Scene(root, 800, 600);

        Snake botSnake = Snake.CreateAvoidWallsBotSnake(5, new Position(20, 30), new Direction(Math.PI / 2));

        Map<KeyCode, Double> keyMap1 = new HashMap<>();
        keyMap1.put(KeyCode.RIGHT, 0.1);
        keyMap1.put(KeyCode.LEFT, -0.1);

        Snake humanSnake1 = Snake.CreateHumanKeyboardSnake(keyMap1, 5, new Position(35, 30),
                new Direction(Math.PI / 2));

        Map<KeyCode, Double> keyMap2 = new HashMap<>();
        keyMap2.put(KeyCode.Q, 0.05);
        keyMap2.put(KeyCode.D, -0.05);

        Snake humanMouseSnake = Snake.CreateHumanMouseSnake(5, new Position(50, 30), new Direction(Math.PI));

        GameModel model = new GameModel((int) scene.getWidth(), (int) scene.getHeight(), 10);
        model.addSnake(botSnake);
        //model.addSnake(humanSnake1);
        model.addSnake(humanMouseSnake);

        GameController controller = new GameController(model, scene);
        /* GameView view = */ new GameView(model, controller, root);

        stage.setTitle("Serpents");
        stage.setScene(scene);

        stage.setResizable(false);
        stage.setMinWidth(scene.getWidth());
        stage.setMaxWidth(scene.getWidth());
        stage.setMinHeight(scene.getHeight());
        stage.setMaxHeight(scene.getHeight());

        stage.show();

        controller.gameStart();
    }

    public static void main(String[] args) {
        launch(args);
    }

}