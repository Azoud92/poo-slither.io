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

        Map<KeyCode, Direction> keyMap = new HashMap<>();
        keyMap.put(KeyCode.UP, Direction.NORTH);
        keyMap.put(KeyCode.DOWN, Direction.SOUTH);
        keyMap.put(KeyCode.LEFT, Direction.WEST);
        keyMap.put(KeyCode.RIGHT, Direction.EAST);
        
        Snake humanSnake = Snake.CreateHumanKeyboardSnake(keyMap, 5, new Position(20, 20), Direction.NORTH);

        GameModel model = new GameModel(80, 60);
        model.addSnake(botSnake);
        model.addSnake(humanSnake);
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