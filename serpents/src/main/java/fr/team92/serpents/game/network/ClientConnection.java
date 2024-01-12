package fr.team92.serpents.game.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import com.google.gson.JsonObject;

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

    /**
     * Initialise le client avec le port et l'adresse spécifiés
     * @param port
     * @param address
     */
    public ClientConnection(int port, String address) {
        this.port = port;
        this.address = address;
    }

    /**
     * Connecte le client au serveur : initialise les flux d'entrée et de sortie, et lance un thread d'écoute du serveur.
     */
    public synchronized void connect() {
        try {
            socket = new Socket(address, port);
            out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            
            // Envoi d'un message de connexion au serveur
            JsonObject connectionMsg = new JsonObject();
            connectionMsg.addProperty("type", "connection");
            send(connectionMsg.toString());

            listeningThread = new Thread(this::listen);
            listeningThread.start();
        }
        catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de la connexion au serveur : " + e.getMessage());
            disconnect();
        }
    }

    /**
     * Déconnecte le client du serveur. Ferme les flux d'entrée et de sortie, et le socket.
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
            }
            else {
                System.err.println("[ERREUR] Erreur lors de la déconnexion du serveur : socket déjà fermée");
            }
            if (listeningThread != null && listeningThread.isAlive()) {
                listeningThread.interrupt();
            }
        }
        catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de la déconnexion du serveur : " + e.getMessage());
        }        
    }

    /**
     * Envoie des données au serveur si la socket est connectée
     * @param message Données à envoyer
     */
    public synchronized void send(String message) {
        if (out != null && socket != null && socket.isConnected()) {
            out.println(message);
        }
        else {
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
                System.out.println("[INFORMATION] Données reçues du serveur : " + inputLine);
            }
        }
        catch (IOException e) {
            System.err.println("[ERREUR] Erreur lors de l'écoute du serveur : " + e.getMessage());
        }
    }

}
