package carrental.client;


import java.io.*;
import java.net.*;

/**
 * ServerConnection - Client Module
 * Manages the singleton TCP socket connection to the Car Rental Server.
 * Provides centralized communication for all client requests using a text-based protocol.
 * Connects to server at 127.0.0.1:5000.
 */
public class ServerConnection {
    private static ServerConnection instance;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 5000;
    
    /**
     * Private constructor to enforce singleton pattern.
     * Initializes socket connection and I/O streams to the server.
     */
    private ServerConnection() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(stream);
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
        }
    }
    
    /**
     * Returns the singleton instance of ServerConnection.
     * Creates the instance if it doesn't exist.
     * 
     * @return the singleton ServerConnection instance
     */
    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }
    
    /**
     * Sends a request to the server and waits for the response.
     * Request format: ACTION|TABLE|DATA
     * 
     * @param request the formatted request string to send
     * @return the server's response string, or error message if communication fails
     */
    public String sendRequest(String request) {
        try {
            writer.println(request);
            return reader.readLine();
        } catch (IOException e) {
            return "ERROR|Connection failed";
        }
    }
    
    /**
     * Closes the connection to the server and releases all resources.
     */
    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
        }
    }
}