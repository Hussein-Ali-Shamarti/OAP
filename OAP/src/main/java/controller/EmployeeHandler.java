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
    
 
    public boolean addEmployee(String employee, int employeeNumber, String firstName, String lastName, String role, String jobTitle, String email, String officeCode) {
      

            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement pstm = connection.prepareStatement(
                        "INSERT INTO " + employee + " (employeeNumber, firstName, lastName, role, jobTitle, email, postalCode) VALUES (?, ?, ?, ?, ?, ?, ?)")) {

                pstm.setInt(1, employeeNumber);
                pstm.setString(2, firstName);
                pstm.setString(3, lastName);
                pstm.setString(4, role);
                pstm.setString(5, jobTitle);
                pstm.setString(6, email);
                pstm.setString(7, officeCode);
    

                int affectedRows = pstm.executeUpdate();

                return affectedRows > 0;
            }
         catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
    }

    public boolean editEmployeeInDatabase(String employee, int employeeNumber, String firstName, String lastName, String role, String jobTitle, String email, String officeCode) {
       
            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement pstm = connection.prepareStatement(
                        "UPDATE " + employee + " SET firstName = ?, lastName = ?, role = ?, jobTitle = ?, email = ?, postalCode = ?, WHERE employeeNr = ?")) {

            	 pstm.setInt(1, employeeNumber);
                 pstm.setString(2, firstName);
                 pstm.setString(3, lastName);
                 pstm.setString(4, role);
                 pstm.setString(5, jobTitle);
                 pstm.setString(6, email);
                 pstm.setString(7, officeCode);
     

                int affectedRows = pstm.executeUpdate();

                return affectedRows > 0; 
            }
         catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
    }

    public boolean removeEmployeeFromDatabase(String tableName, int employeeNumber) {
       

            try (Connection connection = DataBaseConnection.getConnection();
                 PreparedStatement pstm = connection.prepareStatement("DELETE FROM " + tableName + " WHERE employeeNr = ?")) {
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
                    rs.getString("extension"),
                    rs.getInt("officeCode"),
                    rs.getString("territory")
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
