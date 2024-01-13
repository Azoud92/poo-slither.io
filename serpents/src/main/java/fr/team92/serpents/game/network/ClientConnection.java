package fr.team92.serpents.game.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import fr.team92.serpents.server.ServerMessageType;
import fr.team92.serpents.snake.model.BurrowingSegmentBehavior;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * Représente un client connecté au serveur.
 */
public class ClientConnection {

    /**
     * Port sur lequel le client se connecte au serveur
     */
    private final int port;

    /**
     * Adresse IP du serveur
     */
    private final String address;

    /**
     * Socket du client (pour communiquer avec le serveur)
     */
    private Socket socket;

    /**
     * Flux d'entrée pour envoyer des données au serveur
     */
    private PrintWriter out;

    /**
     * Flux de sortie pour recevoir des données du serveur
     */
    private BufferedReader in;

    /**
     * Thread d'écoute du serveur
     */
    private Thread listeningThread;

    /*
     * Objet Gson pour sérialiser / désérialiser les données JSON
     */
    private final Gson gson;

    /*
     * Références vers les classes du modèle MVC du jeu en réseau
     */
    private final GameControllerNetwork gameController;
    private final GameModelNetwork gameModel;
    private final GameViewNetwork gameView;

    private double width, height;

    private Direction direction;

    /**
     * Initialise le client avec le port et l'adresse spécifiés
     * 
     * @param port
     * @param address
     */
    public ClientConnection(Scene scene, int port, String address) {
        this.port = port;
        this.address = address;
        this.gson = new Gson();
        this.gameModel = new GameModelNetwork();
        this.gameController = new GameControllerNetwork(scene, this);
        gameController.setModel(gameModel);
        this.gameView = new GameViewNetwork(gameController);
        gameModel.addObserver(gameView);

    }

    /**
     * Connecte le client au serveur : initialise les flux d'entrée et de sortie, et
     * lance un thread d'écoute du serveur.
     */
    public synchronized void connect(double windowWidth, double windowHeight) {
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            // Envoi d'un message de connexion au serveur
            JsonObject connectionMsg = new JsonObject();
            connectionMsg.addProperty("type", "connection");
            send(connectionMsg.toString());

            // Envoi des informations de la taille de la fenêtre
            sendWindowSize(windowWidth, windowHeight);
            this.width = windowWidth;
            this.height = windowHeight;

            listeningThread = new Thread(this::listen);
            listeningThread.start();
        } catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de la connexion au serveur : " + e.getMessage());
            disconnect();
        }
    }

    public synchronized void sendWindowSize(double width, double height) {
        JsonObject windowSizeMsg = new JsonObject();
        windowSizeMsg.addProperty("type", "window_size");
        windowSizeMsg.addProperty("width", width);
        windowSizeMsg.addProperty("height", height);
        send(windowSizeMsg.toString());
    }

    /**
     * Déconnecte le client du serveur. Ferme les flux d'entrée et de sortie, et le
     * socket.
     */
    public synchronized void disconnect() {
        try {
            // Envoi d'un message de déconnexion au serveur
            JsonObject disconnectionMsg = new JsonObject();
            disconnectionMsg.addProperty("type", "disconnection");
            send(disconnectionMsg.toString());

            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            } else {
                System.err.println("[ERREUR] Erreur lors de la déconnexion du serveur : socket déjà fermée");
            }
            if (listeningThread != null && listeningThread.isAlive()) {
                listeningThread.interrupt();
            }
        } catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de la déconnexion du serveur : " + e.getMessage());
        }
    }

    private synchronized void handle_message(String msg) {
        JsonObject json = gson.fromJson(msg, JsonObject.class);
        ServerMessageType type = ServerMessageType.valueOf(json.get("type").getAsString().toUpperCase());
        System.out.println(type);
        switch (type) {
            case DIRECTION:

                double newAngle = json.get("angle").getAsDouble();
                direction = new Direction(newAngle);
                break;

            case HEAD_POS:
                System.out.println("HEAD_POS");
                double headX = json.get("x").getAsDouble();
                double headY = json.get("y").getAsDouble();

                gameController.setHeadPos(new Position(headX, headY));
                break;

            case CELL_SIZE:
                int cellSize = json.get("cellSize").getAsInt();
                gameController.setCellSize(cellSize);
                break;

            case ERROR:
                String errorMessage = json.get("message").getAsString();
                System.err.println("[ERREUR] Message d'erreur du serveur : " + errorMessage);
                break;

            case VISIBLE_SEGMENTS:
                JsonArray segmentsArray = json.get("segments").getAsJsonArray();
                for (JsonElement segmentElement : segmentsArray) {
                    JsonObject segmentObject = segmentElement.getAsJsonObject();
                    Position position = gson.fromJson(segmentObject.get("position"), Position.class);
                    Segment segment = gson.fromJson(segmentObject.get("segment"), Segment.class);
                    segment.setPosition(position);
                    gameModel.addSegment(segment);
                }
                break;

            case SNAKE:
                JsonArray snakeArray = json.get("segments").getAsJsonArray();
                for (JsonElement segmentElement : snakeArray) {
                    JsonObject segmentObject = segmentElement.getAsJsonObject();
                    int diameter = segmentObject.get("diameter").getAsInt();
                    Position position = gson.fromJson(segmentObject.get("position"), Position.class);
                    try {
                        segmentObject.get("behavior").getAsString();
                        Segment segment = new Segment(position, diameter, new BurrowingSegmentBehavior());
                        segment.setPosition(position);
                        gameModel.addSegment(segment);
                    } catch (Exception e) {
                        Segment segment = new Segment(position, diameter, null);
                        segment.setPosition(position);
                        gameModel.addSegment(segment);
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * Envoie des données au serveur si la socket est connectée
     * 
     * @param message Données à envoyer
     */
    public synchronized void send(String message) {
        if (out != null && socket != null && socket.isConnected()) {
            out.println(message);
        } else {
            System.err.println("[ERREUR] Erreur lors de l'envoi de données au serveur : socket non connectée");
        }
    }

    /**
     * Ecoute les données envoyées par le serveur
     */
    private synchronized void listen() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Traitement des données reçues du serveur
                handle_message(inputLine);
            }
        } catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de l'écoute du serveur : " + e.getMessage());
        }
    }

    public void setKeyMap(KeyCode leftKey, KeyCode rightKey, KeyCode accelerateKey) {
        gameController.setKeyMap(leftKey, rightKey, accelerateKey);
    }

    public double getWidth() {
        return width;

    }

    public double getHeight() {
        return height;
    }

    public Direction getDirection() {
        return direction;

    }

    public void sendDirectionUpdate(Direction dir) {
        JsonObject directionMsg = new JsonObject();
        directionMsg.addProperty("type", "directionChange");
        directionMsg.addProperty("direction", dir.toString());
        send(directionMsg.toString());
    }

    public synchronized void sendAccel() {
        JsonObject accelMsg = new JsonObject();
        accelMsg.addProperty("type", "accelerate");
        send(accelMsg.toString());
    }

    public synchronized void stopAccel() {
        JsonObject stopAccelMsg = new JsonObject();
        stopAccelMsg.addProperty("type", "decelerate");
        send(stopAccelMsg.toString());
    }

}
