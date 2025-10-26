
/*
 * Car Rental Server
 * Main server class that listens for client connections
 */
package carrental.server;

import java.io.*;
import java.net.*;

/**
 *
 * @author Izaek Kisuule
 */
public class CarRentalServer {
    private static final int PORT = 5000;
    
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
            e.printStackTrace();
        }
    }
}