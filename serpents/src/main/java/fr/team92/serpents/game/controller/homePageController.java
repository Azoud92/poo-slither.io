package fr.team92.serpents.game.controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.view.GameView;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.io.IOException;

public class HomePageController {
    @FXML
    private Button singlePlayerButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private Button optionsButton;
    @FXML
    private Button changeControlsButton;
    @FXML
    private Button exitButton;

    private String controlChoice;
    private KeyCode leftKey;
    private KeyCode rightKey;

    @FXML
    public void initialize() {
        addAnimations(singlePlayerButton);
        addAnimations(multiPlayerButton);
        addAnimations(optionsButton);
        addAnimations(changeControlsButton);
        addAnimations(exitButton);
    }

    private void addAnimations(Button button) {
        // effet d'ombre quand la souris passe sur le bouton
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, (MouseEvent e) -> {
            button.setEffect(new DropShadow(20, Color.BLACK));
        });

        // Supprime l'effet d'ombre quand la souris quitte le bouton
        button.addEventHandler(MouseEvent.MOUSE_EXITED, (MouseEvent e) -> {
            button.setEffect(null);
        });

        // animation de zoom lorsque le bouton est cliqué
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent e) -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), button);
            st.setToX(1.5);
            st.setToY(1.5);
            st.setAutoReverse(true);
            st.setCycleCount(2);
            st.play();
        });
    }

    @FXML
    protected void singlePlayerClicked(ActionEvent event) {

        Snake botSnake = Snake.CreateAvoidWallsBotSnake(5, new Position(20, 30), new Direction(Math.PI / 2));

        Snake playerSnake;
        if ("keyboard".equals(controlChoice)) {
            Map<KeyCode, Double> keyMap1 = new HashMap<>();
            keyMap1.put(rightKey, 0.1);
            keyMap1.put(leftKey, -0.1);

            playerSnake = Snake.CreateHumanKeyboardSnake(keyMap1, 5, new Position(35, 30),
                    new Direction(Math.PI / 2));
        } else {
            playerSnake = Snake.CreateHumanMouseSnake(5, new Position(35, 30), new Direction(Math.PI / 2));
        }

        Scene scene = ((Node) event.getSource()).getScene();
        Pane root = (Pane) scene.getRoot();

        GameModel model = new GameModel((int) scene.getWidth(), (int) scene.getHeight(), 20);
        model.addSnake(botSnake);
        model.addSnake(playerSnake);

        GameController controller = new GameController(model, scene);
        /* GameView view = */new GameView(model, controller, root);

        controller.gameStart();
    }

    @FXML
    protected void multiplayerClicked(ActionEvent event) {
        System.out.println("Le bouton 'Multijoueur' a été cliqué");
    }

    @FXML
    protected void optionsButtonClicked(ActionEvent event) {
        System.out.println("Le bouton 'Paramètres' a été cliqué");
    }

    @FXML
    public void changeControlsClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fr/team92/serpents/game/view/ControlsView.fxml"));
            Parent root = loader.load();

            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.setScene(new Scene(root, currentStage.getWidth(), currentStage.getHeight()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void exitButtonClicked(ActionEvent event) {
        // Obtenir la fenêtre actuelle
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    public void setControlChoice(String controlChoice) {
        this.controlChoice = controlChoice;
    }

    public void setLeftKey(KeyCode leftKey) {
        this.leftKey = leftKey;
    }

    public void setRightKey(KeyCode rightKey) {
        this.rightKey = rightKey;
    }
}