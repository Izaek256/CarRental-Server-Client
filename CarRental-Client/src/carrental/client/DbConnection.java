/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package carrental.client;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Izaek Kisuule
 */
public class DbConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/car_rental_sys"; 
    private static final String USER = "root";
    private static final String PASSWORD = "isaacK@12345";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
