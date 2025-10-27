# Car Rental System - Client Application

This is a Java Swing desktop application for managing a car rental business. The system provides a comprehensive interface for handling vehicles, customers, rentals, payments, and maintenance.

## System Overview

The application consists of multiple forms that work together to provide a complete car rental management solution. Each form serves a specific purpose in the business workflow.

## Forms and Their Components

### 1. Login Form (`Login.java`)
The entry point of the application that authenticates users.

**Fields:**
- Email (JTextField)
- Password (JPasswordField)

**Buttons:**
- Login (JButton) - Authenticates user credentials
- Sign up (JButton) - Navigates to the signup form

---

### 2. Signup Form (`Signup.java`)
Allows new employees to register in the system.

**Fields:**
- First name (JTextField)
- Last name (JTextField)
- Email (JTextField)
- Phone Number (JTextField)
- Address (JTextField)
- Password (JPasswordField)
- Confirm Password (JPasswordField)

**Buttons:**
- Sign Up (JButton) - Registers new employee
- Exit (JButton) - Closes the application
- I have An Account (JButton) - Navigates back to login form

---

### 3. Dashboard (`Dashboard.java`)
The main interface that provides navigation to all system functions.

**Menu Items:**
- System Menu:
  - ManageCars (JMenuItem) - Opens Vehicle Management
  - Manage Customers (JMenuItem) - Opens Customer Management
  - Manage Rentals (JMenuItem) - Opens Car Assignment/Rental Management
  - Manage Payments (JMenuItem) - Opens Payment Management
  - Vehicle Maintenance (JMenuItem) - Opens Maintenance Management

- Reports Menu:
  - Customer Report (JMenuItem) - Generates PDF customer report
  - Car Inventory Report (JMenuItem) - Generates PDF car inventory report
  - Rental Report (JMenuItem) - Generates PDF rental report (requires date range)
  - Payment Report (JMenuItem) - Generates PDF payment report
  - Maintenance Report (JMenuItem) - Generates PDF maintenance report

- File Menu:
  - Exit (JMenuItem) - Closes the application

---

### 4. Vehicle Management (`VehicleMgt.java`)
Manages the car inventory including adding, updating, and removing vehicles.

**Fields:**
- Car Id (JComboBox) - Dropdown for selecting existing cars
- Make (JTextField) - Car manufacturer
- Model (JTextField) - Car model
- Plate Number (JTextField) - License plate number
- Year (JTextField) - Manufacturing year
- Rental Rate (JTextField) - Daily rental rate
- Status (JComboBox) - Current status (Available, Rented, Maintenance)
- Color (JTextField) - Car color
- Mileage (JTextField) - Current mileage

**Buttons:**
- Add (JButton) - Adds a new car to the inventory
- Update (JButton) - Updates selected car information
- Delete (JButton) - Removes selected car from inventory
- Clear (JButton) - Clears all form fields
- Find (JButton) - Retrieves information for selected car
- Back To Dashboard (JButton) - Returns to main dashboard

---

### 5. Customer Management (`CustomerManagement.java`)
Manages customer information and records.

**Fields:**
- Customer ID (JComboBox) - Dropdown for selecting existing customers
- First name (JTextField)
- Last name (JTextField)
- Email (JTextField)
- Phone number (JTextField)
- Address (JTextField)
- License Number (JTextField) - Driver's license number

**Buttons:**
- Add (JButton) - Registers a new customer
- Delete (JButton) - Removes selected customer
- Update (JButton) - Updates selected customer information
- Clear (JButton) - Clears all form fields
- Find (JButton) - Retrieves information for selected customer
- Back to Dashboard (JButton) - Returns to main dashboard

---

### 6. Car Assignment/Rental Management (`CarAssignment.java`)
Handles the rental process including assigning cars to customers.

**Fields:**
- Rental Id (JComboBox) - Dropdown for selecting existing rentals
- Customer (JComboBox) - Dropdown for selecting customer
- Car (JComboBox) - Dropdown for selecting available car
- Employee (JComboBox) - Dropdown for selecting employee
- Start Date (JDateChooser) - Rental start date
- End Date (JDateChooser) - Rental end date
- Amount (JTextField) - Total rental amount (automatically calculated)

**Buttons:**
- Rent (JButton) - Creates a new rental agreement
- Update (JButton) - Updates selected rental information
- Delete (JButton) - Cancels selected rental
- Clear (JButton) - Clears all form fields
- Find (JButton) - Retrieves information for selected rental
- Back to Dashboard (JButton) - Returns to main dashboard

---

### 7. Payment Management (`Payment.java`)
Processes payments for completed rentals.

**Fields:**
- Payment Id (JTextField) - Payment identifier
- Rental ID (JComboBox) - Dropdown for selecting rental
- Amount (JTextField) - Payment amount (auto-filled from rental)
- Payment Date (JDateChooser) - Date of payment
- Payment Method (JComboBox) - Payment type (Cash, Mobile Money, Bank Transfer, etc.)

**Buttons:**
- Pay (JButton) - Processes payment for selected rental
- Update (JButton) - Updates payment information
- Delete (JButton) - Removes payment record
- Back to Dashboard (JButton) - Returns to main dashboard

---

### 8. Vehicle Maintenance (`VehicleMantenance.java`)
Tracks maintenance activities for vehicles.

**Fields:**
- ID (JTextField) - Maintenance record identifier
- Car (JComboBox) - Dropdown for selecting car
- Service Date (JDateChooser) - Date of service
- Description (JTextArea) - Description of maintenance performed
- Cost (JTextField) - Cost of maintenance

**Buttons:**
- Add (JButton) - Creates new maintenance record
- Update (JButton) - Updates existing maintenance record
- Delete (JButton) - Removes maintenance record
- Back to Dashboard (JButton) - Returns to main dashboard

---

### 9. Branch Management (`BranchMgt.java`)
Manages branch locations and their information.

**Fields:**
- Branch ID (JComboBox) - Dropdown for selecting existing branches
- Branch Name (JTextField) - Name of the branch
- Address (JTextField) - Street address of the branch
- City (JTextField) - City where the branch is located
- Phone Number (JTextField) - Contact phone number
- Email (JTextField) - Contact email address
- Manager (JComboBox) - Dropdown for selecting branch manager
- Status (JComboBox) - Current status (Active, Inactive)

**Buttons:**
- Add (JButton) - Creates a new branch
- Update (JButton) - Updates selected branch information
- Delete (JButton) - Removes selected branch
- Clear (JButton) - Clears all form fields
- Find (JButton) - Retrieves information for selected branch
- Back To Dashboard (JButton) - Returns to main dashboard

---

### 10. Damages Management (`DamagesMgt.java`)
Tracks vehicle damages reported during rentals.

**Fields:**
- Damage ID (JComboBox) - Dropdown for selecting existing damage records
- Rental ID (JComboBox) - Dropdown for selecting associated rental
- Car (JComboBox) - Dropdown for selecting damaged car
- Description (JTextArea) - Description of the damage
- Repair Cost (JTextField) - Cost to repair the damage
- Reported Date (JDateChooser) - Date when damage was reported
- Status (JComboBox) - Current status (Reported, Assessed, Repaired)

**Buttons:**
- Add (JButton) - Creates a new damage record
- Update (JButton) - Updates selected damage information
- Delete (JButton) - Removes selected damage record
- Clear (JButton) - Clears all form fields
- Find (JButton) - Retrieves information for selected damage record
- Back To Dashboard (JButton) - Returns to main dashboard

---

### 11. Employee Assignment Management (`EmployeeAssignmentMgt.java`)
Manages employee assignments to branches and roles.

**Fields:**
- Assignment ID (JComboBox) - Dropdown for selecting existing assignments
- Employee (JComboBox) - Dropdown for selecting employee
- Branch (JComboBox) - Dropdown for selecting branch
- Assignment Type (JComboBox) - Type of assignment (Rental, Maintenance, Customer Service, Management, Cleaning)
- Assignment Date (JDateChooser) - Date of assignment
- Description (JTextArea) - Description of the assignment
- Status (JComboBox) - Current status (Active, Completed, Cancelled)

**Buttons:**
- Add (JButton) - Creates a new employee assignment
- Update (JButton) - Updates selected assignment information
- Delete (JButton) - Removes selected assignment
- Clear (JButton) - Clears all form fields
- Find (JButton) - Retrieves information for selected assignment
- Back To Dashboard (JButton) - Returns to main dashboard

---

### 12. Insurance Management (`InsuranceMgt.java`)
Manages insurance policies for vehicles.

**Fields:**
- Insurance ID (JComboBox) - Dropdown for selecting existing insurance policies
- Car (JComboBox) - Dropdown for selecting insured car
- Policy Number (JTextField) - Insurance policy number
- Insurance Company (JTextField) - Name of the insurance company
- Coverage Amount (JTextField) - Amount of coverage
- Premium Amount (JTextField) - Insurance premium cost
- Start Date (JDateChooser) - Policy start date
- End Date (JDateChooser) - Policy end date
- Status (JComboBox) - Current status (Active, Expired)

**Buttons:**
- Add (JButton) - Creates a new insurance policy
- Update (JButton) - Updates selected policy information
- Delete (JButton) - Removes selected policy
- Clear (JButton) - Clears all form fields
- Find (JButton) - Retrieves information for selected policy
- Back To Dashboard (JButton) - Returns to main dashboard

---

## Database Connection

The application connects to a MySQL database using the following credentials:
- URL: jdbc:mysql://localhost:3306/car_rental_sys
- Username: root
- Password: isaacK@12345

## Report Generation

The system can generate PDF reports for:
- Customer information
- Car inventory
- Rental history (with date range)
- Payment records
- Maintenance activities

Reports are generated using the iText PDF library and saved in the application's root directory.