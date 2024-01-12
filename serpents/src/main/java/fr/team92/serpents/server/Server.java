package fr.team92.serpents.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fr.team92.serpents.game.controller.GameController;
import fr.team92.serpents.game.model.GameModel;
import fr.team92.serpents.snake.model.Segment;
import fr.team92.serpents.snake.model.Snake;
import fr.team92.serpents.utils.Direction;
import fr.team92.serpents.utils.Position;

/**
 * Représente le serveur de jeu, qui écoute et les connexions des clients.
 * Le serveur gère l'état du jeu. Il délègue la gestion des clients à la classe ClientHandler.
 */
public final class Server {

    /**
     * Port par défaut sur lequel le serveur écoute les connexions des clients
     */
    private static final int DEFAULT_PORT = 13000;

    /*
     * Port sur lequel le serveur écoute les connexions des clients
     */
    private final int port;

    /**
     * Socket du serveur (pour écouter les connexions des clients)
     */
    private ServerSocket serverSocket;

    /**
     * Liste des clients connectés
     */
    private final List<ClientHandler> clientsHandlers;

    /**
     * Pool de threads pour exécuter les ClientHandler
     */
    private final ExecutorService executorService;

    /**
     * Modèle du jeu
     */
    private final GameModel gameModel;

    /**
     * Contrôleur du jeu
     */
    private final GameController gameController;

    /**
     * Largeur et hauteur de la zone de jeu, taille d'une cellule et nombre de nourriture
     */
    private final static int WIDTH = 5000, HEIGHT = 5000, CELL_SIZE = 20, NB_FOOD = 1000;

    /**
     * Initialise le serveur avec le port spécifié
     */
    public Server(int port) {
        this.port = port;
        this.clientsHandlers = new CopyOnWriteArrayList<>(); // Permet de parcourir la liste des clients connectés sans risque de ConcurrentModificationException
        this.executorService = Executors.newCachedThreadPool();
        this.gameModel = new GameModel(WIDTH, HEIGHT, CELL_SIZE, NB_FOOD);
        this.gameController = new GameController(gameModel);
        this.gameController.gameStart();
    }

    /**
     * Démarre le serveur et attend les connexions des clients.
     * Reste dans une boucle infinie tant que le serveur n'est pas arrêté, acceptant les nouvelles connexions clientes.
     */
    private synchronized void start() {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("[INFORMATION] Serveur démarré sur le port " + port
                + ". En attente de connexions...");
            while (!serverSocket.isClosed()) {
                // Traitement de la connexion du client
                Socket clientSocket = serverSocket.accept();
                System.out.println("[INFORMATION] Connexion établie (IP "
                    + clientSocket.getInetAddress().getHostAddress() + ")");
                ClientHandler clientHandler = new ClientHandler(clientSocket, this);
                clientsHandlers.add(clientHandler);
                gameModel.addObserver(clientHandler);
                executorService.execute(clientHandler);
            }
        }
        catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors du démarrage du serveur : " + e.getMessage());
        }
        finally {
            stop();
        }
    }

    /**
     * Arrête le serveur en fermant le socket lié et tous les ClientHandler actifs
     */
    private synchronized void stop() {
        try {
            if (serverSocket == null || serverSocket.isClosed()) {
                System.out.println("[ERREUR] Erreur lors de l'arrêt du serveur : celui-ci n'est pas démarré");
                return;
            }
            System.out.println("[INFORMATION] Arrêt du serveur...");
            serverSocket.close();
            executorService.shutdownNow();
            for (ClientHandler clientHandler : clientsHandlers) {
                clientHandler.closeConnection();
            }
            clientsHandlers.clear();
            System.out.println("[INFORMATION] Serveur arrêté");
        }
        catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de l'arrêt du serveur : " + e.getMessage());
        }
    }

    /**
     * Retire un client de la liste des clients connectés.
     * Cette méthode est appelée par un ClientHandler lorsqu'il se termine ou lorsque la connexion est interrompue.
     */
    public synchronized void removeClientHandler(ClientHandler clientHandler) {
        gameModel.removeObserver(clientHandler);
        clientsHandlers.remove(clientHandler);
    }

    /**
     * Ajoute un serpent au modèle du jeu
     * @param snake Serpent à ajouter
     */
    public synchronized void addSnake(Snake snake) {
        gameModel.addSnake(snake);
    }

    /**
     * Retire un serpent du modèle du jeu
     * @param snake Serpent à retirer
     */
    public synchronized void removeSnake(Snake snake) {
        if (!snake.isDead()) snake.die();
        gameModel.removeSnake(snake);
    }

    /**
     * Retourne une position libre pour un serpent (pour le placer lorsqu'il rejoint la partie)
     * @param minDistanceInit Distance minimale entre la tête du serpent et les autres serpents
     * @param segmentDiameter Diamètre d'un segment du serpent
     * @param initLength Longueur initiale du serpent
     * @param segmentSpacing Espacement entre les segments du serpent
     * @param startDirection Direction initiale du serpent
     * @return Position libre pour un serpent
     */
    public Position getFreePositionForSnake(double minDistanceInit, double segmentDiameter, int initLength,
            double segmentSpacing, Direction startDirection) {
        return gameModel.getFreePositionForSnake(minDistanceInit, segmentDiameter, initLength, segmentSpacing, startDirection);
    }

    /**
     * Retourne une copie du dictionnaire des positions occupées par les serpents et la nourriture sur la grille de jeu
     * @return Copie du dictionnaire des positions occupées par les serpents et la nourriture sur la grille de jeu
     */
    public synchronized Map<Position, Segment> getGrid() {
        return gameModel.getGrid();
    }

    /**
     * Retourne la largeur de la zone de jeu
     * @return Largeur de la zone de jeu
     */
    public double getWidth() {
        return WIDTH;
    }

    /**
     * Retourne la hauteur de la zone de jeu
     * @return Hauteur de la zone de jeu
     */
    public double getHeight() {
        return HEIGHT;
    }

    public static void main(String[] args) {
        try {
            int port = args.length > 0 ? Integer.parseInt(args[0]) : DEFAULT_PORT;
            Server server = new Server(port);
            server.start();
        }
        catch (NumberFormatException e) {
            System.err.println("[ERREUR] Le port spécifié n'est pas valide");
        }        
    }

}