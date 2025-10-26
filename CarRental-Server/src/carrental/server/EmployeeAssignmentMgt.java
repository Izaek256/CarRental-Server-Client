/*
 * Employee Assignment Management Form
 */
package carrental.server;

import java.sql.*;
import javax.swing.*;
import java.text.SimpleDateFormat;

/**
 *
 * @author Izaek Kisuule
 */
public class EmployeeAssignmentMgt extends javax.swing.JFrame {

    public EmployeeAssignmentMgt() {
        initComponents();
        setSize(900, 700);
        setTitle("Employee Assignment Management - Car Rental System");
        setLocationRelativeTo(null);
        loadAssignmentIds();
        loadEmployeeIds();
        loadBranchIds();
    }

    private void loadAssignmentIds() {
        cmbAssignmentId.removeAllItems();
        cmbAssignmentId.addItem("Select Assignment");

        try (Connection conn = DbConnection.getConnection()) {
            String sql = "SELECT assignment_id, assignment_type FROM employeeassignments ORDER BY assignment_id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int assignmentId = rs.getInt("assignment_id");
                String type = rs.getString("assignment_type");
                cmbAssignmentId.addItem(assignmentId + " - " + type);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading assignment IDs: " + ex.getMessage());
        }
    }

    private void loadEmployeeIds() {
    cmbEmployeeId.removeAllItems();
    cmbEmployeeId.addItem("Select Employee");

    try (Connection conn = DbConnection.getConnection()) {
        String sql = "SELECT employee_id, first_name, last_name FROM employees_login ORDER BY employee_id";
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(sql);

        while (rs.next()) {
            int empId = rs.getInt("employee_id");
            String firstname = rs.getString("first_name");
            String lastname = rs.getString("first_name");
            cmbEmployeeId.addItem(empId + " - " + firstname + " - " + lastname);
        }

    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error loading employee IDs: " + ex.getMessage());
    }
}

    private void loadBranchIds() {
        cmbBranchId.removeAllItems();
        cmbBranchId.addItem("Select Branch");

        try (Connection conn = DbConnection.getConnection()) {
            String sql = "SELECT branch_id, branch_name FROM branches ORDER BY branch_id";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                int branchId = rs.getInt("branch_id");
                String name = rs.getString("branch_name");
                cmbBranchId.addItem(branchId + " - " + name);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading branch IDs: " + ex.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        lblAssignmentId = new javax.swing.JLabel();
        lblEmployeeId = new javax.swing.JLabel();
        lblBranchId = new javax.swing.JLabel();
        lblAssignmentType = new javax.swing.JLabel();
        lblAssignmentDate = new javax.swing.JLabel();
        lblDescription = new javax.swing.JLabel();
        lblStatus = new javax.swing.JLabel();
        cmbAssignmentId = new javax.swing.JComboBox<>();
        cmbEmployeeId = new javax.swing.JComboBox<>();
        cmbBranchId = new javax.swing.JComboBox<>();
        cmbAssignmentType = new javax.swing.JComboBox<>();
        dateAssignment = new com.toedter.calendar.JDateChooser();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        cmbStatus = new javax.swing.JComboBox<>();
        btnAdd = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        btnFind = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblAssignmentId.setText("Assignment ID");
        lblEmployeeId.setText("Employee");
        lblBranchId.setText("Branch");
        lblAssignmentType.setText("Assignment Type");
        lblAssignmentDate.setText("Assignment Date");
        lblDescription.setText("Description");
        lblStatus.setText("Status");

        cmbAssignmentId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Assignment" }));

        cmbEmployeeId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Employee" }));

        cmbBranchId.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Branch" }));

        cmbAssignmentType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Rental", "Maintenance", "Customer Service", "Management", "Cleaning" }));

        txtDescription.setColumns(20);
        txtDescription.setRows(5);
        jScrollPane1.setViewportView(txtDescription);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Active", "Completed", "Cancelled" }));

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
                            .addComponent(lblAssignmentId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblEmployeeId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblBranchId, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblAssignmentType, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblAssignmentDate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDescription, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblStatus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmbAssignmentId, 0, 250, Short.MAX_VALUE)
                            .addComponent(cmbEmployeeId, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbBranchId, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmbAssignmentType, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateAssignment, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane1)
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
                    .addComponent(lblAssignmentId)
                    .addComponent(cmbAssignmentId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblEmployeeId)
                    .addComponent(cmbEmployeeId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBranchId)
                    .addComponent(cmbBranchId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblAssignmentType)
                    .addComponent(cmbAssignmentType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblAssignmentDate)
                    .addComponent(dateAssignment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDescription)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
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
        String selectedEmployee = cmbEmployeeId.getSelectedItem().toString();
        String selectedBranch = cmbBranchId.getSelectedItem().toString();

        if (selectedEmployee.equals("Select Employee") || selectedBranch.equals("Select Branch") || 
            dateAssignment.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Please fill all required fields!");
            return;
        }

        int employeeId = Integer.parseInt(selectedEmployee.split(" - ")[0]);
        int branchId = Integer.parseInt(selectedBranch.split(" - ")[0]);

        try (Connection conn = DbConnection.getConnection()) {
            String sql = "INSERT INTO employeeassignments(employee_id, branch_id, assignment_type, assignment_date, description, status) VALUES (?,?,?,?,?,?)";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, employeeId);
            pst.setInt(2, branchId);
            pst.setString(3, cmbAssignmentType.getSelectedItem().toString());
            pst.setDate(4, new java.sql.Date(dateAssignment.getDate().getTime()));
            pst.setString(5, txtDescription.getText());
            pst.setString(6, cmbStatus.getSelectedItem().toString());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Assignment Added Successfully!");
            loadAssignmentIds();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding assignment: " + ex.getMessage());
        }
    }

    private void btnFindActionPerformed(java.awt.event.ActionEvent evt) {
        String selected = cmbAssignmentId.getSelectedItem().toString();
        if (selected.equals("Select Assignment")) {
            JOptionPane.showMessageDialog(this, "Please select an assignment!");
            return;
        }

        int assignmentId = Integer.parseInt(selected.split(" - ")[0]);

        try (Connection conn = DbConnection.getConnection()) {
            String sql = "SELECT * FROM employeeassignments WHERE assignment_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, assignmentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                // Find and select employee
                int empId = rs.getInt("employee_id");
                for (int i = 0; i < cmbEmployeeId.getItemCount(); i++) {
                    if (cmbEmployeeId.getItemAt(i).startsWith(empId + " - ")) {
                        cmbEmployeeId.setSelectedIndex(i);
                        break;
                    }
                }

                // Find and select branch
                int brId = rs.getInt("branch_id");
                for (int i = 0; i < cmbBranchId.getItemCount(); i++) {
                    if (cmbBranchId.getItemAt(i).startsWith(brId + " - ")) {
                        cmbBranchId.setSelectedIndex(i);
                        break;
                    }
                }

                cmbAssignmentType.setSelectedItem(rs.getString("assignment_type"));
                dateAssignment.setDate(rs.getDate("assignment_date"));
                txtDescription.setText(rs.getString("description"));
                cmbStatus.setSelectedItem(rs.getString("status"));
            } else {
                JOptionPane.showMessageDialog(this, "Assignment not found!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error finding assignment: " + ex.getMessage());
        }
    }

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {
        String selected = cmbAssignmentId.getSelectedItem().toString();
        if (selected.equals("Select Assignment")) {
            JOptionPane.showMessageDialog(this, "Please select an assignment!");
            return;
        }

        String selectedEmployee = cmbEmployeeId.getSelectedItem().toString();
        String selectedBranch = cmbBranchId.getSelectedItem().toString();

        if (selectedEmployee.equals("Select Employee") || selectedBranch.equals("Select Branch")) {
            JOptionPane.showMessageDialog(this, "Please select employee and branch!");
            return;
        }

        int assignmentId = Integer.parseInt(selected.split(" - ")[0]);
        int employeeId = Integer.parseInt(selectedEmployee.split(" - ")[0]);
        int branchId = Integer.parseInt(selectedBranch.split(" - ")[0]);

        try (Connection conn = DbConnection.getConnection()) {
            String sql = "UPDATE employeeassignments SET employee_id=?, branch_id=?, assignment_type=?, assignment_date=?, description=?, status=? WHERE assignment_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, employeeId);
            pst.setInt(2, branchId);
            pst.setString(3, cmbAssignmentType.getSelectedItem().toString());
            pst.setDate(4, new java.sql.Date(dateAssignment.getDate().getTime()));
            pst.setString(5, txtDescription.getText());
            pst.setString(6, cmbStatus.getSelectedItem().toString());
            pst.setInt(7, assignmentId);

            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Assignment Updated Successfully!");
            loadAssignmentIds();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating assignment: " + ex.getMessage());
        }
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        String selected = cmbAssignmentId.getSelectedItem().toString();
        if (selected.equals("Select Assignment")) {
            JOptionPane.showMessageDialog(this, "Please select an assignment!");
            return;
        }

        int assignmentId = Integer.parseInt(selected.split(" - ")[0]);

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this assignment?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try (Connection conn = DbConnection.getConnection()) {
            String sql = "DELETE FROM employeeassignments WHERE assignment_id=?";
            PreparedStatement pst = conn.prepareStatement(sql);
            pst.setInt(1, assignmentId);
            pst.executeUpdate();
            JOptionPane.showMessageDialog(this, "Assignment Deleted Successfully!");
            loadAssignmentIds();
            clearFields();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting assignment: " + ex.getMessage());
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
        cmbAssignmentId.setSelectedIndex(0);
        cmbEmployeeId.setSelectedIndex(0);
        cmbBranchId.setSelectedIndex(0);
        cmbAssignmentType.setSelectedIndex(0);
        dateAssignment.setDate(null);
        txtDescription.setText("");
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
            java.util.logging.Logger.getLogger(EmployeeAssignmentMgt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(EmployeeAssignmentMgt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(EmployeeAssignmentMgt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(EmployeeAssignmentMgt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EmployeeAssignmentMgt().setVisible(true);
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
    private javax.swing.JComboBox<String> cmbAssignmentId;
    private javax.swing.JComboBox<String> cmbAssignmentType;
    private javax.swing.JComboBox<String> cmbBranchId;
    private javax.swing.JComboBox<String> cmbEmployeeId;
    private javax.swing.JComboBox<String> cmbStatus;
    private com.toedter.calendar.JDateChooser dateAssignment;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAssignmentDate;
    private javax.swing.JLabel lblAssignmentId;
    private javax.swing.JLabel lblAssignmentType;
    private javax.swing.JLabel lblBranchId;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblEmployeeId;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JTextArea txtDescription;
    // End of variables declaration
}
