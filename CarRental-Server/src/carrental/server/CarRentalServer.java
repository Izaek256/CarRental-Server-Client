
/*
 * Car Rental Server
 * Main server class that listens for client connections
 */
package carrental.server;

import java.io.*;
import java.net.*;

/**
 * CarRentalServer - Server Module
 * Main server bootstrap class for the Car Rental System.
 * Listens on port 5000 for incoming client connections.
 * Spawns a new ClientHandler thread for each connected client to handle requests concurrently.
 *
 * @author Izaek Kisuule
 */
public class CarRentalServer {
    private static final int PORT = 5000;
    
    /**
     * Main entry point for the Car Rental Server.
     * Starts the server socket and accepts client connections in an infinite loop.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("Car Rental System Server");
        System.out.println("========================================");
        System.out.println("Starting server on port " + PORT + "...");
        
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running and waiting for clients...");
            System.out.println("Press Ctrl+C to stop the server");
            System.out.println("========================================\n");
            
            int clientCount = 0;
            
            while (true) {
                // Wait for client connection
                Socket clientSocket = serverSocket.accept();
                clientCount++;
                
                System.out.println("Client #" + clientCount + " connected from: " + 
                                   clientSocket.getInetAddress().getHostAddress());
                
                // Handle each client in a separate thread
                ClientHandler handler = new ClientHandler(clientSocket, clientCount);
                handler.start();
            }
            
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }
}