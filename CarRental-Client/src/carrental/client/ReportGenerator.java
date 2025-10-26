package carrental.client;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.sql.*;
import javax.swing.JOptionPane;

public class ReportGenerator {

    // === 1. Customer Report ===
    public static void generateCustomerReport() {
        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream("CustomerReport.pdf"));
            doc.open();

            doc.add(new Paragraph("Customer Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            doc.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 3, 4, 3, 4});
            table.addCell("ID");
            table.addCell("Name");
            table.addCell("Email");
            table.addCell("Phone");
            table.addCell("Address");

            Connection conn = DbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Customers");
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("customer_id")));
                table.addCell(rs.getString("first_name") + " " + rs.getString("last_name"));
                table.addCell(rs.getString("email"));
                table.addCell(rs.getString("phone_number"));
                table.addCell(rs.getString("address"));
            }

            doc.add(table);
            doc.close();
            JOptionPane.showMessageDialog(null, "Customer Report Created!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // === 2. Car Report ===
    public static void generateCarReport() {
        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream("CarReport.pdf"));
            doc.open();

            doc.add(new Paragraph("Car Inventory Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            doc.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(6);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 3, 3, 2, 2, 2});
            table.addCell("ID");
            table.addCell("Make");
            table.addCell("Model");
            table.addCell("Year");
            table.addCell("Rate");
            table.addCell("Status");

            Connection conn = DbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Cars");
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("car_id")));
                table.addCell(rs.getString("make"));
                table.addCell(rs.getString("model"));
                table.addCell(rs.getString("year"));
                table.addCell(rs.getString("rental_rate"));
                table.addCell(rs.getString("status"));
            }

            doc.add(table);
            doc.close();
            JOptionPane.showMessageDialog(null, "Car Report Created!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // === 3. Rental Report ===
    public static void generateRentalReport(String startDate, String endDate) {
        try {
            Document doc = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(doc, new FileOutputStream("RentalReport.pdf"));
            doc.open();

            doc.add(new Paragraph("Rental Report (" + startDate + " to " + endDate + ")", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            doc.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 4, 4, 4, 3, 3, 3});
            table.addCell("ID");
            table.addCell("Customer");
            table.addCell("Car");
            table.addCell("Employee");
            table.addCell("Start");
            table.addCell("End");
            table.addCell("Total");

            Connection conn = DbConnection.getConnection();
            String sql = "SELECT r.rental_id, CONCAT(c.first_name,' ',c.last_name) AS customer, "
                    + "CONCAT(car.make,' ',car.model,'-',car.license_plate) AS car, "
                    + "CONCAT(e.first_name,' ',e.last_name) AS employee, "
                    + "r.start_date, r.end_date, r.total_amount "
                    + "FROM Rentals r "
                    + "JOIN Customers c ON r.customer_id=c.customer_id "
                    + "JOIN Cars car ON r.car_id=car.car_id "
                    + "JOIN Employees_login e ON r.employee_id=e.employee_id "
                    + "WHERE r.start_date >= ? AND r.end_date <= ?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setString(1, startDate);
            pst.setString(2, endDate);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("rental_id")));
                table.addCell(rs.getString("customer"));
                table.addCell(rs.getString("car"));
                table.addCell(rs.getString("employee"));
                table.addCell(rs.getString("start_date"));
                table.addCell(rs.getString("end_date"));
                table.addCell(rs.getString("total_amount"));
            }

            doc.add(table);
            doc.close();
            JOptionPane.showMessageDialog(null, "Rental Report Created!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // === 4. Payment Report ===
    public static void generatePaymentReport() {
        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream("PaymentReport.pdf"));
            doc.open();

            doc.add(new Paragraph("Payment Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            doc.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 3, 3, 3, 3});
            table.addCell("Payment ID");
            table.addCell("Rental ID");
            table.addCell("Amount");
            table.addCell("Date");
            table.addCell("Method");

            Connection conn = DbConnection.getConnection();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM Payments");
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("payment_id")));
                table.addCell(String.valueOf(rs.getInt("rental_id")));
                table.addCell(rs.getString("amount"));
                table.addCell(rs.getString("payment_date"));
                table.addCell(rs.getString("payment_method"));
            }

            doc.add(table);
            doc.close();
            JOptionPane.showMessageDialog(null, "Payment Report Created!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }

    // === 5. Maintenance Report ===
    public static void generateMaintenanceReport() {
        try {
            Document doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, new FileOutputStream("MaintenanceReport.pdf"));
            doc.open();

            doc.add(new Paragraph("Vehicle Maintenance Report", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
            doc.add(new Paragraph("\n"));

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2, 4, 3, 4, 3});
            table.addCell("ID");
            table.addCell("Car");
            table.addCell("Date");
            table.addCell("Description");
            table.addCell("Cost");

            Connection conn = DbConnection.getConnection();
            String sql = "SELECT m.maintenance_id, CONCAT(c.make,' ',c.model,'-',c.license_plate) AS car, "
                    + "m.service_date, m.description, m.cost "
                    + "FROM VehicleMaintenance m "
                    + "JOIN Cars c ON m.car_id=c.car_id";
            ResultSet rs = conn.createStatement().executeQuery(sql);
            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("maintenance_id")));
                table.addCell(rs.getString("car"));
                table.addCell(rs.getString("service_date"));
                table.addCell(rs.getString("description"));
                table.addCell(rs.getString("cost"));
            }

            doc.add(table);
            doc.close();
            JOptionPane.showMessageDialog(null, "Maintenance Report Created!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
    }
}
