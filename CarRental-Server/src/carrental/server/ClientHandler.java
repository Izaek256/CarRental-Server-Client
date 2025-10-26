
/*
 * Client Handler
 * Handles individual client requests in separate threads
 */
package carrental.server;

import java.io.*;
import java.net.*;
import java.sql.*;

/**
 *
 * @author Izaek Kisuule
 */
public class ClientHandler extends Thread {
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private int clientId;
    
    public ClientHandler(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }
    
    @Override
    public void run() {
        try {
            // Setup input stream
            InputStreamReader stream = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(stream);
            
            // Setup output stream
            writer = new PrintWriter(socket.getOutputStream(), true);
            
            System.out.println("Client #" + clientId + " handler started");
            
            // Read client requests
            String request;
            while ((request = reader.readLine()) != null) {
                System.out.println("Client #" + clientId + " request: " + request);
                
                String response = processRequest(request);
                writer.println(response);
                
                System.out.println("Client #" + clientId + " response: " + response);
            }
            
        } catch (IOException e) {
            System.err.println("Client #" + clientId + " error: " + e.getMessage());
        } finally {
            try {
                if (socket != null) socket.close();
                System.out.println("Client #" + clientId + " disconnected");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Process client request
     * Format: ACTION|TABLE|DATA
     */
    private String processRequest(String request) {
        try {
            String[] parts = request.split("\\|", 3);
            
            if (parts.length < 2) {
                return "ERROR|Invalid request format";
            }
            
            String action = parts[0];
            String table = parts[1];
            String data = parts.length > 2 ? parts[2] : "";
            
            switch (action) {
                case "ADD":
                    return handleAdd(table, data);
                case "UPDATE":
                    return handleUpdate(table, data);
                case "DELETE":
                    return handleDelete(table, data);
                case "FIND":
                    return handleFind(table, data);
                case "LIST":
                    return handleList(table);
                default:
                    return "ERROR|Unknown action: " + action;
            }
            
        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }
    
    /**
     * Handle ADD requests
     */
    private String handleAdd(String table, String data) {
        try (Connection conn = DbConnection.getConnection()) {
            String[] fields = data.split(",");
            
            switch (table) {
                case "Cars":
                    return addCar(conn, fields);
                case "Branches":
                    return addBranch(conn, fields);
                case "Insurance":
                    return addInsurance(conn, fields);
                case "Damages":
                    return addDamage(conn, fields);
                case "EmployeeAssignments":
                    return addAssignment(conn, fields);
                default:
                    return "ERROR|Unknown table: " + table;
            }
            
        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }
    
    private String addCar(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO Cars(make, model, year, license_plate, rental_rate, status, color, mileage) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[0]); // make
        pst.setString(2, fields[1]); // model
        pst.setInt(3, Integer.parseInt(fields[2])); // year
        pst.setString(4, fields[3]); // plate
        pst.setDouble(5, Double.parseDouble(fields[4])); // rate
        pst.setString(6, fields[5]); // status
        pst.setString(7, fields[6]); // color
        pst.setInt(8, Integer.parseInt(fields[7])); // mileage
        pst.executeUpdate();
        return "SUCCESS|Car added successfully";
    }
    
    private String addBranch(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO branches(branch_name, address, city, phone_number, email, manager_id, status) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[0]); // branch_name
        pst.setString(2, fields[1]); // address
        pst.setString(3, fields[2]); // city
        pst.setString(4, fields[3].isEmpty() ? null : fields[3]); // phone
        pst.setString(5, fields[4].isEmpty() ? null : fields[4]); // email
        if (fields[5].isEmpty()) {
            pst.setNull(6, java.sql.Types.INTEGER);
        } else {
            pst.setInt(6, Integer.parseInt(fields[5])); // manager_id
        }
        pst.setString(7, fields[6]); // status
        pst.executeUpdate();
        return "SUCCESS|Branch added successfully";
    }
    
    private String addInsurance(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO insurance(car_id, policy_number, insurance_company, coverage_amount, premium_amount, start_date, end_date, status) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[0])); // car_id
        pst.setString(2, fields[1]); // policy_number
        pst.setString(3, fields[2]); // insurance_company
        if (fields[3].isEmpty()) {
            pst.setNull(4, java.sql.Types.DECIMAL);
        } else {
            pst.setDouble(4, Double.parseDouble(fields[3])); // coverage_amount
        }
        pst.setDouble(5, Double.parseDouble(fields[4])); // premium_amount
        pst.setDate(6, java.sql.Date.valueOf(fields[5])); // start_date
        pst.setDate(7, java.sql.Date.valueOf(fields[6])); // end_date
        pst.setString(8, fields[7]); // status
        pst.executeUpdate();
        return "SUCCESS|Insurance added successfully";
    }
    
    private String addDamage(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO damages(rental_id, car_id, description, repair_cost, reported_date, status) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[0])); // rental_id
        pst.setInt(2, Integer.parseInt(fields[1])); // car_id
        pst.setString(3, fields[2]); // description
        if (fields[3].isEmpty()) {
            pst.setNull(4, java.sql.Types.DECIMAL);
        } else {
            pst.setDouble(4, Double.parseDouble(fields[3])); // repair_cost
        }
        pst.setDate(5, java.sql.Date.valueOf(fields[4])); // reported_date
        pst.setString(6, fields[5]); // status
        pst.executeUpdate();
        return "SUCCESS|Damage record added successfully";
    }
    
    private String addAssignment(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO employeeassignments(employee_id, branch_id, assignment_type, assignment_date, description, status) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[0])); // employee_id
        pst.setInt(2, Integer.parseInt(fields[1])); // branch_id
        pst.setString(3, fields[2]); // assignment_type
        pst.setDate(4, java.sql.Date.valueOf(fields[3])); // assignment_date
        pst.setString(5, fields[4]); // description
        pst.setString(6, fields[5]); // status
        pst.executeUpdate();
        return "SUCCESS|Assignment added successfully";
    }
    
    /**
     * Handle UPDATE requests
     */
    private String handleUpdate(String table, String data) {
        try (Connection conn = DbConnection.getConnection()) {
            String[] fields = data.split(",");
            
            switch (table) {
                case "Cars":
                    return updateCar(conn, fields);
                case "Branches":
                    return updateBranch(conn, fields);
                case "Insurance":
                    return updateInsurance(conn, fields);
                case "Damages":
                    return updateDamage(conn, fields);
                case "EmployeeAssignments":
                    return updateAssignment(conn, fields);
                default:
                    return "ERROR|Unknown table: " + table;
            }
            
        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }
    
    private String updateCar(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE Cars SET make=?, model=?, year=?, license_plate=?, rental_rate=?, status=?, color=?, mileage=? WHERE car_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[1]); // make
        pst.setString(2, fields[2]); // model
        pst.setInt(3, Integer.parseInt(fields[3])); // year
        pst.setString(4, fields[4]); // plate
        pst.setDouble(5, Double.parseDouble(fields[5])); // rate
        pst.setString(6, fields[6]); // status
        pst.setString(7, fields[7]); // color
        pst.setInt(8, Integer.parseInt(fields[8])); // mileage
        pst.setInt(9, Integer.parseInt(fields[0])); // car_id
        pst.executeUpdate();
        return "SUCCESS|Car updated successfully";
    }
    
    private String updateBranch(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE branches SET branch_name=?, address=?, city=?, phone_number=?, email=?, manager_id=?, status=? WHERE branch_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[1]); // branch_name
        pst.setString(2, fields[2]); // address
        pst.setString(3, fields[3]); // city
        pst.setString(4, fields[4].isEmpty() ? null : fields[4]); // phone
        pst.setString(5, fields[5].isEmpty() ? null : fields[5]); // email
        if (fields[6].isEmpty()) {
            pst.setNull(6, java.sql.Types.INTEGER);
        } else {
            pst.setInt(6, Integer.parseInt(fields[6])); // manager_id
        }
        pst.setString(7, fields[7]); // status
        pst.setInt(8, Integer.parseInt(fields[0])); // branch_id
        pst.executeUpdate();
        return "SUCCESS|Branch updated successfully";
    }
    
    private String updateInsurance(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE insurance SET car_id=?, policy_number=?, insurance_company=?, coverage_amount=?, premium_amount=?, start_date=?, end_date=?, status=? WHERE insurance_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[1])); // car_id
        pst.setString(2, fields[2]); // policy_number
        pst.setString(3, fields[3]); // insurance_company
        if (fields[4].isEmpty()) {
            pst.setNull(4, java.sql.Types.DECIMAL);
        } else {
            pst.setDouble(4, Double.parseDouble(fields[4])); // coverage_amount
        }
        pst.setDouble(5, Double.parseDouble(fields[5])); // premium_amount
        pst.setDate(6, java.sql.Date.valueOf(fields[6])); // start_date
        pst.setDate(7, java.sql.Date.valueOf(fields[7])); // end_date
        pst.setString(8, fields[8]); // status
        pst.setInt(9, Integer.parseInt(fields[0])); // insurance_id
        pst.executeUpdate();
        return "SUCCESS|Insurance updated successfully";
    }
    
    private String updateDamage(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE damages SET rental_id=?, car_id=?, description=?, repair_cost=?, reported_date=?, status=? WHERE damage_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[1])); // rental_id
        pst.setInt(2, Integer.parseInt(fields[2])); // car_id
        pst.setString(3, fields[3]); // description
        if (fields[4].isEmpty()) {
            pst.setNull(4, java.sql.Types.DECIMAL);
        } else {
            pst.setDouble(4, Double.parseDouble(fields[4])); // repair_cost
        }
        pst.setDate(5, java.sql.Date.valueOf(fields[5])); // reported_date
        pst.setString(6, fields[6]); // status
        pst.setInt(7, Integer.parseInt(fields[0])); // damage_id
        pst.executeUpdate();
        return "SUCCESS|Damage updated successfully";
    }
    
    private String updateAssignment(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE employeeassignments SET employee_id=?, branch_id=?, assignment_type=?, assignment_date=?, description=?, status=? WHERE assignment_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[1])); // employee_id
        pst.setInt(2, Integer.parseInt(fields[2])); // branch_id
        pst.setString(3, fields[3]); // assignment_type
        pst.setDate(4, java.sql.Date.valueOf(fields[4])); // assignment_date
        pst.setString(5, fields[5]); // description
        pst.setString(6, fields[6]); // status
        pst.setInt(7, Integer.parseInt(fields[0])); // assignment_id
        pst.executeUpdate();
        return "SUCCESS|Assignment updated successfully";
    }
    
    /**
     * Handle DELETE requests
     */
    private String handleDelete(String table, String data) {
        try (Connection conn = DbConnection.getConnection()) {
            int id = Integer.parseInt(data);
            
            String sql;
            switch (table) {
                case "Cars":
                    sql = "DELETE FROM Cars WHERE car_id=?";
                    break;
                case "Branches":
                    sql = "DELETE FROM branches WHERE branch_id=?";
                    break;
                case "Insurance":
                    sql = "DELETE FROM insurance WHERE insurance_id=?";
                    break;
                case "Damages":
                    sql = "DELETE FROM damages WHERE damage_id=?";
                    break;
                case "EmployeeAssignments":
                    sql = "DELETE FROM employeeassignments WHERE assignment_id=?";
                    break;
                default:
                    return "ERROR|Unknown table: " + table;
            }
            
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, id);
            pst.executeUpdate();
            return "SUCCESS|Record deleted successfully";
            
        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }
    
    /**
     * Handle FIND requests
     */
    private String handleFind(String table, String data) {
        try (Connection conn = DbConnection.getConnection()) {
            int id = Integer.parseInt(data);
            
            switch (table) {
                case "Cars":
                    return findCar(conn, id);
                case "Branches":
                    return findBranch(conn, id);
                case "Insurance":
                    return findInsurance(conn, id);
                case "Damages":
                    return findDamage(conn, id);
                case "EmployeeAssignments":
                    return findAssignment(conn, id);
                default:
                    return "ERROR|Unknown table: " + table;
            }
            
        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }
    
    private String findCar(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM Cars WHERE car_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            return "SUCCESS|" + rs.getString("make") + "," +
                   rs.getString("model") + "," +
                   rs.getInt("year") + "," +
                   rs.getString("license_plate") + "," +
                   rs.getDouble("rental_rate") + "," +
                   rs.getString("status") + "," +
                   rs.getString("color") + "," +
                   rs.getInt("mileage");
        }
        return "ERROR|Car not found";
    }
    
    private String findBranch(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM branches WHERE branch_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            String phone = rs.getString("phone_number");
            String email = rs.getString("email");
            int managerId = rs.getInt("manager_id");
            return "SUCCESS|" + rs.getString("branch_name") + "," +
                   rs.getString("address") + "," +
                   rs.getString("city") + "," +
                   (phone != null ? phone : "") + "," +
                   (email != null ? email : "") + "," +
                   (rs.wasNull() ? "" : managerId) + "," +
                   rs.getString("status");
        }
        return "ERROR|Branch not found";
    }
    
    private String findInsurance(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM insurance WHERE insurance_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            double coverage = rs.getDouble("coverage_amount");
            return "SUCCESS|" + rs.getInt("car_id") + "," +
                   rs.getString("policy_number") + "," +
                   rs.getString("insurance_company") + "," +
                   (rs.wasNull() ? "" : coverage) + "," +
                   rs.getDouble("premium_amount") + "," +
                   rs.getDate("start_date") + "," +
                   rs.getDate("end_date") + "," +
                   rs.getString("status");
        }
        return "ERROR|Insurance not found";
    }
    
    private String findDamage(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM damages WHERE damage_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            double cost = rs.getDouble("repair_cost");
            return "SUCCESS|" + rs.getInt("rental_id") + "," +
                   rs.getInt("car_id") + "," +
                   rs.getString("description") + "," +
                   (rs.wasNull() ? "" : cost) + "," +
                   rs.getDate("reported_date") + "," +
                   rs.getString("status");
        }
        return "ERROR|Damage not found";
    }
    
    private String findAssignment(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM employeeassignments WHERE assignment_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            return "SUCCESS|" + rs.getInt("employee_id") + "," +
                   rs.getInt("branch_id") + "," +
                   rs.getString("assignment_type") + "," +
                   rs.getDate("assignment_date") + "," +
                   rs.getString("description") + "," +
                   rs.getString("status");
        }
        return "ERROR|Assignment not found";
    }
    
    /**
     * Handle LIST requests
     */
    private String handleList(String table) {
        try (Connection conn = DbConnection.getConnection()) {
            
            switch (table) {
                case "Cars":
                    return listCars(conn);
                case "Branches":
                    return listBranches(conn);
                case "Insurance":
                    return listInsurance(conn);
                case "Damages":
                    return listDamages(conn);
                case "EmployeeAssignments":
                    return listAssignments(conn);
                case "Employees":
                    return listEmployees(conn);
                case "Rentals":
                    return listRentals(conn);
                default:
                    return "ERROR|Unknown table: " + table;
            }
            
        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }
    
    private String listCars(Connection conn) throws SQLException {
        String sql = "SELECT car_id, make, model FROM Cars ORDER BY car_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) result.append(";");
            result.append(rs.getInt("car_id")).append(" - ")
                  .append(rs.getString("make")).append(" ")
                  .append(rs.getString("model"));
            first = false;
        }
        return result.toString();
    }
    
    private String listBranches(Connection conn) throws SQLException {
        String sql = "SELECT branch_id, branch_name FROM branches ORDER BY branch_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) result.append(";");
            result.append(rs.getInt("branch_id")).append(" - ")
                  .append(rs.getString("branch_name"));
            first = false;
        }
        return result.toString();
    }
    
    private String listInsurance(Connection conn) throws SQLException {
        String sql = "SELECT insurance_id, policy_number FROM insurance ORDER BY insurance_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) result.append(";");
            result.append(rs.getInt("insurance_id")).append(" - ")
                  .append(rs.getString("policy_number"));
            first = false;
        }
        return result.toString();
    }
    
    private String listDamages(Connection conn) throws SQLException {
        String sql = "SELECT damage_id, status FROM damages ORDER BY damage_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) result.append(";");
            result.append(rs.getInt("damage_id")).append(" - ")
                  .append(rs.getString("status"));
            first = false;
        }
        return result.toString();
    }
    
    private String listAssignments(Connection conn) throws SQLException {
        String sql = "SELECT assignment_id, assignment_type FROM employeeassignments ORDER BY assignment_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) result.append(";");
            result.append(rs.getInt("assignment_id")).append(" - ")
                  .append(rs.getString("assignment_type"));
            first = false;
        }
        return result.toString();
    }
    
    private String listEmployees(Connection conn) throws SQLException {
        String sql = "SELECT employee_id, username FROM employees_login ORDER BY employee_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) result.append(";");
            result.append(rs.getInt("employee_id")).append(" - ")
                  .append(rs.getString("username"));
            first = false;
        }
        return result.toString();
    }
    
    private String listRentals(Connection conn) throws SQLException {
        String sql = "SELECT rental_id FROM rentals ORDER BY rental_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) result.append(";");
            result.append(rs.getInt("rental_id"));
            first = false;
        }
        return result.toString();
    }
}