/*
 * Client Handler - COMPLETE VERSION
 * Handles individual client requests in separate threads
 * Supports ALL tables: Cars, Branches, Insurance, Damages, EmployeeAssignments,
 * VehicleMaintenance, Rentals, Payments, Customers, Employees
 */
package carrental.server;

import java.io.*;
import java.net.*;
import java.sql.*;

/**
 * ClientHandler - Server Module
 * Processes client requests in a separate thread.
 * Implements the text-based protocol: ACTION|TABLE|DATA
 * Supported actions: ADD, UPDATE, DELETE, FIND, LIST, REPORT
 * Handles database operations for all system tables.
 *
 * @author Izaek Kisuule
 */
public class ClientHandler extends Thread {

    private final Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;
    private final int clientId;

    /**
     * Constructs a new ClientHandler for the given socket connection.
     * 
     * @param socket the client socket connection
     * @param clientId unique identifier for this client
     */
    public ClientHandler(Socket socket, int clientId) {
        this.socket = socket;
        this.clientId = clientId;
    }

    /**
     * Main thread execution method.
     * Sets up I/O streams, reads client requests, processes them, and sends responses.
     */
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
                if (socket != null) {
                    socket.close();
                }
                System.out.println("Client #" + clientId + " disconnected");
            } catch (IOException e) {
            }
        }
    }

    /**
     * Processes a client request and returns the appropriate response.
     * Request format: ACTION|TABLE|DATA
     * 
     * @param request the client request string
     * @return response string in format STATUS|MESSAGE or STATUS|DATA
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
                case "REPORT":
                    return handleReport(table, data);
                default:
                    return "ERROR|Unknown action: " + action;
            }

        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }
    
    /**
     * Handles REPORT action requests.
     * Delegates to ServerReportGenerator for PDF generation.
     * 
     * @param reportType the type of report (CUSTOMER, CAR, RENTAL, PAYMENT, MAINTENANCE)
     * @param data additional data for the report (e.g., date range)
     * @return success or error message with file path
     */
    private String handleReport(String reportType, String data) {
        try {
            switch (reportType) {
                case "CUSTOMER":
                    return ServerReportGenerator.generateCustomerReport();
                case "CAR":
                    return ServerReportGenerator.generateCarReport();
                case "RENTAL":
                    String[] dates = data.split(",");
                    if (dates.length != 2) {
                        return "ERROR|Invalid date range format. Expected: startDate,endDate";
                    }
                    return ServerReportGenerator.generateRentalReport(dates[0], dates[1]);
                case "PAYMENT":
                    return ServerReportGenerator.generatePaymentReport();
                case "MAINTENANCE":
                    return ServerReportGenerator.generateMaintenanceReport();
                default:
                    return "ERROR|Unknown report type: " + reportType;
            }
        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }

    // ==================== ADD OPERATIONS ====================
    /**
     * Handles ADD action requests for all tables.
     * 
     * @param table the target table name
     * @param data comma-separated field values
     * @return success or error message
     */
    private String handleAdd(String table, String data) {
        try (Connection conn = DbConnection.getConnection()) {
            String[] fields;
            if (table.equals("Damages")) {
                fields = data.split("\\|");
            } else {
                fields = data.split(",");
            }
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
                case "VehicleMaintenance":
                    return addMaintenance(conn, fields);
                case "Rentals":
                    return addRental(conn, fields);
                case "Payments":
                    return addPayment(conn, fields);
                case "Customers":
                    return addCustomer(conn, fields);
                case "Employees":
                    return addEmployee(conn, fields);
                default:
                    return "ERROR|Unknown table: " + table;
            }

        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }

    /**
     * Adds a new car record to the database.
     * 
     * @param conn database connection
     * @param fields car data fields: make, model, year, license_plate, rental_rate, status, color, mileage
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addCar(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO Cars(make, model, year, license_plate, rental_rate, status, color, mileage) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[0]);
        pst.setString(2, fields[1]);
        pst.setInt(3, Integer.parseInt(fields[2]));
        pst.setString(4, fields[3]);
        pst.setDouble(5, Double.parseDouble(fields[4]));
        pst.setString(6, fields[5]);
        pst.setString(7, fields[6]);
        pst.setInt(8, Integer.parseInt(fields[7]));
        pst.executeUpdate();
        return "SUCCESS|Car added successfully";
    }

    /**
     * Adds a new branch record to the database.
     * 
     * @param conn database connection
     * @param fields branch data fields: branch_name, address, city, phone_number, email, manager_id, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addBranch(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO branches(branch_name, address, city, phone_number, email, manager_id, status) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[0]);
        pst.setString(2, fields[1]);
        pst.setString(3, fields[2]);
        pst.setString(4, fields[3].isEmpty() ? null : fields[3]);
        pst.setString(5, fields[4].isEmpty() ? null : fields[4]);
        if (fields[5].isEmpty()) {
            pst.setNull(6, java.sql.Types.INTEGER);
        } else {
            pst.setInt(6, Integer.parseInt(fields[5]));
        }
        pst.setString(7, fields[6]);
        pst.executeUpdate();
        return "SUCCESS|Branch added successfully";
    }

    /**
     * Adds a new insurance record to the database.
     * 
     * @param conn database connection
     * @param fields insurance data fields: car_id, policy_number, insurance_company, coverage_amount, premium_amount, start_date, end_date, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addInsurance(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO insurance(car_id, policy_number, insurance_company, coverage_amount, premium_amount, start_date, end_date, status) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[0]));
        pst.setString(2, fields[1]);
        pst.setString(3, fields[2]);
        if (fields[3].isEmpty()) {
            pst.setNull(4, java.sql.Types.DECIMAL);
        } else {
            pst.setDouble(4, Double.parseDouble(fields[3]));
        }
        pst.setDouble(5, Double.parseDouble(fields[4]));
        pst.setDate(6, java.sql.Date.valueOf(fields[5]));
        pst.setDate(7, java.sql.Date.valueOf(fields[6]));
        pst.setString(8, fields[7]);
        pst.executeUpdate();
        return "SUCCESS|Insurance added successfully";
    }

    /**
     * Adds a new damage record to the database.
     * Handles pipe character replacement in description to avoid delimiter conflicts.
     * 
     * @param conn database connection
     * @param fields damage data fields: rental_id, car_id, description, repair_cost, reported_date, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addDamage(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO damages(rental_id, car_id, description, repair_cost, reported_date, status) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[0]));
        pst.setInt(2, Integer.parseInt(fields[1]));
        pst.setString(3, fields[2].replace("¦", "|"));
        if (fields[3].isEmpty()) {
            pst.setNull(4, java.sql.Types.DECIMAL);
        } else {
            pst.setDouble(4, Double.parseDouble(fields[3]));
        }
        pst.setDate(5, java.sql.Date.valueOf(fields[4]));
        pst.setString(6, fields[5]);
        pst.executeUpdate();
        return "SUCCESS|Damage record added successfully";
    }

    /**
     * Adds a new employee assignment record to the database.
     * 
     * @param conn database connection
     * @param fields assignment data fields: employee_id, branch_id, assignment_type, assignment_date, description, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addAssignment(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO employeeassignments(employee_id, branch_id, assignment_type, assignment_date, description, status) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[0]));
        pst.setInt(2, Integer.parseInt(fields[1]));
        pst.setString(3, fields[2]);
        pst.setDate(4, java.sql.Date.valueOf(fields[3]));
        pst.setString(5, fields[4]);
        pst.setString(6, fields[5]);
        pst.executeUpdate();
        return "SUCCESS|Assignment added successfully";
    }

    /**
     * Adds a new vehicle maintenance record to the database.
     * 
     * @param conn database connection
     * @param fields maintenance data fields: car_id, service_date, description, cost
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addMaintenance(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO vehiclemaintenance(car_id, service_date, description, cost) VALUES (?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[0]));
        pst.setDate(2, java.sql.Date.valueOf(fields[1]));
        pst.setString(3, fields[2]);
        pst.setDouble(4, Double.parseDouble(fields[3]));
        pst.executeUpdate();
        return "SUCCESS|Maintenance record added successfully";
    }

    /**
     * Adds a new rental record to the database.
     * 
     * @param conn database connection
     * @param fields rental data fields: customer_id, car_id, employee_id, start_date, end_date, total_amount, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addRental(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO rentals(customer_id, car_id, employee_id, start_date, end_date, total_amount, status) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[0]));
        pst.setInt(2, Integer.parseInt(fields[1]));
        pst.setInt(3, Integer.parseInt(fields[2]));
        pst.setDate(4, java.sql.Date.valueOf(fields[3]));
        pst.setDate(5, java.sql.Date.valueOf(fields[4]));
        pst.setDouble(6, Double.parseDouble(fields[5]));
        pst.setString(7, fields[6]);
        pst.executeUpdate();
        return "SUCCESS|Rental added successfully";
    }

    /**
     * Adds a new payment record to the database.
     * 
     * @param conn database connection
     * @param fields payment data fields: rental_id, amount, payment_date, payment_method, payment_status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addPayment(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO payments(rental_id, amount, payment_date, payment_method, payment_status) VALUES (?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[0]));
        pst.setDouble(2, Double.parseDouble(fields[1]));
        pst.setDate(3, java.sql.Date.valueOf(fields[2]));
        pst.setString(4, fields[3]);
        pst.setString(5, fields[4]);
        pst.executeUpdate();
        return "SUCCESS|Payment added successfully";
    }

    /**
     * Adds a new customer record to the database.
     * 
     * @param conn database connection
     * @param fields customer data fields: first_name, last_name, email, phone_number, address, license_number
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addCustomer(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO customers(first_name, last_name, email, phone_number, address, license_number) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[0]);
        pst.setString(2, fields[1]);
        pst.setString(3, fields[2]);
        pst.setString(4, fields[3].isEmpty() ? null : fields[3]);
        pst.setString(5, fields[4].isEmpty() ? null : fields[4]);
        pst.setString(6, fields[5].isEmpty() ? null : fields[5]);
        pst.executeUpdate();
        return "SUCCESS|Customer added successfully";
    }

    /**
     * Adds a new employee record to the database.
     * 
     * @param conn database connection
     * @param fields employee data fields: first_name, last_name, email, phone_number, address, password_hash
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String addEmployee(Connection conn, String[] fields) throws SQLException {
        String sql = "INSERT INTO employees_login(first_name, last_name, email, phone_number, address, password_hash) VALUES (?,?,?,?,?,?)";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[0]);
        pst.setString(2, fields[1]);
        pst.setString(3, fields[2]);
        pst.setString(4, fields[3].isEmpty() ? null : fields[3]);
        pst.setString(5, fields[4].isEmpty() ? null : fields[4]);
        pst.setString(6, fields[5]); // password_hash
        pst.executeUpdate();
        return "SUCCESS|Employee added successfully";
    }

    // ==================== UPDATE OPERATIONS ====================
    /**
     * Handles UPDATE action requests for all tables.
     * 
     * @param table the target table name
     * @param data comma-separated field values with ID as first field
     * @return success or error message
     */
    private String handleUpdate(String table, String data) {
        try (Connection conn = DbConnection.getConnection()) {
            String[] fields;
            if (table.equals("Damages")) {
                fields = data.split("\\|");
            } else {
                fields = data.split(",");
            }

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
                case "VehicleMaintenance":
                    return updateMaintenance(conn, fields);
                case "Rentals":
                    return updateRental(conn, fields);
                case "Payments":
                    return updatePayment(conn, fields);
                case "Customers":
                    return updateCustomer(conn, fields);
                case "Employees":
                    return updateEmployee(conn, fields);
                default:
                    return "ERROR|Unknown table: " + table;
            }

        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }

    /**
     * Updates an existing car record in the database.
     * 
     * @param conn database connection
     * @param fields car data fields: car_id, make, model, year, license_plate, rental_rate, status, color, mileage
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updateCar(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE Cars SET make=?, model=?, year=?, license_plate=?, rental_rate=?, status=?, color=?, mileage=? WHERE car_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[1]);
        pst.setString(2, fields[2]);
        pst.setInt(3, Integer.parseInt(fields[3]));
        pst.setString(4, fields[4]);
        pst.setDouble(5, Double.parseDouble(fields[5]));
        pst.setString(6, fields[6]);
        pst.setString(7, fields[7]);
        pst.setInt(8, Integer.parseInt(fields[8]));
        pst.setInt(9, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Car updated successfully";
    }

    /**
     * Updates an existing branch record in the database.
     * 
     * @param conn database connection
     * @param fields branch data fields: branch_id, branch_name, address, city, phone_number, email, manager_id, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updateBranch(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE branches SET branch_name=?, address=?, city=?, phone_number=?, email=?, manager_id=?, status=? WHERE branch_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[1]);
        pst.setString(2, fields[2]);
        pst.setString(3, fields[3]);
        pst.setString(4, fields[4].isEmpty() ? null : fields[4]);
        pst.setString(5, fields[5].isEmpty() ? null : fields[5]);
        if (fields[6].isEmpty()) {
            pst.setNull(6, java.sql.Types.INTEGER);
        } else {
            pst.setInt(6, Integer.parseInt(fields[6]));
        }
        pst.setString(7, fields[7]);
        pst.setInt(8, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Branch updated successfully";
    }

    /**
     * Updates an existing insurance record in the database.
     * 
     * @param conn database connection
     * @param fields insurance data fields: insurance_id, car_id, policy_number, insurance_company, coverage_amount, premium_amount, start_date, end_date, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updateInsurance(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE insurance SET car_id=?, policy_number=?, insurance_company=?, coverage_amount=?, premium_amount=?, start_date=?, end_date=?, status=? WHERE insurance_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[1]));
        pst.setString(2, fields[2]);
        pst.setString(3, fields[3]);
        if (fields[4].isEmpty()) {
            pst.setNull(4, java.sql.Types.DECIMAL);
        } else {
            pst.setDouble(4, Double.parseDouble(fields[4]));
        }
        pst.setDouble(5, Double.parseDouble(fields[5]));
        pst.setDate(6, java.sql.Date.valueOf(fields[6]));
        pst.setDate(7, java.sql.Date.valueOf(fields[7]));
        pst.setString(8, fields[8]);
        pst.setInt(9, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Insurance updated successfully";
    }

    /**
     * Updates an existing damage record in the database.
     * Handles pipe character replacement in description to avoid delimiter conflicts.
     * 
     * @param conn database connection
     * @param fields damage data fields: damage_id, rental_id, car_id, description, repair_cost, reported_date, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updateDamage(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE damages SET rental_id=?, car_id=?, description=?, repair_cost=?, reported_date=?, status=? WHERE damage_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[1]));
        pst.setInt(2, Integer.parseInt(fields[2]));
        pst.setString(3, fields[3].replace("¦", "|"));
        if (fields[4].isEmpty()) {
            pst.setNull(4, java.sql.Types.DECIMAL);
        } else {
            pst.setDouble(4, Double.parseDouble(fields[4]));
        }
        pst.setDate(5, java.sql.Date.valueOf(fields[5]));
        pst.setString(6, fields[6]);
        pst.setInt(7, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Damage updated successfully";
    }

    /**
     * Updates an existing employee assignment record in the database.
     * 
     * @param conn database connection
     * @param fields assignment data fields: assignment_id, employee_id, branch_id, assignment_type, assignment_date, description, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updateAssignment(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE employeeassignments SET employee_id=?, branch_id=?, assignment_type=?, assignment_date=?, description=?, status=? WHERE assignment_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[1]));
        pst.setInt(2, Integer.parseInt(fields[2]));
        pst.setString(3, fields[3]);
        pst.setDate(4, java.sql.Date.valueOf(fields[4]));
        pst.setString(5, fields[5]);
        pst.setString(6, fields[6]);
        pst.setInt(7, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Assignment updated successfully";
    }

    /**
     * Updates an existing vehicle maintenance record in the database.
     * 
     * @param conn database connection
     * @param fields maintenance data fields: maintenance_id, car_id, service_date, description, cost
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updateMaintenance(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE vehiclemaintenance SET car_id=?, service_date=?, description=?, cost=? WHERE maintenance_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[1]));
        pst.setDate(2, java.sql.Date.valueOf(fields[2]));
        pst.setString(3, fields[3]);
        pst.setDouble(4, Double.parseDouble(fields[4]));
        pst.setInt(5, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Maintenance updated successfully";
    }

    /**
     * Updates an existing rental record in the database.
     * 
     * @param conn database connection
     * @param fields rental data fields: rental_id, customer_id, car_id, employee_id, start_date, end_date, total_amount, status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updateRental(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE rentals SET customer_id=?, car_id=?, employee_id=?, start_date=?, end_date=?, total_amount=?, status=? WHERE rental_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[1]));
        pst.setInt(2, Integer.parseInt(fields[2]));
        pst.setInt(3, Integer.parseInt(fields[3]));
        pst.setDate(4, java.sql.Date.valueOf(fields[4]));
        pst.setDate(5, java.sql.Date.valueOf(fields[5]));
        pst.setDouble(6, Double.parseDouble(fields[6]));
        pst.setString(7, fields[7]);
        pst.setInt(8, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Rental updated successfully";
    }

    /**
     * Updates an existing payment record in the database.
     * 
     * @param conn database connection
     * @param fields payment data fields: payment_id, rental_id, amount, payment_date, payment_method, payment_status
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updatePayment(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE payments SET rental_id=?, amount=?, payment_date=?, payment_method=?, payment_status=? WHERE payment_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, Integer.parseInt(fields[1]));
        pst.setDouble(2, Double.parseDouble(fields[2]));
        pst.setDate(3, java.sql.Date.valueOf(fields[3]));
        pst.setString(4, fields[4]);
        pst.setString(5, fields[5]);
        pst.setInt(6, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Payment updated successfully";
    }

    /**
     * Updates an existing customer record in the database.
     * 
     * @param conn database connection
     * @param fields customer data fields: customer_id, first_name, last_name, email, phone_number, address, license_number
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updateCustomer(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE customers SET first_name=?, last_name=?, email=?, phone_number=?, address=?, license_number=? WHERE customer_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[1]);
        pst.setString(2, fields[2]);
        pst.setString(3, fields[3]);
        pst.setString(4, fields[4].isEmpty() ? null : fields[4]);
        pst.setString(5, fields[5].isEmpty() ? null : fields[5]);
        pst.setString(6, fields[6].isEmpty() ? null : fields[6]);
        pst.setInt(7, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Customer updated successfully";
    }

    /**
     * Updates an existing employee record in the database.
     * 
     * @param conn database connection
     * @param fields employee data fields: employee_id, first_name, last_name, email, phone_number, address, password_hash
     * @return success message
     * @throws SQLException if database error occurs
     */
    private String updateEmployee(Connection conn, String[] fields) throws SQLException {
        String sql = "UPDATE employees_login SET first_name=?, last_name=?, email=?, phone_number=?, address=?, password_hash=? WHERE employee_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setString(1, fields[1]);
        pst.setString(2, fields[2]);
        pst.setString(3, fields[3]);
        pst.setString(4, fields[4].isEmpty() ? null : fields[4]);
        pst.setString(5, fields[5].isEmpty() ? null : fields[5]);
        pst.setString(6, fields[6]);
        pst.setInt(7, Integer.parseInt(fields[0]));
        pst.executeUpdate();
        return "SUCCESS|Employee updated successfully";
    }

    // ==================== DELETE OPERATIONS ====================
    /**
     * Handles DELETE action requests for all tables.
     * 
     * @param table the target table name
     * @param data the ID of the record to delete
     * @return success or error message
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
                case "VehicleMaintenance":
                    sql = "DELETE FROM vehiclemaintenance WHERE maintenance_id=?";
                    break;
                case "Rentals":
                    sql = "DELETE FROM rentals WHERE rental_id=?";
                    break;
                case "Payments":
                    sql = "DELETE FROM payments WHERE payment_id=?";
                    break;
                case "Customers":
                    sql = "DELETE FROM customers WHERE customer_id=?";
                    break;
                case "Employees":
                    sql = "DELETE FROM employees_login WHERE employee_id=?";
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

    // ==================== FIND OPERATIONS ====================
    /**
     * Handles FIND action requests for all tables.
     * 
     * @param table the target table name
     * @param data the ID of the record to find
     * @return success with record data or error message
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
                case "VehicleMaintenance":
                    return findMaintenance(conn, id);
                case "Rentals":
                    return findRental(conn, id);
                case "Payments":
                    return findPayment(conn, id);
                case "Customers":
                    return findCustomer(conn, id);
                case "Employees":
                    return findEmployee(conn, id);
                default:
                    return "ERROR|Unknown table: " + table;
            }

        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }

    /**
     * Finds a car record by ID.
     * 
     * @param conn database connection
     * @param id the car ID to search for
     * @return success with car data or error message
     * @throws SQLException if database error occurs
     */
    private String findCar(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM Cars WHERE car_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return "SUCCESS|" + rs.getString("make") + ","
                    + rs.getString("model") + ","
                    + rs.getInt("year") + ","
                    + rs.getString("license_plate") + ","
                    + rs.getDouble("rental_rate") + ","
                    + rs.getString("status") + ","
                    + rs.getString("color") + ","
                    + rs.getInt("mileage");
        }
        return "ERROR|Car not found";
    }

    /**
     * Finds a branch record by ID.
     * 
     * @param conn database connection
     * @param id the branch ID to search for
     * @return success with branch data or error message
     * @throws SQLException if database error occurs
     */
    private String findBranch(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM branches WHERE branch_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String phone = rs.getString("phone_number");
            String email = rs.getString("email");
            int managerId = rs.getInt("manager_id");
            return "SUCCESS|" + rs.getString("branch_name") + ","
                    + rs.getString("address") + ","
                    + rs.getString("city") + ","
                    + (phone != null ? phone : "") + ","
                    + (email != null ? email : "") + ","
                    + (rs.wasNull() ? "" : managerId) + ","
                    + rs.getString("status");
        }
        return "ERROR|Branch not found";
    }

    /**
     * Finds an insurance record by ID.
     * 
     * @param conn database connection
     * @param id the insurance ID to search for
     * @return success with insurance data or error message
     * @throws SQLException if database error occurs
     */
    private String findInsurance(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM insurance WHERE insurance_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            double coverage = rs.getDouble("coverage_amount");
            return "SUCCESS|" + rs.getInt("car_id") + ","
                    + rs.getString("policy_number") + ","
                    + rs.getString("insurance_company") + ","
                    + (rs.wasNull() ? "" : coverage) + ","
                    + rs.getDouble("premium_amount") + ","
                    + rs.getDate("start_date") + ","
                    + rs.getDate("end_date") + ","
                    + rs.getString("status");
        }
        return "ERROR|Insurance not found";
    }

    /**
     * Finds a damage record by ID.
     * Handles pipe character replacement in description to avoid delimiter conflicts.
     * 
     * @param conn database connection
     * @param id the damage ID to search for
     * @return success with damage data using pipe delimiters or error message
     * @throws SQLException if database error occurs
     */
    private String findDamage(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM damages WHERE damage_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            int rentalId = rs.getInt("rental_id");
            int carId = rs.getInt("car_id");
            String description = rs.getString("description");

            // Check for null IMMEDIATELY after reading
            double cost = rs.getDouble("repair_cost");
            boolean costWasNull = rs.wasNull();

            java.sql.Date reportedDate = rs.getDate("reported_date");
            String status = rs.getString("status");

            // Replace pipe characters in description to avoid delimiter conflicts
            if (description != null) {
                description = description.replace("|", "¦");
            }

            // Build response with pipe delimiters
            return "SUCCESS|" + rentalId + "|"
                    + carId + "|"
                    + (description != null ? description : "") + "|"
                    + (costWasNull ? "" : cost) + "|"
                    + reportedDate + "|"
                    + status;
        }
        return "ERROR|Damage not found";
    }

    /**
     * Finds an employee assignment record by ID.
     * 
     * @param conn database connection
     * @param id the assignment ID to search for
     * @return success with assignment data or error message
     * @throws SQLException if database error occurs
     */
    private String findAssignment(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM employeeassignments WHERE assignment_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return "SUCCESS|" + rs.getInt("employee_id") + ","
                    + rs.getInt("branch_id") + ","
                    + rs.getString("assignment_type") + ","
                    + rs.getDate("assignment_date") + ","
                    + rs.getString("description") + ","
                    + rs.getString("status");
        }
        return "ERROR|Assignment not found";
    }

    /**
     * Finds a vehicle maintenance record by ID.
     * 
     * @param conn database connection
     * @param id the maintenance ID to search for
     * @return success with maintenance data or error message
     * @throws SQLException if database error occurs
     */
    private String findMaintenance(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM vehiclemaintenance WHERE maintenance_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return "SUCCESS|" + rs.getInt("car_id") + ","
                    + rs.getDate("service_date") + ","
                    + rs.getString("description") + ","
                    + rs.getDouble("cost");
        }
        return "ERROR|Maintenance record not found";
    }

    /**
     * Finds a rental record by ID.
     * 
     * @param conn database connection
     * @param id the rental ID to search for
     * @return success with rental data or error message
     * @throws SQLException if database error occurs
     */
    private String findRental(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM rentals WHERE rental_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return "SUCCESS|" + rs.getInt("customer_id") + ","
                    + rs.getInt("car_id") + ","
                    + rs.getInt("employee_id") + ","
                    + rs.getDate("start_date") + ","
                    + rs.getDate("end_date") + ","
                    + rs.getDouble("total_amount") + ","
                    + rs.getString("status");
        }
        return "ERROR|Rental not found";
    }

    /**
     * Finds a payment record by ID.
     * 
     * @param conn database connection
     * @param id the payment ID to search for
     * @return success with payment data or error message
     * @throws SQLException if database error occurs
     */
    private String findPayment(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM payments WHERE payment_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            return "SUCCESS|" + rs.getInt("rental_id") + ","
                    + rs.getDouble("amount") + ","
                    + rs.getDate("payment_date") + ","
                    + rs.getString("payment_method") + ","
                    + rs.getString("payment_status");
        }
        return "ERROR|Payment not found";
    }

    /**
     * Finds a customer record by ID.
     * 
     * @param conn database connection
     * @param id the customer ID to search for
     * @return success with customer data or error message
     * @throws SQLException if database error occurs
     */
    private String findCustomer(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String phone = rs.getString("phone_number");
            String address = rs.getString("address");
            String license = rs.getString("license_number");
            return "SUCCESS|" + rs.getString("first_name") + ","
                    + rs.getString("last_name") + ","
                    + rs.getString("email") + ","
                    + (phone != null ? phone : "") + ","
                    + (address != null ? address : "") + ","
                    + (license != null ? license : "");
        }
        return "ERROR|Customer not found";
    }

    /**
     * Finds an employee record by ID.
     * 
     * @param conn database connection
     * @param id the employee ID to search for
     * @return success with employee data or error message
     * @throws SQLException if database error occurs
     */
    private String findEmployee(Connection conn, int id) throws SQLException {
        String sql = "SELECT * FROM employees_login WHERE employee_id=?";
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.setInt(1, id);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            String phone = rs.getString("phone_number");
            String address = rs.getString("address");
            return "SUCCESS|" + rs.getString("first_name") + ","
                    + rs.getString("last_name") + ","
                    + rs.getString("email") + ","
                    + (phone != null ? phone : "") + ","
                    + (address != null ? address : "") + ","
                    + rs.getString("password_hash");
        }
        return "ERROR|Employee not found";
    }

    // ==================== LIST OPERATIONS ====================
    /**
     * Handles LIST action requests for all tables.
     * Returns a summary list of all records in the specified table.
     * 
     * @param table the target table name
     * @return success with semicolon-separated list of records or error message
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
                case "VehicleMaintenance":
                    return listMaintenance(conn);
                case "Rentals":
                    return listRentals(conn);
                case "Payments":
                    return listPayments(conn);
                case "Customers":
                    return listCustomers(conn);
                case "Employees":
                    return listEmployees(conn);
                default:
                    return "ERROR|Unknown table: " + table;
            }

        } catch (Exception e) {
            return "ERROR|" + e.getMessage();
        }
    }

    /**
     * Lists all cars with basic information.
     * Format: "car_id - make model"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of cars
     * @throws SQLException if database error occurs
     */
    private String listCars(Connection conn) throws SQLException {
        String sql = "SELECT car_id, make, model FROM Cars ORDER BY car_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            result.append(rs.getInt("car_id")).append(" - ")
                    .append(rs.getString("make")).append(" ")
                    .append(rs.getString("model"));
            first = false;
        }
        return result.toString();
    }

    /**
     * Lists all branches with basic information.
     * Format: "branch_id - branch_name"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of branches
     * @throws SQLException if database error occurs
     */
    private String listBranches(Connection conn) throws SQLException {
        String sql = "SELECT branch_id, branch_name FROM branches ORDER BY branch_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            result.append(rs.getInt("branch_id")).append(" - ")
                    .append(rs.getString("branch_name"));
            first = false;
        }
        return result.toString();
    }

    /**
     * Lists all insurance policies with basic information.
     * Format: "insurance_id - policy_number"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of insurance policies
     * @throws SQLException if database error occurs
     */
    private String listInsurance(Connection conn) throws SQLException {
        String sql = "SELECT insurance_id, policy_number FROM insurance ORDER BY insurance_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            result.append(rs.getInt("insurance_id")).append(" - ")
                    .append(rs.getString("policy_number"));
            first = false;
        }
        return result.toString();
    }

    /**
     * Lists all damage records with basic information.
     * Format: "damage_id - status"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of damages
     * @throws SQLException if database error occurs
     */
    private String listDamages(Connection conn) throws SQLException {
        String sql = "SELECT damage_id, status FROM damages ORDER BY damage_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            result.append(rs.getInt("damage_id")).append(" - ")
                    .append(rs.getString("status"));
            first = false;
        }
        return result.toString();
    }

    /**
     * Lists all employee assignments with basic information.
     * Format: "assignment_id - assignment_type"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of assignments
     * @throws SQLException if database error occurs
     */
    private String listAssignments(Connection conn) throws SQLException {
        String sql = "SELECT assignment_id, assignment_type FROM employeeassignments ORDER BY assignment_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            result.append(rs.getInt("assignment_id")).append(" - ")
                    .append(rs.getString("assignment_type"));
            first = false;
        }
        return result.toString();
    }

    /**
     * Lists all vehicle maintenance records with basic information.
     * Format: "maintenance_id - service_date"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of maintenance records
     * @throws SQLException if database error occurs
     */
    private String listMaintenance(Connection conn) throws SQLException {
        String sql = "SELECT maintenance_id, service_date FROM vehiclemaintenance ORDER BY maintenance_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            result.append(rs.getInt("maintenance_id")).append(" - ")
                    .append(rs.getDate("service_date"));
            first = false;
        }
        return result.toString();
    }

    /**
     * Lists all rentals with detailed information including customer and car details.
     * Format: "rental_id - customer_name - car_info : car_id"
     * Example: "2 - Alex Johnson - Toyota Camry : 5"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of rentals
     * @throws SQLException if database error occurs
     */
    private String listRentals(Connection conn) throws SQLException {
        String sql = "SELECT r.rental_id, c.first_name, c.last_name, "
                + "car.car_id, car.make, car.model "
                + "FROM rentals r "
                + "JOIN customers c ON r.customer_id = c.customer_id "
                + "JOIN Cars car ON r.car_id = car.car_id "
                + "ORDER BY r.rental_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            // Format: "rental_id - customer_name - car_info : car_id"
            // Example: "2 - Alex Johnson - Toyota Camry : 5"
            result.append(rs.getInt("rental_id")).append(" - ")
                    .append(rs.getString("first_name")).append(" ")
                    .append(rs.getString("last_name")).append(" - ")
                    .append(rs.getString("make")).append(" ")
                    .append(rs.getString("model")).append(" : ")
                    .append(rs.getInt("car_id"));
            first = false;
        }
        return result.toString();
    }

    /**
     * Lists all payments with basic information.
     * Format: "payment_id - payment_date - rental_id"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of payments
     * @throws SQLException if database error occurs
     */
    private String listPayments(Connection conn) throws SQLException {
        String sql = "SELECT payment_id, payment_date FROM payments ORDER BY payment_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            result.append(rs.getInt("payment_id")).append(" - ")
                    .append(rs.getDate("payment_date")).append(" - ").append("rental_id");
            first = false;
        }
        return result.toString();
    }

    /**
     * Lists all customers with basic information.
     * Format: "customer_id - first_name last_name"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of customers
     * @throws SQLException if database error occurs
     */
    private String listCustomers(Connection conn) throws SQLException {
        String sql = "SELECT customer_id, first_name, last_name FROM customers ORDER BY customer_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            result.append(rs.getInt("customer_id")).append(" - ")
                    .append(rs.getString("first_name")).append(" ")
                    .append(rs.getString("last_name"));
            first = false;
        }
        return result.toString();
    }

    /**
     * Lists all employees with basic information.
     * Format: "employee_id - first_name last_name"
     * 
     * @param conn database connection
     * @return success with semicolon-separated list of employees
     * @throws SQLException if database error occurs
     */
    private String listEmployees(Connection conn) throws SQLException {
        String sql = "SELECT employee_id, first_name, last_name FROM employees_login ORDER BY employee_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        StringBuilder result = new StringBuilder("SUCCESS|");
        boolean first = true;
        while (rs.next()) {
            if (!first) {
                result.append(";");
            }
            result.append(rs.getInt("employee_id")).append(" - ")
                    .append(rs.getString("first_name")).append(" ")
                    .append(rs.getString("last_name"));
            first = false;
        }
        return result.toString();
    }
}
