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