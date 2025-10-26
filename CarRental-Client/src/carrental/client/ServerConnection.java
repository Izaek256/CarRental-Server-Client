package carrental.client;


import java.io.*;
import java.net.*;

public class ServerConnection {
    private static ServerConnection instance;
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private static final String SERVER_HOST = "127.0.0.1";
    private static final int SERVER_PORT = 5000;
    
    private ServerConnection() {
        try {
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(stream);
            writer = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
        }
    }
    
    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection();
        }
        return instance;
    }
    
    public String sendRequest(String request) {
        try {
            writer.println(request);
            return reader.readLine();
        } catch (IOException e) {
            return "ERROR|Connection failed";
        }
    }
    
    public void close() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
        }
    }
}