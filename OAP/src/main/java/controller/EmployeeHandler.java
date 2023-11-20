/**
 * File: EmployeeHandler.java
 * Description: This is a class responsible for handling CRUD operations related to employees, including operators and administrators.
 * It provides methods for adding employee, operator, and administrator records in the database, as well as authorization of new roles.
 * @author Albert
 * @version 09.11.2023
 */
 package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import database.DataBaseConnection;

public class EmployeeHandler {
    private DataBaseConnection dbConnection;

    public EmployeeHandler() {
        dbConnection = new DataBaseConnection();
    }

    public boolean addEmployee(String tableName, int employeeNr, String firstName, String lastName, String role, String jobTitle, String password, String email, boolean canCheckDeliveryStatus, String postalCode, String roles) {
        try {
            dbConnection.open(); // Open the database connection

            try (Connection connection = dbConnection.getConnection();
                 PreparedStatement pstm = connection.prepareStatement(
                        "INSERT INTO " + tableName + " (employeeNr, firstName, lastName, role, jobTitle, password, email, canCheckDeliveryStatus, postalCode, roles) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

                pstm.setInt(1, employeeNr);
                pstm.setString(2, firstName);
                pstm.setString(3, lastName);
                pstm.setString(4, role);
                pstm.setString(5, jobTitle);
                pstm.setString(6, password);
                pstm.setString(7, email);
                pstm.setBoolean(8, canCheckDeliveryStatus);
                pstm.setString(9, postalCode);
                pstm.setString(10, roles);

                int affectedRows = pstm.executeUpdate();

                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbConnection.close(); // Close the database connection in a finally block
        }
    }

    public boolean editEmployeeInDatabase(String tableName, int employeeNr, String firstName, String lastName, String role, String jobTitle, String password, String email, boolean canCheckDeliveryStatus, String postalCode, String roles) {
        try {
            dbConnection.open(); // Open the database connection

            try (Connection connection = dbConnection.getConnection();
                 PreparedStatement pstm = connection.prepareStatement(
                        "UPDATE " + tableName + " SET firstName = ?, lastName = ?, role = ?, jobTitle = ?, password = ?, email = ?, canCheckDeliveryStatus = ?, postalCode = ?, roles = ? WHERE employeeNr = ?")) {

                pstm.setString(1, firstName);
                pstm.setString(2, lastName);
                pstm.setString(3, role);
                pstm.setString(4, jobTitle);
                pstm.setString(5, password);
                pstm.setString(6, email);
                pstm.setBoolean(7, canCheckDeliveryStatus);
                pstm.setString(8, postalCode);
                pstm.setString(9, roles);
                pstm.setInt(10, employeeNr);

                int affectedRows = pstm.executeUpdate();

                return affectedRows > 0; 
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbConnection.close(); // Close the database connection in a finally block
        }
    }

    public boolean removeEmployeeFromDatabase(String tableName, int employeeNr) {
        try {
            dbConnection.open(); // Open the database connection

            try (Connection connection = dbConnection.getConnection();
                 PreparedStatement pstm = connection.prepareStatement("DELETE FROM " + tableName + " WHERE employeeNr = ?")) {
                pstm.setInt(1, employeeNr);

                int affectedRows = pstm.executeUpdate();

                return affectedRows > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            dbConnection.close(); // Close the database connection in a finally block
        }
    }
}
