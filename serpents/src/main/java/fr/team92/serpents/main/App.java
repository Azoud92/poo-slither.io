package fr.team92.serpents.main;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public final class App extends Application {

    @Override
    public void start(Stage stage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fr/team92/serpents/home/view/Homepage.fxml"));

            // Obtenir les dimensions de l'écran principal
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();

            Scene scene = new Scene(root, screenBounds.getWidth(), screenBounds.getHeight());

            stage.setTitle("Serpents");
            stage.setScene(scene);
            stage.sizeToScene();

            stage.setMinWidth(scene.getWidth());
            stage.setMaxWidth(scene.getWidth());
            stage.setMinHeight(scene.getHeight());
            stage.setMaxHeight(scene.getHeight());

            stage.setResizable(false);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}