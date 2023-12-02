package fr.team92.serpents.main;

import java.util.List;

import fr.team92.serpents.game.bots.BotController;
import fr.team92.serpents.game.bots.strategy.RandomBotStrategy;
import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.game.controller.SegmentController;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.model.Position;
import fr.team92.serpents.game.model.Segment;
import fr.team92.serpents.game.view.GameView;
import javafx.application.Application;
import javafx.scene.Scene;
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

        Segment player1 = new Segment(new Position(0, 0));
        Segment bot1 = new Segment(new Position(79, 59));        
        
        SegmentController bot1Controller = new BotController(new RandomBotStrategy());

        bot1.setController(bot1Controller);

        List<Segment> segments = List.of(player1, bot1);

        GameModel model = new GameModel(80, 60, segments);
        
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