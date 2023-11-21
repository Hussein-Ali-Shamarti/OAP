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
 import java.sql.ResultSet;
 import java.sql.SQLException;
 import java.util.ArrayList;
 import java.util.*;
 import database.DataBaseConnection;
 import model.Employee; 


public class EmployeeHandler {
    
 
    public boolean addEmployee(String employees, int employeeNumber, String firstName, String lastName,String extension, String email, String officeCode, int reportsTo, String jobTitle) {
      

            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement pstm = connection.prepareStatement(
                        "INSERT INTO " + employees + " (employeeNumber, firstName, lastName, extension, email, officeCode, reportsTo, jobTitle) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

                pstm.setInt(1, employeeNumber);
                pstm.setString(2, firstName);
                pstm.setString(3, lastName);
                pstm.setString(4, extension);
                pstm.setString(5, email);
                pstm.setString(6, officeCode);
                pstm.setInt(7, reportsTo);
                pstm.setString(8, jobTitle);
              
    
                
                int affectedRows = pstm.executeUpdate();

                return affectedRows > 0;
            }
         catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
    }

    public boolean editEmployeeInDatabase(String employees, int employeeNumber, String firstName, String lastName, String extension, String email, String officeCode, int reportsTo, String jobTitle) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(
                    "UPDATE " + employees + " SET firstName = ?, lastName = ?, extension = ?, email = ?, officeCode = ?, reportsTo = ?, jobTitle = ? WHERE employeeNumber = ?")) {

            pstm.setString(1, firstName);
            pstm.setString(2, lastName);
            pstm.setString(3, extension);
            pstm.setString(4, email);
            pstm.setString(5, officeCode);
            pstm.setInt(6, reportsTo);
            pstm.setString(7, jobTitle);
            pstm.setInt(8, employeeNumber);  // Assuming employeeNr is the identifier

            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0; 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public Employee fetchEmployeeData(int employeeNumber) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM employees WHERE employeeNumber = ?")) {
            
            pstmt.setInt(1, employeeNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Employee(
                    rs.getInt("employeeNumber"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("extension"),
                    rs.getString("email"),
                    rs.getString("officeCode"),
                    rs.getInt("reportsTo"),
                    rs.getString("jobTitle")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if employee is not found or an error occurs
    }

    public boolean removeEmployeeFromDatabase(String employees, int employeeNumber) {
       

            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement pstm = connection.prepareStatement("DELETE FROM " + employees + " WHERE employeeNumberr = ?")) {
                pstm.setInt(1, employeeNumber);

                int affectedRows = pstm.executeUpdate();

                return affectedRows > 0;
            }
         catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
    }
    
    public List<Employee> displayAll() {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees"; // Assuming your table is named 'employees'
        
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Employee employee = new Employee(
                    rs.getInt("employeeNumber"), // Make sure these column names match your database
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("jobTitle"),
                    rs.getString("email"),
                    rs.getString("role"),
                    rs.getInt("reportsTo"),
                    rs.getString("extension")
                );
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception or rethrow as a runtime exception
        }
        return employees;
    }

}
