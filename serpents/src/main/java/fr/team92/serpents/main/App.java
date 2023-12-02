package fr.team92.serpents.main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.view.GameView;
import fr.team92.serpents.snake.bot.factory.AvoidWallsBotFactory;
import fr.team92.serpents.snake.controller.HumanSnakeController;
import fr.team92.serpents.snake.controller.KeyboardControl;
import fr.team92.serpents.snake.controller.SnakeController;
import fr.team92.serpents.snake.controller.SnakeEventControl;
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

        SnakeController botController = new AvoidWallsBotFactory().createBotController();
        Snake botSnake = new Snake(botController, 5, new Position(10, 10), Direction.EAST);

        Map<KeyCode, Direction> keyMap = new HashMap<>();
        keyMap.put(KeyCode.UP, Direction.NORTH);
        keyMap.put(KeyCode.DOWN, Direction.SOUTH);
        keyMap.put(KeyCode.LEFT, Direction.WEST);
        keyMap.put(KeyCode.RIGHT, Direction.EAST);
        SnakeEventControl snakeEventControl = new KeyboardControl(keyMap);

        SnakeController snakeController = new HumanSnakeController(snakeEventControl);
        Snake humanSnake = new Snake(snakeController, 5, new Position(10, 20), Direction.SOUTH);

        List<Snake> snakes = List.of(botSnake, humanSnake);

        GameModel model = new GameModel(80, 60, snakes);
        GameController controller = new GameController(model, scene);
        /* GameView view = */ new GameView(model, controller, root);
        

        stage.setTitle("Serpents");
        stage.setScene(scene);
        stage.show();        
    }

    public static void main(String[] args) {
        launch(args);
    }

}