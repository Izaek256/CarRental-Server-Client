/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carrental.server.client;

import javax.swing.JOptionPane;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.*;
import com.toedter.calendar.JDateChooser;
import java.time.temporal.ChronoUnit;

/**
 *
 * @author Izaek Kisuule
 */
public class CarAssignment extends javax.swing.JFrame {

    /**
     * Creates new form CarAssignment
     */
    public CarAssignment() {
        initComponents();
        setSize(800, 600);
        setTitle("Car Rental Form - Car_Rental_System");
        setLocationRelativeTo(null);
        loadCustomers();
        loadCars();
        loadEmployees();
        calculateAmount();
        loadRentalId();
    }

    private void clearFields() {
        CmbBoxRentalId.setSelectedIndex(0);
        CustomerComboBox.setSelectedIndex(0);
        CarComboBox.setSelectedIndex(0);
        dateEndDate.setDate(null);
        dateStartDate.setDate(null);
        EmployeeComboBox.setSelectedIndex(0);
        txtAmount.setText("0.000");
        CmbBoxRentalId.setSelectedIndex(0);
    }

    private void loadCustomers() {
        try (Connection conn = DbConnection.getConnection();) {
            CustomerComboBox.removeAllItems();
            CustomerComboBox.addItem("Select Customer");

            String sql = "SELECT customer_id, CONCAT(first_name, ' ', last_name) as full_name FROM Customers ORDER BY first_name";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                CustomerComboBox.addItem(rs.getInt("customer_id") + " - " + rs.getString("full_name"));
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRentalId() {
        try (Connection conn = DbConnection.getConnection()) {
            CmbBoxRentalId.removeAllItems();
            CmbBoxRentalId.addItem("Select Rental");

            String sql = "SELECT r.rental_id, c.first_name "
                    + "FROM Rentals r "
                    + "JOIN Customers c ON r.customer_id = c.customer_id "
                    + "ORDER BY r.rental_id ASC";

            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                int rentalId = rs.getInt("rental_id");
                String customerName = rs.getString("first_name"); // you can also use last_name if needed
                CmbBoxRentalId.addItem(rentalId + " - " + customerName);
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading rental IDs: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCars() {
        try (Connection conn = DbConnection.getConnection();) {
            CarComboBox.removeAllItems();
            CarComboBox.addItem("Select Car");
            String sql = "SELECT car_id, CONCAT(make, ' ', model, ' (', license_plate, ')') as car_info, rental_rate "
                    + "FROM Cars WHERE status = 'Available' ORDER BY make, model";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                CarComboBox.addItem(rs.getInt("car_id") + " - " + rs.getString("car_info")
                        + " - Ugx" + rs.getBigDecimal("rental_rate") + "/day");
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading cars: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployees() {
        try (Connection conn = DbConnection.getConnection();) {
            EmployeeComboBox.removeAllItems();
            EmployeeComboBox.addItem("Select Employee");

            String sql = "SELECT employee_id, CONCAT(first_name, ' ', last_name) as full_name FROM employees_login ORDER BY first_name";
            PreparedStatement pst = conn.prepareStatement(sql);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                EmployeeComboBox.addItem(rs.getInt("employee_id") + " - " + rs.getString("full_name"));
            }

            rs.close();
            pst.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateAmount() {
        if (CarComboBox.getSelectedIndex() > 0 && dateStartDate.getDate() != null && dateEndDate.getDate() != null) {
            try (Connection conn = DbConnection.getConnection()) {
                // Get car ID from selection
                String carSelection = CarComboBox.getSelectedItem().toString();
                int carId = Integer.parseInt(carSelection.split(" - ")[0]);

                // Get rental rate
                String sql = "SELECT rental_rate FROM Cars WHERE car_id = ?";
                PreparedStatement pst = conn.prepareStatement(sql);
                pst.setInt(1, carId);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    double dailyRate = rs.getDouble("rental_rate");

                    // Calculate days
                    LocalDate startDate = dateStartDate.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    LocalDate endDate = dateEndDate.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                    long days = ChronoUnit.DAYS.between(startDate, endDate);
                    if (days <= 0) {
                        days = 1; // Minimum 1 day
                    }

                    double totalAmount = dailyRate * days;
                    txtAmount.setText(String.format("%.2f", totalAmount));
                }

                rs.close();
                pst.close();
            } catch (Exception e) {
                txtAmount.setText("0.00");
            }
        } else {
            txtAmount.setText("0.00");
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        LblCustomer = new javax.swing.JLabel();
        LblCar = new javax.swing.JLabel();
        LblEmployee = new javax.swing.JLabel();
        LblAmount = new javax.swing.JLabel();
        LblStartDate = new javax.swing.JLabel();
        LblEndDate = new javax.swing.JLabel();
        CustomerComboBox = new javax.swing.JComboBox<>();
        CarComboBox = new javax.swing.JComboBox<>();
        EmployeeComboBox = new javax.swing.JComboBox<>();
        txtAmount = new javax.swing.JTextField();
        btnRent = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDel = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        dateStartDate = new com.toedter.calendar.JDateChooser();
        dateEndDate = new com.toedter.calendar.JDateChooser();
        btnBack = new javax.swing.JButton();
        LblRenatl_id = new javax.swing.JLabel();
        btnFind = new javax.swing.JButton();
        CmbBoxRentalId = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        LblCustomer.setText("Customer");

        LblCar.setText("Car");

        LblEmployee.setText("Employee");

        LblAmount.setText("Amount");

        LblStartDate.setText("Start Date");

        LblEndDate.setText("End Date");

        CustomerComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        CustomerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CustomerComboBoxActionPerformed(evt);
            }
        });

        CarComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        EmployeeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        txtAmount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAmountActionPerformed(evt);
            }
        });

        btnRent.setText("Rent");
        btnRent.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRentActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDel.setText("Delete");
        btnDel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDelActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnBack.setText("Back to Dashboard");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        LblRenatl_id.setText("Rental Id*");

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        CmbBoxRentalId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(btnRent)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(btnUpdate)
                        .addGap(18, 18, 18)
                        .addComponent(btnDel)
                        .addGap(18, 18, 18)
                        .addComponent(btnClear)
                        .addGap(18, 18, 18)
                        .addComponent(btnFind)
                        .addGap(18, 18, 18)
                        .addComponent(btnBack)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(LblEmployee, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(LblCustomer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(LblCar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(LblRenatl_id, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(CmbBoxRentalId, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(LblEndDate, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(LblStartDate)
                                    .addComponent(LblAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 194, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(EmployeeComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(dateStartDate, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE)
                                    .addComponent(CarComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(CustomerComboBox, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtAmount, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(dateEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGap(51, 51, 51))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblRenatl_id, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CmbBoxRentalId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(153, 153, 153)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(EmployeeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LblCustomer, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(CustomerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(CarComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(LblCar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 33, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dateStartDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblStartDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(LblEndDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(49, 49, 49)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtAmount, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LblAmount, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear)
                    .addComponent(btnDel)
                    .addComponent(btnUpdate)
                    .addComponent(btnRent)
                    .addComponent(btnFind)
                    .addComponent(btnBack))
                .addContainerGap(68, Short.MAX_VALUE))
        );

        CarComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                calculateAmount();
            }
        });
        dateStartDate.getDateEditor().addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    calculateAmount();
                }
            }
        });
        dateEndDate.getDateEditor().addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                if ("date".equals(evt.getPropertyName())) {
                    calculateAmount();
                }
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void CustomerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CustomerComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CustomerComboBoxActionPerformed

    private void txtAmountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAmountActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAmountActionPerformed

    private void btnRentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRentActionPerformed
        // TODO add your handling code here:

        try (Connection conn = DbConnection.getConnection()) {
            // Get selected IDs
            int customerId = Integer.parseInt(CustomerComboBox.getSelectedItem().toString().split(" - ")[0]);
            int carId = Integer.parseInt(CarComboBox.getSelectedItem().toString().split(" - ")[0]);
            int employeeId = Integer.parseInt(EmployeeComboBox.getSelectedItem().toString().split(" - ")[0]);

            // Prepare dates
            Date startDate = new Date(dateStartDate.getDate().getTime());
            Date endDate = new Date(dateEndDate.getDate().getTime());
            double amount = Double.parseDouble(txtAmount.getText());

            // Insert rental
            String sql = "INSERT INTO Rentals (customer_id, car_id, employee_id, start_date, end_date, total_amount, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, 'Active')";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, customerId);
            pst.setInt(2, carId);
            pst.setInt(3, employeeId);
            pst.setDate(4, startDate);
            pst.setDate(5, endDate);
            pst.setDouble(6, amount);

            int result = pst.executeUpdate();

            if (result > 0) {
                // Update car status to 'Rented'
                String updateCarSql = "UPDATE Cars SET status = 'Rented' WHERE car_id = ?";
                PreparedStatement updatePst = conn.prepareStatement(updateCarSql);
                updatePst.setInt(1, carId);
                updatePst.executeUpdate();
                updatePst.close();

                JOptionPane.showMessageDialog(this, "Car rented successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            }

            pst.close();
            clearFields();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error renting car: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnRentActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        // TODO add your handling code here:
        clearFields();
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        dispose();
        new Dashboard().setVisible(true);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        String rentalIdStr = CmbBoxRentalId.getSelectedItem().toString();
        if (CmbBoxRentalId.getSelectedIndex() <= 0 || rentalIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select a Rental ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DbConnection.getConnection()) {
            int rentalId = Integer.parseInt(rentalIdStr.split("-")[0].trim());

            String sql = "SELECT r.customer_id, r.car_id, r.employee_id, r.start_date, r.end_date, r.total_amount, "
                    + "c.first_name, c.last_name, car.make, car.model, car.license_plate, "
                    + "e.first_name AS emp_first, e.last_name AS emp_last "
                    + "FROM Rentals r "
                    + "JOIN Customers c ON r.customer_id = c.customer_id "
                    + "JOIN Cars car ON r.car_id = car.car_id "
                    + "JOIN employees_login e ON r.employee_id = e.employee_id "
                    + "WHERE r.rental_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, rentalId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Set combo boxes properly
                CustomerComboBox.setSelectedItem(rs.getInt("customer_id") + " - "
                        + rs.getString("first_name") + " "
                        + rs.getString("last_name"));
                // Car
                for (int i = 0; i < CarComboBox.getItemCount(); i++) {
                    if (CarComboBox.getItemAt(i).startsWith(rs.getInt("car_id") + " -")) {
                        CarComboBox.setSelectedIndex(i);
                        break;
                    }
                }
                // Employee
                for (int i = 0; i < EmployeeComboBox.getItemCount(); i++) {
                    if (EmployeeComboBox.getItemAt(i).startsWith(rs.getInt("employee_id") + " -")) {
                        EmployeeComboBox.setSelectedIndex(i);
                        break;
                    }
                }

                // Dates
                dateStartDate.setDate(rs.getDate("start_date"));
                dateEndDate.setDate(rs.getDate("end_date"));

                // Amount
                txtAmount.setText(String.format("%.2f", rs.getDouble("total_amount")));
            } else {
                JOptionPane.showMessageDialog(this, "Rental ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }

            rs.close();
            pst.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error finding rental: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnFindActionPerformed

    private void CmbBoxRentalIdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CmbBoxRentalIdActionPerformed
        // When a rental ID is selected from the combo box, populate the text field
        if (CmbBoxRentalId.getSelectedIndex() > 0) {
            CmbBoxRentalId.getSelectedItem().toString();
        }
    }//GEN-LAST:event_CmbBoxRentalIdActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        if (CmbBoxRentalId.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a Rental ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DbConnection.getConnection()) {
            int rentalId = Integer.parseInt(CmbBoxRentalId.getSelectedItem().toString().split("-")[0].trim());
            int customerId = Integer.parseInt(CustomerComboBox.getSelectedItem().toString().split(" - ")[0]);
            int carId = Integer.parseInt(CarComboBox.getSelectedItem().toString().split(" - ")[0]);
            int employeeId = Integer.parseInt(EmployeeComboBox.getSelectedItem().toString().split(" - ")[0]);
            Date startDate = new Date(dateStartDate.getDate().getTime());
            Date endDate = new Date(dateEndDate.getDate().getTime());
            double amount = Double.parseDouble(txtAmount.getText());

            String sql = "UPDATE Rentals SET customer_id=?, car_id=?, employee_id=?, start_date=?, end_date=?, total_amount=? "
                    + "WHERE rental_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, customerId);
            pst.setInt(2, carId);
            pst.setInt(3, employeeId);
            pst.setDate(4, startDate);
            pst.setDate(5, endDate);
            pst.setDouble(6, amount);
            pst.setInt(7, rentalId);

            int rows = pst.executeUpdate();
            pst.close();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Rental updated successfully!");
                clearFields();
                loadCars();
                loadRentalId();
            } else {
                JOptionPane.showMessageDialog(this, "Rental ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating rental: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDelActionPerformed
        if (CmbBoxRentalId.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(this, "Please select a Rental ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DbConnection.getConnection()) {
            int rentalId = Integer.parseInt(CmbBoxRentalId.getSelectedItem().toString().split("-")[0].trim());

            // Get linked car
            String getCarSql = "SELECT car_id FROM Rentals WHERE rental_id=?";
            PreparedStatement getCarPst = conn.prepareStatement(getCarSql);
            getCarPst.setInt(1, rentalId);
            ResultSet rs = getCarPst.executeQuery();
            int carId = -1;
            if (rs.next()) {
                carId = rs.getInt("car_id");
            }
            rs.close();
            getCarPst.close();

            // Delete rental
            String sql = "DELETE FROM Rentals WHERE rental_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, rentalId);
            int rows = pst.executeUpdate();
            pst.close();

            if (rows > 0) {
                // Update car back to available
                if (carId != -1) {
                    String updateCarSql = "UPDATE Cars SET status='Available' WHERE car_id=?";
                    PreparedStatement updatePst = conn.prepareStatement(updateCarSql);
                    updatePst.setInt(1, carId);
                    updatePst.executeUpdate();
                    updatePst.close();
                }

                JOptionPane.showMessageDialog(this, "Rental deleted successfully!");
                clearFields();
                loadCars();
                loadRentalId();
            } else {
                JOptionPane.showMessageDialog(this, "Rental ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting rental: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnDelActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CarAssignment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CarAssignment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CarAssignment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CarAssignment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new CarAssignment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> CarComboBox;
    private javax.swing.JComboBox<String> CmbBoxRentalId;
    private javax.swing.JComboBox<String> CustomerComboBox;
    private javax.swing.JComboBox<String> EmployeeComboBox;
    private javax.swing.JLabel LblAmount;
    private javax.swing.JLabel LblCar;
    private javax.swing.JLabel LblCustomer;
    private javax.swing.JLabel LblEmployee;
    private javax.swing.JLabel LblEndDate;
    private javax.swing.JLabel LblRenatl_id;
    private javax.swing.JLabel LblStartDate;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDel;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnRent;
    private javax.swing.JButton btnUpdate;
    private com.toedter.calendar.JDateChooser dateEndDate;
    private com.toedter.calendar.JDateChooser dateStartDate;
    private javax.swing.JTextField txtAmount;
    // End of variables declaration//GEN-END:variables
}
