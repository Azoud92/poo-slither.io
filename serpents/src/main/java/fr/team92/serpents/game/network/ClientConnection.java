package fr.team92.serpents.game.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import fr.team92.serpents.server.ServerMessageType;
import fr.team92.serpents.snake.controller.NetworkSnakeController;
import fr.team92.serpents.snake.controller.SnakeController;
import fr.team92.serpents.utils.Direction;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

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
    public ClientConnection(Parent root, int port, String address) {
        this.port = port;
        this.address = address;
        this.gson = new Gson();
        this.gameController = new GameControllerNetwork(root, this);
        this.gameModel = new GameModelNetwork();
        Pane pane = (Pane) root;
        this.gameView = new GameViewNetwork(pane, gameController, gameModel);
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

    public synchronized void changeDirection(String direction) {
        JsonObject directionMsg = new JsonObject();
        directionMsg.addProperty("type", "directionChange");
        directionMsg.addProperty("direction", direction);
        send(directionMsg.toString());
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

    private void handle_message(String msg) {
        JsonObject json = gson.fromJson(msg, JsonObject.class);
        ServerMessageType type = ServerMessageType.valueOf(json.get("type").getAsString());

        switch (type) {
            case SNAKE:
                SnakeController snakeController = new NetworkSnakeController();
                // Le serveur a indiqué que le serpent a été créé
                // Vous pouvez extraire les informations du serpent du message et les utiliser
                // pour créer le serpent dans votre jeu
                // Par exemple :
                double snakeX = json.get("head").getAsJsonObject().get("x").getAsDouble();
                double snakeY = json.get("head").getAsJsonObject().get("y").getAsDouble();

                break;
            case DIRECTION:

                double newAngle = json.get("angle").getAsDouble();
                direction = new Direction(newAngle);
                break;
            // ... autres cas ...
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
    private void listen() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Traitement des données reçues du serveur
                handle_message(inputLine);
                System.out.println("[INFORMATION] Données reçues du serveur : " + inputLine);
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

    public void sendAccel() {
        JsonObject accelMsg = new JsonObject();
        accelMsg.addProperty("type", "accelStart");
        send(accelMsg.toString());
    }

    public void stopAccel() {
        JsonObject stopAccelMsg = new JsonObject();
        stopAccelMsg.addProperty("type", "accelStop");
        send(stopAccelMsg.toString());
    }

}
