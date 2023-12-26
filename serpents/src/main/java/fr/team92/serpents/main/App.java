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

        Snake botSnake = Snake.CreateAvoidWallsBotSnake(5, new Position(10, 10), Direction.NORTH);

        Map<KeyCode, Direction> keyMap1 = new HashMap<>();
        keyMap1.put(KeyCode.UP, Direction.NORTH);
        keyMap1.put(KeyCode.DOWN, Direction.SOUTH);
        keyMap1.put(KeyCode.LEFT, Direction.WEST);
        keyMap1.put(KeyCode.RIGHT, Direction.EAST);
        
        Snake humanSnake1 = Snake.CreateHumanKeyboardSnake(keyMap1, 5, new Position(20, 20), Direction.NORTH);

        Map<KeyCode, Direction> keyMap2 = new HashMap<>();
        keyMap2.put(KeyCode.Z, Direction.NORTH);
        keyMap2.put(KeyCode.X, Direction.SOUTH);
        keyMap2.put(KeyCode.Q, Direction.WEST);
        keyMap2.put(KeyCode.D, Direction.EAST);

        //Snake humanSnake2 = Snake.CreateHumanKeyboardSnake(keyMap2, 5, new Position(30, 30), Direction.NORTH);

        GameModel model = new GameModel(80, 60);
        model.addSnake(botSnake);
        model.addSnake(humanSnake1);
        //model.addSnake(humanSnake2);
        GameController controller = new GameController(model, scene);
        /* GameView view = */ new GameView(model, controller, root);        

        stage.setTitle("Serpents");
        stage.setScene(scene);
        stage.show();

        controller.gameStart();
    }

    public static void main(String[] args) {
        launch(args);
    }

}