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
 * EmployeeDAO class is responsible for handling all database operations related to employees.
 * This includes searching, adding, editing, fetching, and deleting employee records. 
 * The class utilizes prepared statements to securely interact with the database.
 *
 * @author 7080
 */

public class EmployeeDAO {
	
	/**
	 * SQL query for searching employees in the database. 
	 * It searches across various fields like employee number, first name, last name, extension, email, office code, reports to, and job title.
	 * The query uses LIKE clauses for each field to support partial matching.
	 */
	
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
	
	
	
	
	
	//CRUD- + search-methods 
	
	
	
	
	
	 /**
     * Adds a new employee to the database.
     *
     * @param employee The Employee object to be added.
     * @return true if the operation was successful, false otherwise.
     */
	
	public boolean addEmployee(Employee employee) {
	    String employeesTable = "employees"; 
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
     * Edits an existing employee's details in the database.
     *
     * @param employees Table name where the employee data is stored.
     * @param employeeNumber The unique identifier of the employee to be edited.
     * @param firstName The new first name of the employee.
     * @param lastName The new last name of the employee.
     * @param extension The new extension number of the employee.
     * @param email The new email address of the employee.
     * @param officeCode The new office code associated with the employee.
     * @param reportsTo The employee number of the manager to whom this employee reports.
     * @param jobTitle The new job title of the employee.
     * @return true if the operation was successful, false otherwise.
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
	 * Deletes an employee from the database based on their employee number.
	 *
	 * @param employees      The name of the table or collection where the employee should be deleted from.
	 * @param employeeNumber The unique identifier of the employee to be deleted.
	 * @return true if the operation was successful, false otherwise.
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
     * Searches for employees in the database based on the provided search criteria.
     * The method uses a SQL query with LIKE clauses to find matches.
     *
     * @param searchCriteria The string to search for in various employee attributes.
     * @return A list of Employee objects that match the search criteria.
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
	
	 /**
     * Maps a row from a ResultSet to an Employee object.
     *
     * @param resultSet The ResultSet from a SQL query.
     * @return An Employee object populated with data from the ResultSet.
     * @throws SQLException If there is an issue accessing the ResultSet data.
     */ 
	
	public Employee mapResultSetToEmployee(ResultSet resultSet) throws SQLException {
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

	
  //Methods for fetching employees from database
	

	 /**
     * Fetches the details of a single employee from the database.
     *
     * @param employeeNumber The unique identifier of the employee.
     * @return The Employee object if found, null otherwise.
     */

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
	    return null; 
	}

	
	 /**
     * Retrieves a list of all employees from the database.
     *
     * @return A list of Employee objects representing all employees.
     */
	
	public List<Employee> displayAll() {
	    List<Employee> employees = new ArrayList<>();
	    String sql = "SELECT * FROM employees";

	    try (Connection connection = DataBaseConnection.getConnection();
	         PreparedStatement pstmt = connection.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {

	        while (rs.next()) {
	            Employee employee = new Employee(
	                rs.getInt("employeeNumber"), 
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
	        
	    }
	    return employees;
	}
	
     /**
     * Fetches the employee data from the database and returns it as a list of string arrays.
     * Each array contains the details of one employee.
     *
     * @return A list of String arrays, each representing an employee's data.
     */
	
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