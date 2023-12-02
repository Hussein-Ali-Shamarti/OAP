/**
 /**
 * File: EmployeeHandler.java
 * Description: This class is responsible for handling CRUD operations related to employees, including operators and administrators.
 * It provides methods for adding employee, operator, and administrator records in the database, as well as authorization of new roles.
 * @author Albert
 * @version 22.11.2023
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import database.DataBaseConnection;

/**
 * A class for handling CRUD operations related to employees, including operators and administrators.
 */
public class EmployeeDAO {
	
	private static final String SEARCH_EMPLOYEES_SQL = 
	        "SELECT * FROM employees WHERE " +
	        "CAST(employeeNumber AS CHAR) LIKE ? OR " +
	        "firstName LIKE ? OR " +
	        "lastName LIKE ? OR " +
	        "extension LIKE ? OR " +
	        "email LIKE ? OR " +
	        "officeCode LIKE ? OR " +
	        "CAST(reportsTo AS CHAR) LIKE ? OR " +
	        "jobTitle LIKE ?";

	/**
	 * Searches for employees in the database based on the provided search criteria.
	 * @param searchCriteria The criteria to search for employees.
	 * @return A list of Employee objects matching the search criteria.
	 */
	public List<Employee> searchEmployees(String searchCriteria) {
	    List<Employee> searchResults = new ArrayList<>();
	    
	    try (Connection connection = DataBaseConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_EMPLOYEES_SQL)) {

	        for (int i = 1; i <= 8; i++) {
	            preparedStatement.setString(i, "%" + searchCriteria + "%");
	        }

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            while (resultSet.next()) {
	                Employee employee = mapResultSetToEmployee(resultSet);
	                searchResults.add(employee);
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

		        return searchResults;
		    }
	 
	// Helper method to map a ResultSet to an Employee object
	private Employee mapResultSetToEmployee(ResultSet resultSet) throws SQLException {
	    return new Employee(
	        resultSet.getInt("employeeNumber"),
	        resultSet.getString("firstName"),
	        resultSet.getString("lastName"),
	        resultSet.getString("extension"),
	        resultSet.getString("email"),
	        resultSet.getString("officeCode"),
	        resultSet.getInt("reportsTo"),
	        resultSet.getString("jobTitle")
	    );
	}

	/**
	 * Adds a new employee, operator, or administrator to the database.
	 * @param employees The table name for the type of employee ("employees," "operators," or "administrators").
	 * @param employeeNumber The employee number.
	 * @param firstName The first name of the employee.
	 * @param lastName The last name of the employee.
	 * @param extension The extension of the employee.
	 * @param email The email address of the employee.
	 * @param officeCode The office code of the employee.
	 * @param reportsTo The employee number to whom this employee reports.
	 * @param jobTitle The job title of the employee.
	 * @return True if the employee is added successfully, false otherwise.
	 */
	public boolean addEmployee(Employee employee) {
	    String employeesTable = "employees"; // Assuming you have a table name for employees
	    try (Connection connection = DataBaseConnection.getConnection();
	         PreparedStatement pstm = connection.prepareStatement(
	                "INSERT INTO " + employeesTable + " (employeeNumber, firstName, lastName, extension, email, officeCode, reportsTo, jobTitle) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {

	        pstm.setInt(1, employee.getEmployeeNumber());
	        pstm.setString(2, employee.getFirstName());
	        pstm.setString(3, employee.getLastName());
	        pstm.setString(4, employee.getExtension());
	        pstm.setString(5, employee.getEmail());
	        pstm.setString(6, employee.getOfficeCode());
	        pstm.setInt(7, employee.getReportsTo());
	        pstm.setString(8, employee.getJobTitle());

	        int affectedRows = pstm.executeUpdate();

	        return affectedRows > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * Edits an existing employee's information in the database.
	 * @param employees The table name for the type of employee ("employees," "operators," or "administrators").
	 * @param employeeNumber The employee number.
	 * @param firstName The first name of the employee.
	 * @param lastName The last name of the employee.
	 * @param extension The extension of the employee.
	 * @param email The email address of the employee.
	 * @param officeCode The office code of the employee.
	 * @param reportsTo The employee number to whom this employee reports.
	 * @param jobTitle The job title of the employee.
	 * @return True if the employee is edited successfully, false otherwise.
	 */
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
	        pstm.setInt(8, employeeNumber);

	        int affectedRows = pstm.executeUpdate();
	        return affectedRows > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}

	/**
	 * Fetches employee data from the database based on the employee number.
	 * @param employeeNumber The employee number.
	 * @return An Employee object representing the employee's data, or null if not found.
	 */
	public Employee fetchEmployeeData(int employeeNumber) {
	    try (Connection connection = DataBaseConnection.getConnection();
	         PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM employees WHERE employeeNumber = ?")) {

	        pstmt.setInt(1, employeeNumber);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            // Create and return an Employee object from the ResultSet
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
	    return null; // Return null if employee not found
	}

	/**
	 * Removes an employee, operator, or administrator from the database based on the employee number.
	 * @param employees The table name for the type of employee ("employees," "operators," or "administrators").
	 * @param employeeNumber The employee number.
	 * @return True if the employee is removed successfully, false otherwise.
	 */
	public boolean removeEmployeeFromDatabase(String employees, int employeeNumber) {
	    try (Connection connection = DataBaseConnection.getConnection();
	         PreparedStatement pstm = connection.prepareStatement("DELETE FROM " + employees + " WHERE employeeNumber = ?")) {
	        pstm.setInt(1, employeeNumber);

	        int affectedRows = pstm.executeUpdate();
	        return affectedRows > 0;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;  
	    }
	}

	/**
	 * Displays all employees in the database.
	 * @return A list of all Employee objects in the database.
	 */
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
	
	 public List<String[]> fetchEmployees() {
	        List<String[]> employees = new ArrayList<>();
	        try (Connection conn = database.DataBaseConnection.getConnection();
	             Statement statement = conn.createStatement()) {
	            String sql = "SELECT employeeNumber, firstName, lastName, extension, email, officeCode, reportsTo, jobTitle FROM employees";
	            ResultSet resultSet = statement.executeQuery(sql);
	            while (resultSet.next()) {
	                String[] employee = {
	                    resultSet.getString("employeeNumber"),
	                    resultSet.getString("firstName"),
	                    resultSet.getString("lastName"),
	                    resultSet.getString("extension"),
	                    resultSet.getString("email"),
	                    resultSet.getString("officeCode"),
	                    resultSet.getString("reportsTo"),
	                    resultSet.getString("jobTitle")
	                };
	                employees.add(employee);
	            }
	        } catch (SQLException e) {
	            JOptionPane.showMessageDialog(null, "Error fetching employee data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        }
	        return employees;
	    }
	 
	 
	 
	 
}