/*
 * Branch Management Form
 */
package carrental.client;

import javax.swing.*;

/**
 *
 * @author Izaek Kisuule
 */
public class BranchMgt extends javax.swing.JFrame {

    public BranchMgt() {
        initComponents();
        setSize(900, 700);
        setTitle("Branch Management - Car Rental System");
        setLocationRelativeTo(null);
        loadBranchIds();
        loadManagerIds();
    }

    private void loadBranchIds() {
        try {
            cmbBranchId.removeAllItems();
            cmbBranchId.addItem("Select Branch");

            String response = ServerConnection.getInstance().sendRequest("LIST|Branches|");
            String[] parts = response.split("\\|", 2);

            if (parts[0].equals("SUCCESS") && parts.length > 1) {
                String[] branches = parts[1].split(";");
                for (String branch : branches) {
                    cmbBranchId.addItem(branch);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading branch IDs: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void loadManagerIds() {
        try {
            cmbManagerId.removeAllItems();
            cmbManagerId.addItem("Select Manager");

            String response = ServerConnection.getInstance().sendRequest("LIST|Employees|");
            String[] parts = response.split("\\|", 2);

            if (parts[0].equals("SUCCESS") && parts.length > 1) {
                String[] employees = parts[1].split(";");
                for (String employee : employees) {
                    cmbManagerId.addItem(employee);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading manager IDs: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        lblBranchId = new javax.swing.JLabel();
        lblBranchName = new javax.swing.JLabel();
        lblAddress = new javax.swing.JLabel();
        lblCity = new javax.swing.JLabel();
        lblPhone = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblManagerId = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        cmbBranchId = new javax.swing.JComboBox<>();
        txtBranchName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        txtCity = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        cmbManagerId = new javax.swing.JComboBox<>();
        cmbStatus = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblBranchId.setText("Branch ID");
        lblBranchName.setText("Branch Name");
        lblAddress.setText("Address");
        lblCity.setText("City");
        lblPhone.setText("Phone Number");
        lblEmail.setText("Email");
        lblManagerId.setText("Manager");
        lblStatus.setText("Status");

        cmbBranchId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Select Branch"}));

        cmbManagerId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Select Manager"}));

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[]{"Active", "Inactive"}));

        btnAdd.setText("Add");
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
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

        btnClear.setText("Clear");
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnFind.setText("Find");
        btnFind.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFindActionPerformed(evt);
            }
        });

        btnBack.setText("Back To Dashboard");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(110, 110, 110)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lblBranchId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblBranchName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblAddress, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblCity, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblPhone, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblEmail, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblManagerId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(40, 40, 40)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(cmbBranchId, 0, 250, Short.MAX_VALUE)
                                                        .addComponent(txtBranchName)
                                                        .addComponent(txtAddress)
                                                        .addComponent(txtCity)
                                                        .addComponent(txtPhone)
                                                        .addComponent(txtEmail)
                                                        .addComponent(cmbManagerId, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cmbStatus, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(11, 11, 11)
                                                .addComponent(btnAdd)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnUpdate)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnDelete)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnClear)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnFind)))
                                .addContainerGap(130, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBack)
                                .addGap(320, 320, 320))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40, 40, 40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblBranchId)
                                        .addComponent(cmbBranchId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblBranchName)
                                        .addComponent(txtBranchName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblAddress)
                                        .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblCity)
                                        .addComponent(txtCity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblPhone)
                                        .addComponent(txtPhone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblEmail)
                                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblManagerId)
                                        .addComponent(cmbManagerId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25, 25, 25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lblStatus)
                                        .addComponent(cmbStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(35, 35, 35)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAdd)
                                        .addComponent(btnUpdate)
                                        .addComponent(btnDelete)
                                        .addComponent(btnClear)
                                        .addComponent(btnFind))
                                .addGap(18, 18, 18)
                                .addComponent(btnBack)
                                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            if (txtBranchName.getText().isEmpty() || txtAddress.getText().isEmpty()
                    || txtCity.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!");
                return;
            }

            String selectedManager = cmbManagerId.getSelectedItem().toString();
            String managerId = "";

            if (!selectedManager.equals("Select Manager")) {
                managerId = selectedManager.split(" - ")[0];
            }

            // Data format: branch_name,address,city,phone_number,email,manager_id,status
            String branchData = txtBranchName.getText() + ","
                    + txtAddress.getText() + ","
                    + txtCity.getText() + ","
                    + txtPhone.getText().trim() + ","
                    + txtEmail.getText().trim() + ","
                    + managerId + ","
                    + cmbStatus.getSelectedItem().toString();

            String response = ServerConnection.getInstance().sendRequest("ADD|Branches|" + branchData);
            String[] parts = response.split("\\|", 2);

            if (parts[0].equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, parts[1]);
                loadBranchIds();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + parts[1]);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding branch: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String selected = cmbBranchId.getSelectedItem().toString();
            if (selected.equals("Select Branch")) {
                JOptionPane.showMessageDialog(this, "Please select a branch!");
                return;
            }

            // Extract branch_id from "5 - Branch Name"
            int branchId = Integer.parseInt(selected.split(" - ")[0]);

            // Send FIND request
            String response = ServerConnection.getInstance().sendRequest("FIND|Branches|" + branchId);
            System.out.println("Server response: " + response); // Debug

            String[] parts = response.split("\\|", 2);

            if (parts[0].equals("SUCCESS")) {
                /*
                Server returns: "SUCCESS|branch_name,address,city,phone_number,email,manager_id,status"
                Example: "SUCCESS|Downtown,123 Main St,New York,555-1234,info@branch.com,5,Active"
                 */
                String[] fields = parts[1].split(",");

                System.out.println("Number of fields: " + fields.length); // Debug
                for (int i = 0; i < fields.length; i++) {
                    System.out.println("Field " + i + ": " + fields[i]); // Debug
                }

                if (fields.length >= 7) {
                    // Set text fields
                    txtBranchName.setText(fields[0]);
                    txtAddress.setText(fields[1]);
                    txtCity.setText(fields[2]);
                    txtPhone.setText(fields[3]);
                    txtEmail.setText(fields[4]);

                    // Find and select manager ID (may be empty for NULL)
                    if (!fields[5].isEmpty()) {
                        int managerId = Integer.parseInt(fields[5]);
                        for (int i = 0; i < cmbManagerId.getItemCount(); i++) {
                            String item = cmbManagerId.getItemAt(i);
                            if (item.startsWith(managerId + " - ")) {
                                cmbManagerId.setSelectedIndex(i);
                                break;
                            }
                        }
                    } else {
                        cmbManagerId.setSelectedIndex(0); // Select "Select Manager"
                    }

                    // Set status
                    cmbStatus.setSelectedItem(fields[6]);

                    JOptionPane.showMessageDialog(this, "Record found!");
                } else {
                    JOptionPane.showMessageDialog(this, "Incomplete data received! Expected 7 fields, got " + fields.length);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + parts[1]);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error finding branch: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String selected = cmbBranchId.getSelectedItem().toString();
            if (selected.equals("Select Branch")) {
                JOptionPane.showMessageDialog(this, "Please select a branch!");
                return;
            }

            if (txtBranchName.getText().isEmpty() || txtAddress.getText().isEmpty()
                    || txtCity.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!");
                return;
            }

            int branchId = Integer.parseInt(selected.split(" - ")[0]);

            String selectedManager = cmbManagerId.getSelectedItem().toString();
            String managerId = "";

            if (!selectedManager.equals("Select Manager")) {
                managerId = selectedManager.split(" - ")[0];
            }

            // Data format: branch_id,branch_name,address,city,phone_number,email,manager_id,status
            String branchData = branchId + ","
                    + txtBranchName.getText() + ","
                    + txtAddress.getText() + ","
                    + txtCity.getText() + ","
                    + txtPhone.getText().trim() + ","
                    + txtEmail.getText().trim() + ","
                    + managerId + ","
                    + cmbStatus.getSelectedItem().toString();

            String response = ServerConnection.getInstance().sendRequest("UPDATE|Branches|" + branchData);
            String[] parts = response.split("\\|", 2);

            if (parts[0].equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, parts[1]);
                loadBranchIds();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + parts[1]);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating branch: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            String selected = cmbBranchId.getSelectedItem().toString();
            if (selected.equals("Select Branch")) {
                JOptionPane.showMessageDialog(this, "Please select a branch!");
                return;
            }

            int branchId = Integer.parseInt(selected.split(" - ")[0]);

            int confirm = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete this branch?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            String response = ServerConnection.getInstance().sendRequest("DELETE|Branches|" + branchId);
            String[] parts = response.split("\\|", 2);

            if (parts[0].equals("SUCCESS")) {
                JOptionPane.showMessageDialog(this, parts[1]);
                loadBranchIds();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Error: " + parts[1]);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting branch: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {
        clearFields();
    }

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
        new Dashboard().setVisible(true);
    }

    private void clearFields() {
        cmbBranchId.setSelectedIndex(0);
        txtBranchName.setText("");
        txtAddress.setText("");
        txtCity.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        cmbManagerId.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BranchMgt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BranchMgt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BranchMgt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BranchMgt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BranchMgt().setVisible(true);
            }
        });
    }

    // Variables declaration
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JComboBox<String> cmbBranchId;
    private javax.swing.JComboBox<String> cmbManagerId;
    private javax.swing.JComboBox<String> cmbStatus;
    private javax.swing.JLabel lblAddress;
    private javax.swing.JLabel lblBranchId;
    private javax.swing.JLabel lblBranchName;
    private javax.swing.JLabel lblCity;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblManagerId;
    private javax.swing.JLabel lblPhone;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtBranchName;
    private javax.swing.JTextField txtCity;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration
}
