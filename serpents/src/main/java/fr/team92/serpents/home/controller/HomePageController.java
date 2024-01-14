package fr.team92.serpents.home.controller;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.game.network.ClientConnection;
import fr.team92.serpents.game.view.GameMode;
import fr.team92.serpents.game.view.GameView;
import fr.team92.serpents.game.view.SinglePlayerMode;
import fr.team92.serpents.game.view.TwoPlayersMode;
import fr.team92.serpents.home.model.SettingsModel;
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
    private Button optionsButton;
    @FXML
    private Button changeControlsButton;
    @FXML
    private Button exitButton;

    private String controlChoice1;
    private String controlChoice2 = "keyboard";

    private int numberOfBots = 5;
    private int numberOfFood = 100;

    private KeyCode leftKey1 = KeyCode.LEFT, leftKey2 = KeyCode.Q;
    private KeyCode rightKey1 = KeyCode.RIGHT, rightKey2 = KeyCode.D;
    private KeyCode accelerateKey1 = KeyCode.UP, accelerateKey2 = KeyCode.SPACE;

    @FXML
    public void initialize() {

        ButtonsAnimations.addAnimations(singlePlayerButton);
        ButtonsAnimations.addAnimations(twoPlayerButton);
        ButtonsAnimations.addAnimations(optionsButton);
        ButtonsAnimations.addAnimations(changeControlsButton);
        ButtonsAnimations.addAnimations(exitButton);
        SettingsModel settings = SettingsModel.loadSettings();
        if (settings != null) {
            numberOfBots = settings.getNumberOfBots();
            numberOfFood = settings.getNumberOfFood();
        }
    }

    @FXML
    protected void twoPlayerClicked(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        Pane root = (Pane) scene.getRoot();
        root.getChildren().clear();
        GameModel model = new GameModel((int) scene.getWidth(), (int) scene.getHeight(), 20, numberOfFood);

        for (int i = 0; i < numberOfBots; i++) {
            Snake botSnake = MakeBotSnake(model);
            model.addSnake(botSnake);
        }

        Snake playerSnake1 = makePlayer(model, true);
        Snake playerSnake2 = makePlayer(model, false);

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
        root.getChildren().clear();
        GameModel model = new GameModel((int) scene.getWidth(), (int) scene.getHeight(), 20, numberOfFood);

        for (int i = 0; i < numberOfBots; i++) {
            Snake botSnake = MakeBotSnake(model);
            model.addSnake(botSnake);
        }

        Snake playerSnake = makePlayer(model, true);

        model.addSnake(playerSnake);

        GameController controller = new GameController(model, scene);
        GameMode gameMode = new SinglePlayerMode();
        /* GameView view = */new GameView(model, controller, root, gameMode);
        controller.gameStart();
    }

    private Snake MakeBotSnake(GameModel model) {
        Random random = new Random();
        Position position;
        Snake botSnake;

        do {
            int x = random.nextInt(model.getWidth());
            int y = random.nextInt(model.getHeight());
            position = new Position(x, y);
            double direction = random.nextDouble() * 2 * Math.PI;
            botSnake = Snake.CreateAvoidWallsBotSnake(Snake.INIT_LENGTH, position, new Direction(direction));
        } while (!model.isValidSnake(botSnake));

        return botSnake;
    }

    private Snake makePlayer(GameModel model, boolean isPlayer1) {
        Snake playerSnake;
        if (isPlayer1) {
            if ("keyboard".equals(controlChoice1)) {
                Map<KeyCode, Double> keyMap1 = new HashMap<>();
                keyMap1.put(rightKey1, 6.0);
                keyMap1.put(leftKey1, -6.0);
                keyMap1.put(accelerateKey1, 0.0);

                Random random = new Random();
                Position position;
                do {
                    int x = random.nextInt(model.getWidth());
                    int y = random.nextInt(model.getHeight());
                    position = new Position(x, y);
                    double direction = random.nextDouble() * 2 * Math.PI;
                    playerSnake = Snake.CreateHumanKeyboardSnake(keyMap1, Snake.INIT_LENGTH, position,
                            new Direction(direction));
                } while (!model.isValidSnake(playerSnake));

            } else {

                Random random = new Random();
                Position position;
                do {
                    int x = random.nextInt(model.getWidth());
                    int y = random.nextInt(model.getHeight());
                    position = new Position(x, y);
                    double direction = random.nextDouble() * 2 * Math.PI;
                    playerSnake = Snake.CreateHumanMouseSnake(Snake.INIT_LENGTH, position, new Direction(direction));
                } while (!model.isValidSnake(playerSnake));
            }
        } else {
            if ("keyboard".equals(controlChoice2)) {
                Map<KeyCode, Double> keyMap2 = new HashMap<>();
                keyMap2.put(rightKey2, 6.0);
                keyMap2.put(leftKey2, -6.0);
                keyMap2.put(accelerateKey2, 0.0);

                Random random = new Random();
                Position position;
                do {
                    int x = random.nextInt(model.getWidth());
                    int y = random.nextInt(model.getHeight());
                    position = new Position(x, y);
                    double direction = random.nextDouble() * 2 * Math.PI;
                    playerSnake = Snake.CreateHumanKeyboardSnake(keyMap2, Snake.INIT_LENGTH, position,
                            new Direction(direction));
                } while (!model.isValidSnake(playerSnake));

            } else {

                Random random = new Random();
                Position position;
                do {
                    int x = random.nextInt(model.getWidth());
                    int y = random.nextInt(model.getHeight());
                    position = new Position(x, y);
                    double direction = random.nextDouble() * 2 * Math.PI;
                    playerSnake = Snake.CreateHumanMouseSnake(5, position, new Direction(direction));
                } while (!model.isValidSnake(playerSnake));
            }
        }
        return playerSnake;

    }

    @FXML
    protected void multiplayerClicked(ActionEvent event) throws InterruptedException {
        System.out.println("Le bouton 'Multijoueur' a été cliqué");
        ClientConnection clientConnection = new ClientConnection(13000, "localhost");
        clientConnection.connect();
        //clientConnection.disconnect();
    }

    @FXML
    public void changeControlsClicked(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/fr/team92/serpents/home/view/ControlsView.fxml"));
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
                    getClass().getResource("/fr/team92/serpents/home/view/SettingsView.fxml"));
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