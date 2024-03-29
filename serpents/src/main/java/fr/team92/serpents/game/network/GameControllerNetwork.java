package fr.team92.serpents.game.network;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class GameControllerNetwork {
    private final ClientConnection clientConnection;
    private final Scene scene;
    private GameModelNetwork model;
    private final Map<KeyCode, Double> keyMap = new HashMap<>();
    private boolean isMouse = true;
    private boolean isAccelerating = false;
    private int cellSize;
    private Position headPos;

    public GameControllerNetwork(Scene scene, ClientConnection clientConnection) {
        this.clientConnection = clientConnection;
        this.scene = scene;
        setKeyListeners(scene);
        setMouseListeners(scene);

        Window window = scene.getWindow();
        window.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, event -> {
            clientConnection.disconnect();
        });
    }

    public void setKeyMap(KeyCode leftKey, KeyCode rightKey, KeyCode accelerateKey) {
        isMouse = false;
        keyMap.put(leftKey, 6.0);
        keyMap.put(rightKey, -6.0);
        keyMap.put(accelerateKey, 0.0);
    }

    private void setKeyListeners(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (keyMap.containsKey(event.getCode())) {
                if (keyMap.get(event.getCode()) == 0.0) {
                    if (!isAccelerating) {
                        clientConnection.sendAccel();
                        isAccelerating = true;
                    }
                } else {
                    double direction = keyMap.get(event.getCode());
                    clientConnection.sendDirectionUpdate(new Direction(direction));
                }
            }
        });
        scene.setOnKeyReleased(event -> {
            if (keyMap.containsKey(event.getCode())) {
                if (keyMap.get(event.getCode()) == 0.0) {
                    if (isAccelerating) {
                        clientConnection.stopAccel();
                        isAccelerating = false;
                    }
                }
            }
        });
    }

    private void setMouseListeners(Scene scene) {
        scene.setOnMouseMoved(event -> {
            if (isMouse) {
                if (clientConnection.getDirection() == null) {
                    return;
                }
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                Direction dir = calculateMouseDir(mouseX, mouseY);
                clientConnection.sendDirectionUpdate(dir);
            }
        });
        scene.setOnMouseDragged(event -> {
            if (isMouse) {
                if (clientConnection.getDirection() == null) {
                    return;
                }
                double mouseX = event.getSceneX();
                double mouseY = event.getSceneY();
                Direction dir = calculateMouseDir(mouseX, mouseY);
                clientConnection.sendDirectionUpdate(dir);
            }
        });

        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                clientConnection.sendAccel();
            }
        });
        scene.setOnMouseReleased(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                clientConnection.stopAccel();
            }
        });
    }

    private Direction calculateMouseDir(double mouseX, double mouseY) {

        double snakeX = clientConnection.getWidth() / 2;
        double snakeY = clientConnection.getHeight() / 2;

        double dx = mouseX - snakeX;
        double dy = mouseY - snakeY;

        double targetAngleInDegrees = (Math.toDegrees(Math.atan2(dy, dx))) % 360;

        double currentAngle = clientConnection.getDirection().angle();
        double angleDifference = targetAngleInDegrees - currentAngle;

        // Normalise la différence d'angle à l'intervalle [-180, 180)
        if (angleDifference > 180) {
            angleDifference -= 360;
        } else if (angleDifference < -180) {
            angleDifference += 360;
        }

        // Augmente ou diminue progressivement l'angle actuel du serpent pour atteindre
        // l'angle cible
        double angleChangeSpeed = 6; // Vitesse de changement d'angle
        if (angleDifference > 0) {
            currentAngle += Math.min(angleChangeSpeed, angleDifference);
        } else if (angleDifference < 0) {
            currentAngle -= Math.min(angleChangeSpeed, -angleDifference);
        }

        // Normalise l'angle actuel à l'intervalle [0, 360)
        if (currentAngle < 0) {
            currentAngle += 360;
        } else if (currentAngle >= 360) {
            currentAngle -= 360;
        }

        return new Direction(currentAngle);

    }

    public synchronized List<Segment> getSegments() {
        return model.getSegments();
    }

    public int getCellSize() {
        return cellSize;
    }

    public Position getHeadPos() {
        return headPos;
    }

    public void setCellSize(int size) {
        cellSize = size;

    }

    public void setHeadPos(Position pos) {
        headPos = pos;
    }

    public Pane getPane() {
        return (Pane) scene.getRoot();
    }

    public void setModel(GameModelNetwork model) {
        this.model = model;
    }

}
