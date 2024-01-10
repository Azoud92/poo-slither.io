package fr.team92.serpents.game.controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import fr.team92.serpents.game.model.GameMode;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.model.SinglePlayerMode;
import fr.team92.serpents.game.model.TwoPlayersMode;
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
    private Button twoPlayerButton;
    @FXML
    private Button multiPlayerButton;
    @FXML
    private Button optionsButton;
    @FXML
    private Button changeControlsButton;
    @FXML
    private Button exitButton;

    private String controlChoice1;
    private String controlChoice2 = "keyboard";

    private int numberOfBots = 1;
    private int numberOfFood = 100;

    private KeyCode leftKey1, leftKey2 = KeyCode.LEFT;
    private KeyCode rightKey1, rightKey2 = KeyCode.RIGHT;
    private KeyCode accelerateKey1, accelerateKey2 = KeyCode.UP;

    @FXML
    public void initialize() {

        ButtonsAnimations.addAnimations(singlePlayerButton);
        ButtonsAnimations.addAnimations(twoPlayerButton);
        ButtonsAnimations.addAnimations(multiPlayerButton);
        ButtonsAnimations.addAnimations(optionsButton);
        ButtonsAnimations.addAnimations(changeControlsButton);
        ButtonsAnimations.addAnimations(exitButton);
    }

    @FXML
    protected void twoPlayerClicked(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        Pane root = (Pane) scene.getRoot();
        GameModel model = new GameModel((int) scene.getWidth(), (int) scene.getHeight(), 20, numberOfFood);

        for (int i = 0; i < numberOfBots; i++) {
            Snake botSnake = Snake.CreateAvoidWallsBotSnake(5, new Position(20 + i * 5, 30),
                    new Direction(Math.PI / 2));
            model.addSnake(botSnake);
        }

        Snake playerSnake1;
        if ("keyboard".equals(controlChoice1)) {
            Map<KeyCode, Double> keyMap1 = new HashMap<>();
            keyMap1.put(rightKey1, 6.0);
            keyMap1.put(leftKey1, -6.0);
            keyMap1.put(accelerateKey1, 0.0);

            playerSnake1 = Snake.CreateHumanKeyboardSnake(keyMap1, 5, new Position(55, 30),
                    new Direction(Math.PI / 2));
        } else {
            playerSnake1 = Snake.CreateHumanMouseSnake(5, new Position(35, 30), new Direction(Math.PI / 2));
        }

        Snake playerSnake2;
        if ("keyboard".equals(controlChoice2)) {
            Map<KeyCode, Double> keyMap2 = new HashMap<>();
            keyMap2.put(rightKey2, 6.0);
            keyMap2.put(leftKey2, -6.0);
            keyMap2.put(accelerateKey2, 0.0);

            playerSnake2 = Snake.CreateHumanKeyboardSnake(keyMap2, 5, new Position(65, 30),
                    new Direction(Math.PI / 2));
        } else {
            playerSnake2 = Snake.CreateHumanMouseSnake(5, new Position(45, 30), new Direction(Math.PI / 2));
        }

        model.addSnake(playerSnake1);
        model.addSnake(playerSnake2);

        GameController controller = new GameController(model, scene);
        GameMode gameMode = new TwoPlayersMode();
        /* GameView view = */new GameView(model, controller, root, gameMode);
        controller.gameStart();
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
        if ("keyboard".equals(controlChoice1)) {
            Map<KeyCode, Double> keyMap1 = new HashMap<>();
            keyMap1.put(rightKey1, 6.0);
            keyMap1.put(leftKey1, -6.0);
            keyMap1.put(accelerateKey1, 0.0);

            playerSnake = Snake.CreateHumanKeyboardSnake(keyMap1, 5, new Position(55, 30),
                    new Direction(Math.PI / 2));
        } else {
            playerSnake = Snake.CreateHumanMouseSnake(5, new Position(35, 30), new Direction(Math.PI / 2));
        }

        model.addSnake(playerSnake);

        GameController controller = new GameController(model, scene);
        GameMode gameMode = new SinglePlayerMode();
        /* GameView view = */new GameView(model, controller, root, gameMode);
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

    public void setControlChoice(String controlChoice, boolean isPlayer1) {
        if (isPlayer1) {
            this.controlChoice1 = controlChoice;
        } else {
            this.controlChoice2 = controlChoice;
        }
    }

    public void setLeftKey(KeyCode leftKey, boolean isPlayer1) {
        if (isPlayer1) {
            this.leftKey1 = leftKey;
        } else {
            this.leftKey2 = leftKey;
        }
    }

    public void setRightKey(KeyCode rightKey, boolean isPlayer1) {
        if (isPlayer1) {
            this.rightKey1 = rightKey;
        } else {
            this.rightKey2 = rightKey;
        }
    }

    public void setNumberOfBots(int numberOfBots) {
        this.numberOfBots = numberOfBots;
    }

    public void setNumberOfFood(int numberOfFood) {
        this.numberOfFood = numberOfFood;

    }

    public void setAccelerateKey(KeyCode accelerateKey, boolean isPlayer1) {
        if (isPlayer1) {
            this.accelerateKey1 = accelerateKey;
        } else {
            this.accelerateKey2 = accelerateKey;
        }
    }
}