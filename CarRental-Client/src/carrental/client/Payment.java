/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package carrental.client;

import javax.swing.JOptionPane;
import java.text.SimpleDateFormat;

/**
 * Payment - Client Module
 * Payment Management interface.
 * Handles payment processing for car rentals including add, update, delete operations.
 * Auto-loads rental amounts and provides payment tracking functionality.
 *
 * @author Izaek Kisuule
 */
public class Payment extends javax.swing.JFrame {

    /**
     * Loads all rental IDs from the server into the rental selection combo box.
     */
    private void loadRentalIDs() {
        try {
            Rental_IDComboBox.removeAllItems();
            Rental_IDComboBox.addItem("Select Rental ID");

            String response = ServerConnection.getInstance().sendRequest("LIST|Rentals");
            if (response.startsWith("SUCCESS|")) {
                String[] rentals = response.substring(8).split(";");
                for (String rental : rentals) {
                    Rental_IDComboBox.addItem(rental);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading rental IDs: " + e.getMessage());
        }
    }

    /**
     * Loads all payment IDs from the server into the payment selection combo box.
     */
    private void loadPaymentIDs() {
        try {
            cmbPaymentID.removeAllItems();
            cmbPaymentID.addItem("Select Payment ID");

            String response = ServerConnection.getInstance().sendRequest("LIST|Payments");
            if (response.startsWith("SUCCESS|")) {
                String[] payments = response.substring(8).split(";");
                for (String payment : payments) {
                    cmbPaymentID.addItem(payment);
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading payment IDs: " + e.getMessage());
        }
    }

    /**
     * Clears all input fields and resets combo boxes.
     */
    private void clearForm() {
        // Use safe selection methods
        if (cmbPaymentID.getItemCount() > 0) {
            cmbPaymentID.setSelectedIndex(0);
        }
        if (Rental_IDComboBox.getItemCount() > 0) {
            Rental_IDComboBox.setSelectedIndex(0);
        }
        txtAmountField.setText("");
        PaymentDateChooser.setDate(null);
        if (PaymentMtdComboBox.getItemCount() > 0) {
            PaymentMtdComboBox.setSelectedIndex(0);
        }
    }

    /**
     * Creates new form Payment.
     * Initializes UI, loads data, and sets up action listeners.
     */
    public Payment() {
        initComponents();
        setTitle("Payment - Car Rental System");
        setSize(600, 600);
        setLocationRelativeTo(null);
        loadRentalIDs();
        loadPaymentIDs();
        loadRentalAmount();
        setupActionListeners(); // Add this line
        clearForm();
    }

    /**
     * Sets up action listeners for combo boxes to auto-load related data.
     */
    private void setupActionListeners() {
        // Auto-load rental amount when rental is selected - with null safety
        Rental_IDComboBox.addActionListener(evt -> {
            if (Rental_IDComboBox.getSelectedItem() != null
                    && Rental_IDComboBox.getSelectedIndex() > 0) {
                loadRentalAmount();
            }
        });

        // Auto-load payment data when payment is selected - with null safety
        cmbPaymentID.addActionListener(evt -> {
            if (cmbPaymentID.getSelectedItem() != null
                    && cmbPaymentID.getSelectedIndex() > 0) {
                loadPaymentData();
            }
        });
    }

    /**
     * Auto-loads the total rental amount when a rental is selected.
     */
    private void loadRentalAmount() {
        try {
            // Safe null check
            Object selectedItem = Rental_IDComboBox.getSelectedItem();
            if (selectedItem == null) {
                txtAmountField.setText("");
                return;
            }

            String rentalStr = selectedItem.toString();
            if (rentalStr.equals("Select Rental ID")) {
                txtAmountField.setText("");
                return;
            }

            // Extract just the rental ID number from the combo box item
            int rentalId = extractIdFromComboBox(rentalStr);

            // Find rental details to get the amount
            String response = ServerConnection.getInstance().sendRequest("FIND|Rentals|" + rentalId);

            if (response.startsWith("SUCCESS|")) {
                String[] data = response.substring(8).split(",");
                // The total_amount is at index 5 in the rental data
                if (data.length > 5) {
                    double totalAmount = Double.parseDouble(data[5]);
                    txtAmountField.setText(String.format("%.2f", totalAmount));
                } else {
                    System.out.println("Invalid rental data format: " + response);
                }
            } else {
                System.out.println("Error loading rental amount: " + response);
            }
        } catch (Exception e) {
            System.out.println("Error loading rental amount: " + e.getMessage());
            // Don't show dialog for auto-load errors, only log them
        }
    }

    private void loadPaymentData() {
        try {
            // Safe null check
            Object selectedItem = cmbPaymentID.getSelectedItem();
            if (selectedItem == null) {
                return;
            }

            String paymentStr = selectedItem.toString();
            if (paymentStr.equals("Select Payment ID")) {
                return;
            }

            // Extract just the payment ID number from the combo box item
            int paymentId = extractIdFromComboBox(paymentStr);

            // Find payment details
            String response = ServerConnection.getInstance().sendRequest("FIND|Payments|" + paymentId);

            if (response.startsWith("SUCCESS|")) {
                String[] data = response.substring(8).split(",");
                // Payment data format: rental_id,amount,payment_date,payment_method,payment_status

                if (data.length >= 5) {
                    // Set rental ID - with better error handling
                    int rentalId = Integer.parseInt(data[0]);
                    boolean rentalSelected = selectRentalInComboBox(rentalId);

                    if (!rentalSelected) {
                        System.out.println("Rental ID " + rentalId + " not found in combo box - refreshing rental list");
                        // Refresh rental list and try again
                        loadRentalIDs();
                        rentalSelected = selectRentalInComboBox(rentalId);

                        if (!rentalSelected) {
                            JOptionPane.showMessageDialog(this,
                                    "Associated rental (ID: " + rentalId + ") not found in available rentals!");
                            return;
                        }
                    }

                    // Set amount
                    txtAmountField.setText(data[1]);

                    // Set payment date
                    if (!data[2].isEmpty() && !data[2].equals("null")) {
                        try {
                            PaymentDateChooser.setDate(java.sql.Date.valueOf(data[2]));
                        } catch (Exception e) {
                            System.out.println("Invalid date format: " + data[2]);
                            PaymentDateChooser.setDate(null);
                        }
                    } else {
                        PaymentDateChooser.setDate(null);
                    }

                    // Set payment method
                    selectPaymentMethodInComboBox(data[3]);

                    JOptionPane.showMessageDialog(this, "Payment data loaded successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid payment data format received");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error loading payment data: " + response);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error loading payment data: " + e.getMessage());
        }
    }

    // Helper method to extract ID from combo box items like "1 - 2024-01-15"
    private int extractIdFromComboBox(String comboBoxItem) {
        if (comboBoxItem.contains(" - ")) {
            return Integer.parseInt(comboBoxItem.split(" - ")[0].trim());
        } else {
            return Integer.parseInt(comboBoxItem.trim());
        }
    }

    // Helper method to select rental in combo box by ID
    private boolean selectRentalInComboBox(int rentalId) {
        for (int i = 0; i < Rental_IDComboBox.getItemCount(); i++) {
            String item = Rental_IDComboBox.getItemAt(i);
            // Check both formats: "1 - details" and just "1"
            if (item.startsWith(rentalId + " - ") || item.equals(String.valueOf(rentalId))) {
                Rental_IDComboBox.setSelectedIndex(i);
                return true;
            }
        }
        System.out.println("Rental ID " + rentalId + " not found in combo box");
        return false;
    }

    // Helper method to select payment method in combo box
    private void selectPaymentMethodInComboBox(String paymentMethod) {
        boolean methodFound = false;
        for (int i = 0; i < PaymentMtdComboBox.getItemCount(); i++) {
            String item = PaymentMtdComboBox.getItemAt(i);
            if (item != null && item.equals(paymentMethod)) {
                PaymentMtdComboBox.setSelectedIndex(i);
                methodFound = true;
                break;
            }
        }
        if (!methodFound) {
            PaymentMtdComboBox.setSelectedIndex(0);
            System.out.println("Payment method '" + paymentMethod + "' not found in list");
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

        LblRentalID = new javax.swing.JLabel();
        LblAmount = new javax.swing.JLabel();
        LblPayDate = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        Rental_IDComboBox = new javax.swing.JComboBox<>();
        PaymentMtdComboBox = new javax.swing.JComboBox<>();
        txtAmountField = new javax.swing.JTextField();
        btnPay = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        LblPaymentId = new javax.swing.JLabel();
        PaymentDateChooser = new com.toedter.calendar.JDateChooser();
        cmbPaymentID = new javax.swing.JComboBox<>();
        btnFind = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        LblRentalID.setText("Rental ID");

        LblAmount.setText("Amount");

        LblPayDate.setText("Payment Date");

        jLabel4.setText("Payment Method");

        Rental_IDComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        Rental_IDComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Rental_IDComboBoxActionPerformed(evt);
            }
        });

        PaymentMtdComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Cash", "Mobile Money", "Bank Transfer", "Bank Card", "Credit Card" }));
        PaymentMtdComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PaymentMtdComboBoxActionPerformed(evt);
            }
        });

        btnPay.setText("Pay");
        btnPay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPayActionPerformed(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setText("Delete");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        btnBack.setText("Back to Dashboard");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        LblPaymentId.setText("Payment Id");

        cmbPaymentID.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbPaymentID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbPaymentIDActionPerformed(evt);
            }
        });

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(LblPaymentId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LblAmount, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LblPayDate, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LblRentalID, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(144, 144, 144)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PaymentMtdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(cmbPaymentID, javax.swing.GroupLayout.Alignment.LEADING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(PaymentDateChooser, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtAmountField, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(Rental_IDComboBox, javax.swing.GroupLayout.Alignment.LEADING, 0, 144, Short.MAX_VALUE))
                                .addGap(1, 1, 1))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(200, 200, 200)
                        .addComponent(btnBack))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(btnClear)
                        .addGap(18, 18, 18)
                        .addComponent(btnPay)
                        .addGap(31, 31, 31)
                        .addComponent(btnUpdate)
                        .addGap(30, 30, 30)
                        .addComponent(btnDelete)
                        .addGap(18, 18, 18)
                        .addComponent(btnFind)))
                .addContainerGap(82, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblPaymentId)
                    .addComponent(cmbPaymentID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LblRentalID, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Rental_IDComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtAmountField)
                    .addComponent(LblAmount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(LblPayDate, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PaymentDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(50, 50, 50)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PaymentMtdComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnPay)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(btnFind)
                    .addComponent(btnClear))
                .addGap(18, 18, 18)
                .addComponent(btnBack)
                .addContainerGap(61, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void PaymentMtdComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PaymentMtdComboBoxActionPerformed
        
    }//GEN-LAST:event_PaymentMtdComboBoxActionPerformed

    private void btnPayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPayActionPerformed
        try {
            String rentalStr = Rental_IDComboBox.getSelectedItem().toString();
            if (rentalStr.equals("Select Rental ID")) {
                JOptionPane.showMessageDialog(this, "Please select a rental!");
                return;
            }

            if (PaymentDateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select a payment date!");
                return;
            }

            int rentalId = extractIdFromComboBox(rentalStr);
            double amount = Double.parseDouble(txtAmountField.getText());
            java.sql.Date payDate = new java.sql.Date(PaymentDateChooser.getDate().getTime());
            String method = PaymentMtdComboBox.getSelectedItem().toString();

            // Create payment data
            String paymentData = rentalId + "," + amount + ","
                    + new SimpleDateFormat("yyyy-MM-dd").format(payDate) + ","
                    + method + ",Completed";

            String response = ServerConnection.getInstance().sendRequest("ADD|Payments|" + paymentData);

            if (response.startsWith("SUCCESS|")) {
                JOptionPane.showMessageDialog(this, "Payment processed successfully!");
                clearForm();
                loadPaymentIDs();
                loadRentalIDs();
            } else {
                JOptionPane.showMessageDialog(this, "Error processing payment: " + response);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error processing payment: " + e.getMessage());
        }
    }//GEN-LAST:event_btnPayActionPerformed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        try {
            String paymentStr = cmbPaymentID.getSelectedItem().toString();
            if (paymentStr.equals("Select Payment ID")) {
                JOptionPane.showMessageDialog(this, "Please select a payment to update!");
                return;
            }

            String rentalStr = Rental_IDComboBox.getSelectedItem().toString();
            if (rentalStr.equals("Select Rental ID")) {
                JOptionPane.showMessageDialog(this, "Please select a rental!");
                return;
            }

            if (PaymentDateChooser.getDate() == null) {
                JOptionPane.showMessageDialog(this, "Please select a payment date!");
                return;
            }

            int paymentId = extractIdFromComboBox(paymentStr);
            int rentalId = extractIdFromComboBox(rentalStr);
            double amount = Double.parseDouble(txtAmountField.getText());
            java.sql.Date payDate = new java.sql.Date(PaymentDateChooser.getDate().getTime());
            String method = PaymentMtdComboBox.getSelectedItem().toString();

            String paymentData = paymentId + "," + rentalId + "," + amount + ","
                    + new SimpleDateFormat("yyyy-MM-dd").format(payDate) + ","
                    + method + ",Completed";

            String response = ServerConnection.getInstance().sendRequest("UPDATE|Payments|" + paymentData);

            if (response.startsWith("SUCCESS|")) {
                JOptionPane.showMessageDialog(this, "Payment updated successfully!");
                clearForm();
                loadPaymentIDs();
                loadRentalIDs();
            } else {
                JOptionPane.showMessageDialog(this, "Error updating payment: " + response);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error updating payment: " + e.getMessage());
        }
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        try {
            String paymentStr = cmbPaymentID.getSelectedItem().toString();
            if (paymentStr.equals("Select Payment ID")) {
                JOptionPane.showMessageDialog(this, "Please select a payment to delete!");
                return;
            }
            int paymentId = extractIdFromComboBox(paymentStr);
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this payment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }
            String response = ServerConnection.getInstance().sendRequest("DELETE|Payments|" + paymentId);
            if (response.startsWith("SUCCESS|")) {
                JOptionPane.showMessageDialog(this, "Payment deleted successfully!");
                clearForm();
                loadPaymentIDs();
                loadRentalIDs();
            } else {
                JOptionPane.showMessageDialog(this, "Error deleting payment: " + response);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error deleting payment: " + e.getMessage());
        }
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
               
        new Dashboard().setVisible(true);
        dispose();
    }//GEN-LAST:event_btnBackActionPerformed

    private void Rental_IDComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Rental_IDComboBoxActionPerformed
        
        loadRentalAmount();

    }//GEN-LAST:event_Rental_IDComboBoxActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        
        clearForm();
    }//GEN-LAST:event_btnClearActionPerformed

    private void cmbPaymentIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbPaymentIDActionPerformed
        
        loadPaymentData();
    }//GEN-LAST:event_cmbPaymentIDActionPerformed

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFindActionPerformed
        try {
            String paymentStr = cmbPaymentID.getSelectedItem().toString();
            if (paymentStr.equals("Select Payment ID")) {
                JOptionPane.showMessageDialog(this, "Please select a payment ID to find!");
                return;
            }

            int paymentId = Integer.parseInt(paymentStr.split(" - ")[0]);

            // Find payment details from server
            String response = ServerConnection.getInstance().sendRequest("FIND|Payments|" + paymentId);

            if (response.startsWith("SUCCESS|")) {
                String[] data = response.substring(8).split(",");
                // Payment data format: rental_id,amount,payment_date,payment_method,payment_status

                // Set rental ID
                int rentalId = Integer.parseInt(data[0]);
                boolean rentalFound = false;
                for (int i = 0; i < Rental_IDComboBox.getItemCount(); i++) {
                    String item = Rental_IDComboBox.getItemAt(i);
                    if (item.startsWith(rentalId + " - ")) {
                        Rental_IDComboBox.setSelectedIndex(i);
                        rentalFound = true;
                        break;
                    }
                }

                if (!rentalFound) {
                    JOptionPane.showMessageDialog(this, "Associated rental not found in list!");
                }

                // Set amount
                txtAmountField.setText(data[1]);

                // Set payment date
                if (!data[2].isEmpty() && !data[2].equals("null")) {
                    try {
                        PaymentDateChooser.setDate(java.sql.Date.valueOf(data[2]));
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this, "Invalid date format: " + data[2]);
                    }
                } else {
                    PaymentDateChooser.setDate(null);
                }

                // Set payment method
                String paymentMethod = data[3];
                boolean methodFound = false;
                for (int i = 0; i < PaymentMtdComboBox.getItemCount(); i++) {
                    if (PaymentMtdComboBox.getItemAt(i).equals(paymentMethod)) {
                        PaymentMtdComboBox.setSelectedIndex(i);
                        methodFound = true;
                        break;
                    }
                }

                if (!methodFound) {
                    JOptionPane.showMessageDialog(this, "Payment method '" + paymentMethod + "' not found in list!");
                }

                JOptionPane.showMessageDialog(this, "Payment data loaded successfully!");

            } else {
                JOptionPane.showMessageDialog(this, "Payment not found: " + response);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid payment ID format!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error finding payment: " + e.getMessage());
        }
    }//GEN-LAST:event_btnFindActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Payment.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Payment().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel LblAmount;
    private javax.swing.JLabel LblPayDate;
    private javax.swing.JLabel LblPaymentId;
    private javax.swing.JLabel LblRentalID;
    private com.toedter.calendar.JDateChooser PaymentDateChooser;
    private javax.swing.JComboBox<String> PaymentMtdComboBox;
    private javax.swing.JComboBox<String> Rental_IDComboBox;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbPaymentID;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JTextField txtAmountField;
    // End of variables declaration//GEN-END:variables
}
