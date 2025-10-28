package carrental.client;

import javax.swing.JOptionPane;
import java.io.*;
import java.net.Socket;

/**
 * Client-side Report Generator
 * Sends report requests to the server
 * 
 * @author Izaek Kisuule
 */
public class ReportGenerator {
    
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 5000;

    /**
     * Send a report request to the server and handle the response
     */
    private static String sendReportRequest(String reportType, String data) {
        Socket socket = null;
        BufferedReader reader = null;
        PrintWriter writer = null;
        
        try {
            // Connect to server
            socket = new Socket(SERVER_HOST, SERVER_PORT);
            
            // Setup streams
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new PrintWriter(socket.getOutputStream(), true);
            
            // Send request
            String request = "REPORT|" + reportType + (data.isEmpty() ? "" : "|" + data);
            writer.println(request);
            
            // Read response
            String response = reader.readLine();
            
            return response;
            
        } catch (Exception e) {
            return "ERROR|Connection failed: " + e.getMessage();
        } finally {
            try {
                if (reader != null) reader.close();
                if (writer != null) writer.close();
                if (socket != null) socket.close();
            } catch (IOException e) {
                // Ignore cleanup errors
            }
        }
    }
    
    /**
     * Parse server response and show appropriate message
     */
    private static void handleResponse(String response, String reportName) {
        if (response == null) {
            JOptionPane.showMessageDialog(null, 
                "No response from server", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String[] parts = response.split("\\|", 2);
        String status = parts[0];
        String message = parts.length > 1 ? parts[1] : "No message";
        
        if ("SUCCESS".equals(status)) {
            JOptionPane.showMessageDialog(null, 
                message, 
                reportName + " Generated", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, 
                message, 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // === 1. Customer Report ===
    public static void generateCustomerReport() {
        String response = sendReportRequest("CUSTOMER", "");
        handleResponse(response, "Customer Report");
    }

    // === 2. Car Report ===
    public static void generateCarReport() {
        String response = sendReportRequest("CAR", "");
        handleResponse(response, "Car Report");
    }

    // === 3. Rental Report ===
    public static void generateRentalReport(String startDate, String endDate) {
        // Validate dates
        if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Please provide both start and end dates", 
                "Invalid Input", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String response = sendReportRequest("RENTAL", startDate + "," + endDate);
        handleResponse(response, "Rental Report");
    }

    // === 4. Payment Report ===
    public static void generatePaymentReport() {
        String response = sendReportRequest("PAYMENT", "");
        handleResponse(response, "Payment Report");
    }

    // === 5. Maintenance Report ===
    public static void generateMaintenanceReport() {
        String response = sendReportRequest("MAINTENANCE", "");
        handleResponse(response, "Maintenance Report");
    }
    
    // === Optional: Test method to verify connection ===
    public static boolean testServerConnection() {
        try {
            Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
            socket.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}