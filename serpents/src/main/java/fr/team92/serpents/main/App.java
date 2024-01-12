package fr.team92.serpents.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public final class App extends Application {

    @Override
    public void start(Stage stage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fr/team92/serpents/game/view/homepage.fxml"));
            Scene scene = new Scene(root, 1000, 600);

            stage.setTitle("Serpents");
            stage.setScene(scene);
            stage.sizeToScene();

            /*stage.setResizable(false);
            stage.setMinWidth(scene.getWidth());
            stage.setMaxWidth(scene.getWidth());
            stage.setMinHeight(scene.getHeight());
            stage.setMaxHeight(scene.getHeight());*/

            stage.show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}