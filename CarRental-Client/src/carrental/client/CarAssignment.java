/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carrental.client;

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
        try {
            CustomerComboBox.removeAllItems();
            CustomerComboBox.addItem("Select Customer");

            // Send LIST request to server
            String request = "LIST|Customers|";
            String response = ServerConnection.getInstance().sendRequest(request);

            // Parse response
            String[] parts = response.split("\\|", 2);
            if (parts[0].equals("SUCCESS") && parts.length > 1) {
                String[] customers = parts[1].split(";");
                for (String customer : customers) {
                    CustomerComboBox.addItem(customer);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error loading customers: "
                        + (parts.length > 1 ? parts[1] : "Server error"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading customers: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadRentalId() {
        try {
            CmbBoxRentalId.removeAllItems();
            CmbBoxRentalId.addItem("Select Rental");

            // Send LIST request to server for rentals
            String request = "LIST|Rentals|";
            String response = ServerConnection.getInstance().sendRequest(request);

            // Parse response
            String[] parts = response.split("\\|", 2);
            if (parts[0].equals("SUCCESS") && parts.length > 1) {
                String[] rentals = parts[1].split(";");
                for (String rental : rentals) {
                    // Format: "rental_id - Customer: customer_id"
                    CmbBoxRentalId.addItem(rental);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error loading rentals: "
                        + (parts.length > 1 ? parts[1] : "Server error"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading rental IDs: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadCars() {
        try {
            CarComboBox.removeAllItems();
            CarComboBox.addItem("Select Car");

            // Send LIST request to server for available cars
            String request = "LIST|Cars|";
            String response = ServerConnection.getInstance().sendRequest(request);

            // Parse response
            String[] parts = response.split("\\|", 2);
            if (parts[0].equals("SUCCESS") && parts.length > 1) {
                String[] cars = parts[1].split(";");
                for (String car : cars) {
                    // Format: "car_id - make model"
                    // We'll need additional info for rental rate, so we'll handle that separately
                    CarComboBox.addItem(car);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error loading cars: "
                        + (parts.length > 1 ? parts[1] : "Server error"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading cars: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadEmployees() {
        try {
            EmployeeComboBox.removeAllItems();
            EmployeeComboBox.addItem("Select Employee");

            // Send LIST request to server
            String request = "LIST|Employees|";
            String response = ServerConnection.getInstance().sendRequest(request);

            // Parse response
            String[] parts = response.split("\\|", 2);
            if (parts[0].equals("SUCCESS") && parts.length > 1) {
                String[] employees = parts[1].split(";");
                for (String employee : employees) {
                    EmployeeComboBox.addItem(employee);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error loading employees: "
                        + (parts.length > 1 ? parts[1] : "Server error"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading employees: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateAmount() {
        if (CarComboBox.getSelectedIndex() > 0 && dateStartDate.getDate() != null && dateEndDate.getDate() != null) {
            try {
                // Get car ID from selection
                String carSelection = CarComboBox.getSelectedItem().toString();
                int carId = Integer.parseInt(carSelection.split(" - ")[0]);

                // Get rental rate using FIND request
                String request = "FIND|Cars|" + carId;
                String response = ServerConnection.getInstance().sendRequest(request);

                String[] parts = response.split("\\|", 2);
                if (parts[0].equals("SUCCESS")) {
                    String[] fields = parts[1].split(",");
                    double dailyRate = Double.parseDouble(fields[4]); // rental_rate is at index 4

                    // Calculate days
                    LocalDate startDate = dateStartDate.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();
                    LocalDate endDate = dateEndDate.getDate().toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate();

                    long days = ChronoUnit.DAYS.between(startDate, endDate);
                    if (days <= 0) {
                        days = 1; // Minimum 1 day
                    }

                    double totalAmount = dailyRate * days;
                    txtAmount.setText(String.format("%.2f", totalAmount));
                } else {
                    txtAmount.setText("0.00");
                }
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
        try {
            // Validation
            if (CustomerComboBox.getSelectedIndex() <= 0 || CarComboBox.getSelectedIndex() <= 0
                    || EmployeeComboBox.getSelectedIndex() <= 0 || dateStartDate.getDate() == null
                    || dateEndDate.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!");
                return;
            }

            // Get selected IDs
            int customerId = Integer.parseInt(CustomerComboBox.getSelectedItem().toString().split(" - ")[0]);
            int carId = Integer.parseInt(CarComboBox.getSelectedItem().toString().split(" - ")[0]);
            int employeeId = Integer.parseInt(EmployeeComboBox.getSelectedItem().toString().split(" - ")[0]);

            // Prepare dates
            java.sql.Date startDate = new java.sql.Date(dateStartDate.getDate().getTime());
            java.sql.Date endDate = new java.sql.Date(dateEndDate.getDate().getTime());
            double amount = Double.parseDouble(txtAmount.getText());

            // Prepare data: customer_id,car_id,employee_id,start_date,end_date,total_amount,status
            String data = customerId + ","
                    + carId + ","
                    + employeeId + ","
                    + startDate.toString() + ","
                    + endDate.toString() + ","
                    + amount + ","
                    + "Active";

            // Send ADD request to server
            String request = "ADD|Rentals|" + data;
            String response = ServerConnection.getInstance().sendRequest(request);

            // Parse response
            String[] parts = response.split("\\|", 2);
            if (parts[0].equals("SUCCESS")) {
                // Update car status to 'Rented'
                String carData = carId + ",,,,Rented,,"; // Only updating status
                String updateRequest = "UPDATE|Cars|" + carData;
                ServerConnection.getInstance().sendRequest(updateRequest);

                JOptionPane.showMessageDialog(this, "Car rented successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                loadCars(); // Refresh available cars
                loadRentalId(); // Refresh rentals list
            } else {
                JOptionPane.showMessageDialog(this, "Error renting car: "
                        + (parts.length > 1 ? parts[1] : "Server error"),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
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

        try {
            int rentalId = Integer.parseInt(rentalIdStr.split(" - ")[0].trim());

            // Send FIND request to server
            String request = "FIND|Rentals|" + rentalId;
            String response = ServerConnection.getInstance().sendRequest(request);

            // Parse response
            String[] parts = response.split("\\|", 2);
            if (parts[0].equals("SUCCESS")) {
                // Response format: customer_id,car_id,employee_id,start_date,end_date,total_amount,status
                String[] fields = parts[1].split(",");

                int customerId = Integer.parseInt(fields[0]);
                int carId = Integer.parseInt(fields[1]);
                int employeeId = Integer.parseInt(fields[2]);

                // Set customer
                for (int i = 0; i < CustomerComboBox.getItemCount(); i++) {
                    if (CustomerComboBox.getItemAt(i).startsWith(customerId + " -")) {
                        CustomerComboBox.setSelectedIndex(i);
                        break;
                    }
                }

                // Set car
                for (int i = 0; i < CarComboBox.getItemCount(); i++) {
                    if (CarComboBox.getItemAt(i).startsWith(carId + " -")) {
                        CarComboBox.setSelectedIndex(i);
                        break;
                    }
                }

                // Set employee
                for (int i = 0; i < EmployeeComboBox.getItemCount(); i++) {
                    if (EmployeeComboBox.getItemAt(i).startsWith(employeeId + " -")) {
                        EmployeeComboBox.setSelectedIndex(i);
                        break;
                    }
                }

                // Dates
                dateStartDate.setDate(java.sql.Date.valueOf(fields[3]));
                dateEndDate.setDate(java.sql.Date.valueOf(fields[4]));

                // Amount
                txtAmount.setText(String.format("%.2f", Double.parseDouble(fields[5])));
            } else {
                JOptionPane.showMessageDialog(this, "Rental ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
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

        try {
            int rentalId = Integer.parseInt(CmbBoxRentalId.getSelectedItem().toString().split(" - ")[0].trim());
            int customerId = Integer.parseInt(CustomerComboBox.getSelectedItem().toString().split(" - ")[0]);
            int carId = Integer.parseInt(CarComboBox.getSelectedItem().toString().split(" - ")[0]);
            int employeeId = Integer.parseInt(EmployeeComboBox.getSelectedItem().toString().split(" - ")[0]);
            java.sql.Date startDate = new java.sql.Date(dateStartDate.getDate().getTime());
            java.sql.Date endDate = new java.sql.Date(dateEndDate.getDate().getTime());
            double amount = Double.parseDouble(txtAmount.getText());

            // Prepare data: rental_id,customer_id,car_id,employee_id,start_date,end_date,total_amount,status
            String data = rentalId + ","
                    + customerId + ","
                    + carId + ","
                    + employeeId + ","
                    + startDate.toString() + ","
                    + endDate.toString() + ","
                    + amount + ","
                    + "Active";

            // Send UPDATE request to server
            String request = "UPDATE|Rentals|" + data;
            String response = ServerConnection.getInstance().sendRequest(request);

            // Parse response
            String[] parts = response.split("\\|", 2);
            if (parts[0].equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, "Rental updated successfully!");
                clearFields();
                loadCars();
                loadRentalId();
            } else {
                JOptionPane.showMessageDialog(this, "Error: "
                        + (parts.length > 1 ? parts[1] : "Server error"));
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

        try {
            int rentalId = Integer.parseInt(CmbBoxRentalId.getSelectedItem().toString().split(" - ")[0].trim());

            // First get car_id from the rental
            String findRequest = "FIND|Rentals|" + rentalId;
            String findResponse = ServerConnection.getInstance().sendRequest(findRequest);

            int carId = -1;
            String[] findParts = findResponse.split("\\|", 2);
            if (findParts[0].equals("SUCCESS")) {
                String[] fields = findParts[1].split(",");
                carId = Integer.parseInt(fields[1]); // car_id is at index 1
            }

            // Confirm deletion
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this rental?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Send DELETE request to server
            String request = "DELETE|Rentals|" + rentalId;
            String response = ServerConnection.getInstance().sendRequest(request);

            // Parse response
            String[] parts = response.split("\\|", 2);
            if (parts[0].equals("SUCCESS")) {
                // Update car back to available
                if (carId != -1) {
                    String carData = carId + ",,,,Available,,"; // Only updating status
                    String updateRequest = "UPDATE|Cars|" + carData;
                    ServerConnection.getInstance().sendRequest(updateRequest);
                }

                JOptionPane.showMessageDialog(this, "Rental deleted successfully!");
                clearFields();
                loadCars();
                loadRentalId();
            } else {
                JOptionPane.showMessageDialog(this, "Error: "
                        + (parts.length > 1 ? parts[1] : "Server error"));
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
