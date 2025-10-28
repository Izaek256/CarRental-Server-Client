package carrental.server;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.sql.*;
import java.text.SimpleDateFormat;

/**
 * Server-side Report Generator
 * Generates PDF reports on the server and returns status to client
 * 
 * @author Izaek Kisuule
 */
public class ServerReportGenerator {

    // === 1. Customer Report ===
    public static String generateCustomerReport() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String filename = "CustomerReport_" + timestamp + ".pdf";
            
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Customer Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("\n"));
            
            // Metadata
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph meta = new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()), metaFont);
            meta.setAlignment(Element.ALIGN_RIGHT);
            doc.add(meta);
            doc.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 3, 3.5f, 2.5f, 3.5f});
            
            // Header styling
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            PdfPCell headerCell;
            
            String[] headers = {"ID", "Name", "Email", "Phone", "Address"};
            for (String header : headers) {
                headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(new BaseColor(52, 73, 94));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            // Data
            Connection conn = DbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Customers ORDER BY customer_id");
            
            int count = 0;
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("customer_id")));
                table.addCell(rs.getString("first_name") + " " + rs.getString("last_name"));
                table.addCell(rs.getString("email") != null ? rs.getString("email") : "N/A");
                table.addCell(rs.getString("phone_number") != null ? rs.getString("phone_number") : "N/A");
                table.addCell(rs.getString("address") != null ? rs.getString("address") : "N/A");
                count++;
            }

            doc.add(table);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("Total Customers: " + count, FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            
            doc.close();
            rs.close();
            conn.close();
            
            return "SUCCESS|Customer Report generated successfully: " + filename;
        } catch (Exception e) {
            return "ERROR|Failed to generate Customer Report: " + e.getMessage();
        }
    }

    // === 2. Car Report ===
    public static String generateCarReport() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String filename = "CarReport_" + timestamp + ".pdf";
            
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Car Inventory Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("\n"));
            
            // Metadata
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph meta = new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()), metaFont);
            meta.setAlignment(Element.ALIGN_RIGHT);
            doc.add(meta);
            doc.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 2.5f, 2.5f, 1.5f, 2f, 1.5f, 2f});
            
            // Header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            String[] headers = {"ID", "Make", "Model", "Year", "License", "Rate", "Status"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(new BaseColor(52, 73, 94));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            // Data
            Connection conn = DbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Cars ORDER BY car_id");
            
            int count = 0;
            int availableCount = 0;
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("car_id")));
                table.addCell(rs.getString("make"));
                table.addCell(rs.getString("model"));
                table.addCell(String.valueOf(rs.getInt("year")));
                table.addCell(rs.getString("license_plate"));
                table.addCell("$" + String.format("%.2f", rs.getDouble("rental_rate")));
                
                String status = rs.getString("status");
                table.addCell(status);
                
                count++;
                if ("Available".equalsIgnoreCase(status)) {
                    availableCount++;
                }
            }

            doc.add(table);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("Total Cars: " + count + " | Available: " + availableCount, 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            
            doc.close();
            rs.close();
            conn.close();
            
            return "SUCCESS|Car Report generated successfully: " + filename;
        } catch (Exception e) {
            return "ERROR|Failed to generate Car Report: " + e.getMessage();
        }
    }

    // === 3. Rental Report ===
    public static String generateRentalReport(String startDate, String endDate) {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String filename = "RentalReport_" + timestamp + ".pdf";
            
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Rental Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            
            Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12, BaseColor.GRAY);
            Paragraph subtitle = new Paragraph("Period: " + startDate + " to " + endDate, subtitleFont);
            subtitle.setAlignment(Element.ALIGN_CENTER);
            doc.add(subtitle);
            doc.add(new Paragraph("\n"));
            
            // Metadata
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph meta = new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()), metaFont);
            meta.setAlignment(Element.ALIGN_RIGHT);
            doc.add(meta);
            doc.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1f, 3f, 3.5f, 2.5f, 2f, 2f, 2f, 2f});
            
            // Header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            String[] headers = {"ID", "Customer", "Car", "Employee", "Start", "End", "Amount", "Status"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(new BaseColor(52, 73, 94));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            // Data
            Connection conn = DbConnection.getConnection();
            String sql = "SELECT r.rental_id, CONCAT(c.first_name,' ',c.last_name) AS customer, "
                    + "CONCAT(car.make,' ',car.model) AS car, "
                    + "CONCAT(e.first_name,' ',e.last_name) AS employee, "
                    + "r.start_date, r.end_date, r.total_amount, r.status "
                    + "FROM Rentals r "
                    + "JOIN Customers c ON r.customer_id=c.customer_id "
                    + "JOIN Cars car ON r.car_id=car.car_id "
                    + "JOIN Employees_login e ON r.employee_id=e.employee_id "
                    + "WHERE r.start_date >= ? AND r.end_date <= ? "
                    + "ORDER BY r.rental_id";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, startDate);
            pst.setString(2, endDate);
            ResultSet rs = pst.executeQuery();

            int count = 0;
            double totalRevenue = 0;
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("rental_id")));
                table.addCell(rs.getString("customer"));
                table.addCell(rs.getString("car"));
                table.addCell(rs.getString("employee"));
                table.addCell(rs.getString("start_date"));
                table.addCell(rs.getString("end_date"));
                
                double amount = rs.getDouble("total_amount");
                table.addCell("$" + String.format("%.2f", amount));
                table.addCell(rs.getString("status"));
                
                count++;
                totalRevenue += amount;
            }

            doc.add(table);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("Total Rentals: " + count + " | Total Revenue: $" + String.format("%.2f", totalRevenue), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            
            doc.close();
            rs.close();
            pst.close();
            conn.close();
            
            return "SUCCESS|Rental Report generated successfully: " + filename;
        } catch (Exception e) {
            return "ERROR|Failed to generate Rental Report: " + e.getMessage();
        }
    }

    // === 4. Payment Report ===
    public static String generatePaymentReport() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String filename = "PaymentReport_" + timestamp + ".pdf";
            
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Payment Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("\n"));
            
            // Metadata
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph meta = new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()), metaFont);
            meta.setAlignment(Element.ALIGN_RIGHT);
            doc.add(meta);
            doc.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 2f, 2.5f, 2.5f, 2.5f, 2f});
            
            // Header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            String[] headers = {"Pay ID", "Rental ID", "Amount", "Date", "Method", "Status"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(new BaseColor(52, 73, 94));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            // Data
            Connection conn = DbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Payments ORDER BY payment_id");
            
            int count = 0;
            double totalAmount = 0;
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("payment_id")));
                table.addCell(String.valueOf(rs.getInt("rental_id")));
                
                double amount = rs.getDouble("amount");
                table.addCell("$" + String.format("%.2f", amount));
                table.addCell(rs.getString("payment_date"));
                table.addCell(rs.getString("payment_method"));
                table.addCell(rs.getString("payment_status"));
                
                count++;
                totalAmount += amount;
            }

            doc.add(table);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("Total Payments: " + count + " | Total Amount: $" + String.format("%.2f", totalAmount), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            
            doc.close();
            rs.close();
            conn.close();
            
            return "SUCCESS|Payment Report generated successfully: " + filename;
        } catch (Exception e) {
            return "ERROR|Failed to generate Payment Report: " + e.getMessage();
        }
    }

    // === 5. Maintenance Report ===
    public static String generateMaintenanceReport() {
        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new java.util.Date());
            String filename = "MaintenanceReport_" + timestamp + ".pdf";
            
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream(filename));
            doc.open();

            // Title
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Vehicle Maintenance Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            doc.add(title);
            doc.add(new Paragraph("\n"));
            
            // Metadata
            Font metaFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            Paragraph meta = new Paragraph("Generated: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()), metaFont);
            meta.setAlignment(Element.ALIGN_RIGHT);
            doc.add(meta);
            doc.add(new Paragraph("\n"));

            // Table
            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1.5f, 4f, 2.5f, 5f, 2f});
            
            // Header
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
            String[] headers = {"ID", "Car", "Date", "Description", "Cost"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, headerFont));
                headerCell.setBackgroundColor(new BaseColor(52, 73, 94));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setPadding(8);
                table.addCell(headerCell);
            }

            // Data
            Connection conn = DbConnection.getConnection();
            String sql = "SELECT m.maintenance_id, CONCAT(c.make,' ',c.model,' (',c.license_plate,')') AS car, "
                    + "m.service_date, m.description, m.cost "
                    + "FROM VehicleMaintenance m "
                    + "JOIN Cars c ON m.car_id=c.car_id "
                    + "ORDER BY m.maintenance_id";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            
            int count = 0;
            double totalCost = 0;
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("maintenance_id")));
                table.addCell(rs.getString("car"));
                table.addCell(rs.getString("service_date"));
                table.addCell(rs.getString("description"));
                
                double cost = rs.getDouble("cost");
                table.addCell("$" + String.format("%.2f", cost));
                
                count++;
                totalCost += cost;
            }

            doc.add(table);
            doc.add(new Paragraph("\n"));
            doc.add(new Paragraph("Total Maintenance Records: " + count + " | Total Cost: $" + String.format("%.2f", totalCost), 
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
            
            doc.close();
            rs.close();
            conn.close();
            
            return "SUCCESS|Maintenance Report generated successfully: " + filename;
        } catch (Exception e) {
            return "ERROR|Failed to generate Maintenance Report: " + e.getMessage();
        }
    }
}