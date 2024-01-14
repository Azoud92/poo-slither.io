package fr.team92.serpents.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import fr.team92.serpents.game.network.ClientMessageType;
import fr.team92.serpents.game.view.GameMode;
import fr.team92.serpents.snake.controller.NetworkSnakeController;
import fr.team92.serpents.snake.controller.SnakeController;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.snake.model.segments.Segment;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Observer;
import fr.team92.serpents.utils.Position;

/**
 * Gère la communication entre un client et le serveur.
 * Cette classe est responsable de recevoir les messages du client,
 * traiter ces messages, et envoyer des réponses appropriées.
 */
public class ClientHandler extends Thread implements Observer {

    /**
     * Socket du client
     */
    private final Socket clientSocket;

    /**
     * Référence vers le serveur
     */
    private final Server server;

    /**
     * Flux d'entrée pour envoyer des données au client
     */
    private PrintWriter out;

    /**
     * Flux de sortie pour recevoir des données du client
     */
    private BufferedReader in;

    /**
     * Serpent contrôlé par le client
     */
    private Snake snake;
   
    /*
     * Objet Gson pour sérialiser / désérialiser les données JSON
     */
    private final Gson gson;

    /**
     * Largeur et hauteur de la zone de jeu côté client
     */
    private double width, height;

    /**
     * Initialise le client avec la socket et le serveur spécifiés
     */
    public ClientHandler(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.gson = new Gson();
        this.width = -1;
        this.height = -1;
    }

    /**
     * Configure les flux d'entrée / sortie et traite les messages entrants dans une boucle.
     * Reste actif jusqu'à ce que le client se déconnecte ou qu'une erreur survienne.
     */
    @Override
    public void run() {
        try {
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), StandardCharsets.UTF_8));

            String inputLine;
            while (!Thread.currentThread().isInterrupted() && in != null && (inputLine = in.readLine()) != null) {
                // Traitement des données reçues du client
                System.out.println("[INFORMATION] Données reçues du client (IP " + clientSocket.getInetAddress().getHostAddress() + ") : " + inputLine);                
                handle_message(inputLine);
            }
        } catch (IOException e) {
            if (!Thread.currentThread().isInterrupted())
                System.err.println(ServerError.CLIENT_THREAD_EXECUTION_ERROR + " (IP " + clientSocket.getInetAddress().getHostAddress() + ") : " + e.getMessage());
        }
        finally {
            closeConnection();
        }
    }

    /**
     * Crée et ajoute un serpent pour le client au jeu.
     * Si aucune position de départ valide n'est trouvée, un message d'erreur est envoyé au client.
     */
    private synchronized void createSnake() {
        Direction startDirection = Direction.random();

        Position startPosition = server.getFreePositionForSnake(Snake.MIN_DISTANCE_INIT, 
            Segment.DEFAULT_DIAMETER, Snake.INIT_LENGTH, Snake.SEGMENT_SPACING, startDirection);

        if (startPosition == null) {
            System.err.println(ServerError.NO_FREE_POSITION_FOR_SNAKE);
            // On envoie au client un message d'erreur
            send(gson.toJson(ServerError.NO_FREE_POSITION_FOR_SNAKE.toJSON()));
            closeConnection();
            return;
        }

        snake = Snake.CreateNetworkSnake(Snake.INIT_LENGTH, startPosition, startDirection);        
        server.addSnake(snake);

        System.out.println("[INFORMATION] Serpent créé (IP " + clientSocket.getInetAddress().getHostAddress() + ")");
        // On envoie au client les données du serpent
        send(gson.toJson(snake.toJSON()));
    }

    /**
     * Traite un message individuel reçu du client.
     * Réagit en fonction du type de message (connexion, déconnexion, taille de fenêtre, etc.).
     * 
     * @param msg Message reçu du client
     */
    private synchronized void handle_message(String msg) {
        JsonObject json = gson.fromJson(msg, JsonObject.class);
        ClientMessageType type = ClientMessageType.valueOf(json.get("type").getAsString().toUpperCase());

        switch (type) {
            case CONNECTION:                
                if (snake == null) {                    
                    createSnake();
                }
                else {
                    System.err.println(ServerError.SNAKE_ALREADY_CREATED);
                    gson.toJson(ServerError.SNAKE_ALREADY_CREATED.toJSON());
                    closeConnection();
                }
                break;

            case DISCONNECTION:
                closeConnection();
                break;

            case WINDOW_SIZE:
                double width = json.get("width").getAsDouble();
                double height = json.get("height").getAsDouble();

                if (width <= 0 || height <= 0 || width > server.getWidth() || height > server.getHeight()) {
                    System.err.println(ServerError.INVALID_WINDOW_SIZE);
                    gson.toJson(ServerError.INVALID_WINDOW_SIZE.toJSON());
                    closeConnection();
                    break;
                }
                this.width = width;
                this.height = height;
                break;

            case DIRECTION:
                if (snake == null) {
                    break;
                }

                double newAngle = json.get("angle").getAsDouble();
                Direction newDirection = new Direction(newAngle);

                SnakeController controller = snake.getController();
                if (controller != null && controller instanceof NetworkSnakeController) {
                    ((NetworkSnakeController) controller).addCommand(newDirection);
                }
                break;

            default:
                System.err.println(ServerError.UNKNOWN_RECEIVED_MESSAGE);
                break;
        }
    }

    /**
     * Envoie des données au client connecté.
     * Si le flux de sortie n'est pas initialisé, une erreur est affichée
     * 
     * @param data Données à envoyer
     */
    private synchronized void send(String data) {
        if (out != null) {
            out.println(data);
        }
        else {
            System.err.println(ServerError.DATA_SEND_ERROR + " (IP " + clientSocket.getInetAddress().getHostAddress() + ")");
        }
    }

    /**
     * Ferme proprement la connexion avec le client.
     * Ferme les flux d'entrée / sortie et informe le serveur de la déconnexion du client.
     */
    public synchronized void closeConnection() {
        try {
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            if (clientSocket != null && !clientSocket.isClosed()) {
                clientSocket.close();
            }
            server.removeClientHandler(this);
            System.out.println("[INFORMATION] Connexion client fermée (IP " + clientSocket.getInetAddress().getHostAddress() + ")");
        } catch (IOException e) {
            System.err.println(ServerError.CLIENT_CONNECTION_CLOSE_ERROR + " (IP " + clientSocket.getInetAddress().getHostAddress() + ") : " + e.getMessage());
        }
    }

    /**
     * Met à jour l'état du client basé sur les changements dans le jeu.
     * Envoie les segments visibles au client basé sur la position actuelle du serpent.
     */
    @Override
    public synchronized void update() {
        if (snake == null || width < 0 || height < 0) {
            return;
        }

        Map<Position, Segment> grid = server.getGrid();

        JsonObject message = new JsonObject();
        message.addProperty("type", ServerMessageType.VISIBLE_SEGMENTS.toString().toLowerCase());

        // On crée un JsonArray pour stocker les segments visibles
        JsonArray segmentsInView = new JsonArray();

        Position headPosition = snake.getHeadPosition();

        // Taille totale du modèle
        double modelWidth = server.getWidth();
        double modelHeight = server.getHeight();

        for (Map.Entry<Position, Segment> entry : grid.entrySet()) {
            Position segmentPosition = entry.getKey();
            Segment segment = entry.getValue();

            // On gère les cas où le serpent est proche des bords
            double distanceX = Math.min(Math.abs(headPosition.x() - segmentPosition.x()),
                                        modelWidth - Math.abs(headPosition.x() - segmentPosition.x()));
            double distanceY = Math.min(Math.abs(headPosition.y() - segmentPosition.y()),
                                        modelHeight - Math.abs(headPosition.y() - segmentPosition.y()));

            // On vérifie si le segment est dans le champ de vision
            if (distanceX <= width / 2 && distanceY <= height / 2) {
                JsonObject segmentData = new JsonObject();
                segmentData.add("position", segmentPosition.toJSON());
                segmentData.add("segment", segment.toJSON());

                segmentsInView.add(segmentData);
            }
        }

        message.add("array", segmentsInView);

        // On envoie les segments visibles au client
        send(message.toString());
    }

    @Override
    public GameMode getGameMode() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGameMode'");
    }

}