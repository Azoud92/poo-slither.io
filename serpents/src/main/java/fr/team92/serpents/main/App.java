package fr.team92.serpents.main;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.game.model.GameModel;
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

        GameModel model = new GameModel(80, 60);        
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