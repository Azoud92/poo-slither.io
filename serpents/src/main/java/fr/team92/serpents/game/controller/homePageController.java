package fr.team92.serpents.game.controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.view.GameView;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;
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
    private int numberOfBots = 1;
    private int numberOfFood = 100;

    private KeyCode leftKey;
    private KeyCode rightKey;
    private KeyCode accelerateKey;

    @FXML
    public void initialize() {
        ButtonsAnimations.addAnimations(singlePlayerButton);
        ButtonsAnimations.addAnimations(multiPlayerButton);
        ButtonsAnimations.addAnimations(optionsButton);
        ButtonsAnimations.addAnimations(changeControlsButton);
        ButtonsAnimations.addAnimations(exitButton);
    }

    @FXML
    protected void singlePlayerClicked(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        Pane root = (Pane) scene.getRoot();
        GameModel model = new GameModel((int) scene.getWidth(), (int) scene.getHeight(), 20, numberOfFood);

        for (int i = 0; i < numberOfBots; i++) {
            Snake botSnake = Snake.CreateAvoidWallsBotSnake(5, new Position(20 + i * 5, 30),
                    new Direction(Math.PI / 2));
            model.addSnake(botSnake);
        }

        Snake playerSnake;
        if ("keyboard".equals(controlChoice)) {
            Map<KeyCode, Double> keyMap1 = new HashMap<>();
            keyMap1.put(rightKey, 6.0);
            keyMap1.put(leftKey, -6.0);
            keyMap1.put(accelerateKey, 0.0);

            playerSnake = Snake.CreateHumanKeyboardSnake(keyMap1, 5, new Position(55, 30),
                    new Direction(Math.PI / 2));
        } else {
            playerSnake = Snake.CreateHumanMouseSnake(5, new Position(35, 30), new Direction(Math.PI / 2));
        }

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
    protected void optionsButtonClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fr/team92/serpents/game/view/SettingsView.fxml"));
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

    public void setNumberOfBots(int numberOfBots) {
        this.numberOfBots = numberOfBots;
    }

    public void setNumberOfFood(int numberOfFood) {
        this.numberOfFood = numberOfFood;

    }

    public void setAccelerateKey(KeyCode accelerateKey) {
        this.accelerateKey = accelerateKey;
    }
}