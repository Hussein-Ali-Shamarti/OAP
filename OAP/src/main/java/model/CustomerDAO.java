/**
 * File: CustomerHandler.java
 * Description:
 * The CustomerHandler class is responsible for managing customer records in our CMS.
 * It provides methods for adding, editing, deleting, and generating customer reports, interfacing with a database.
 * @author Albert
 * @version 09.11.2023
 */

package model;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import database.DataBaseConnection;

 public class CustomerDAO {
    
    // Define the SQL query as a constant for searching customers in the database.
    // This SQL query utilizes a SELECT statement with multiple conditions to filter results based on different customer attributes.
    // The query includes placeholders using the LIKE operator to allow partial matching in the WHERE clause.
    // Each condition corresponds to a specific customer attribute such as customer number, name, contact details, address, and sales-related information.
    // The CAST function is used to convert numeric fields (customerNumber, salesRepEmployeeNumber, creditLimit) to CHAR for pattern matching.
    // The placeholders denoted by "?" will be replaced with actual search criteria during the PreparedStatement execution.

private static final String SEARCH_CUSTOMERS_SQL = "SELECT * FROM customers WHERE " +
            "CAST(customerNumber AS CHAR) LIKE ? OR " +
            "customerName LIKE ? OR " +
            "contactLastName LIKE ? OR " +
            "contactFirstName LIKE ? OR " +
            "phone LIKE ? OR " +
            "addressLine1 LIKE ? OR " +
            "addressLine2 LIKE ? OR " +
            "city LIKE ? OR " +
            "state LIKE ? OR " +
            "postalCode LIKE ? OR " +
            "country LIKE ? OR " +
            "CAST(salesRepEmployeeNumber AS CHAR) LIKE ? OR " +
            "CAST(creditLimit AS CHAR) LIKE ?";

    /**
     * Searches for customers in the database based on the provided search criteria.
     * @param searchCriteria The criteria to search for customers.
     * @return A list of Customer objects matching the search criteria.
     */
    public List<Customer> searchCustomers(String searchCriteria) {
        List<Customer> searchResults = new ArrayList<>();

        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_CUSTOMERS_SQL)) {

            for (int i = 1; i <= 13; i++) {
                preparedStatement.setString(i, "%" + searchCriteria + "%");
            }

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customer = mapResultSetToCustomer(resultSet);
                    searchResults.add(customer);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return searchResults;
    }

    // Helper method to map a ResultSet to a Customer object
    private Customer mapResultSetToCustomer(ResultSet resultSet) throws SQLException {
        return new Customer(
            resultSet.getInt("customerNumber"),
            resultSet.getString("customerName"),
            resultSet.getString("contactLastName"),
            resultSet.getString("contactFirstName"),
            resultSet.getString("phone"),
            resultSet.getString("addressLine1"),
            resultSet.getString("addressLine2"),
            resultSet.getString("city"),
            resultSet.getString("state"),
            resultSet.getString("postalCode"),
            resultSet.getString("country"),
            resultSet.getInt("salesRepEmployeeNumber"),
            resultSet.getBigDecimal("creditLimit")
        );
    }

    /**
     * Adds a new customer to the database.
     * @param customerNumber The customer number.
     * @param customerName The customer name.
     * @param contactLastName The last name of the contact person.
     * @param contactFirstName The first name of the contact person.
     * @param phone The phone number.
     * @param addressLine1 The first line of the address.
     * @param addressLine2 The second line of the address.
     * @param city The city.
     * @param state The state.
     * @param postalCode The postal code.
     * @param country The country.
     * @param salesRepEmployeeNumber The sales representative's employee number.
     * @param creditLimit The credit limit.
     * @return True if the customer is added successfully, false otherwise.
     */
    public static boolean addCustomer(int customerNumber, String customerName, String contactLastName, String contactFirstName, 
             String phone, String addressLine1, String addressLine2, String city, 
             String state, String postalCode, String country, 
             int salesRepEmployeeNumber, BigDecimal creditLimit) {
        try (Connection connection = DataBaseConnection.getConnection();
            PreparedStatement pstm = connection.prepareStatement(
            "INSERT INTO customers (customerNumber, customerName, contactLastName, contactFirstName, " + 
            "phone, addressLine1, addressLine2, city, state, postalCode, country, " +
            "salesRepEmployeeNumber, creditLimit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstm.setInt(1, customerNumber);
            pstm.setString(2, customerName);
            pstm.setString(3, contactLastName);
            pstm.setString(4, contactFirstName);
            pstm.setString(5, phone);
            pstm.setString(6, addressLine1);
            pstm.setString(7, addressLine2);
            pstm.setString(8, city);
            pstm.setString(9, state);
            pstm.setString(10, postalCode);
            pstm.setString(11, country);
            pstm.setInt(12, salesRepEmployeeNumber);
            pstm.setBigDecimal(13, creditLimit);

            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
            } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Edits an existing customer's information in the database.
     * @param customerNumber The customer number.
     * @param customerName The customer name.
     * @param contactLastName The last name of the contact person.
     * @param contactFirstName The first name of the contact person.
     * @param phone The phone number.
     * @param addressLine1 The first line of the address.
     * @param addressLine2 The second line of the address.
     * @param city The city.
     * @param state The state.
     * @param postalCode The postal code.
     * @param country The country.
     * @param salesRepEmployeeNumber The sales representative's employee number.
     * @param creditLimit The credit limit.
     * @return True if the customer is edited successfully, false otherwise.
     */
    public boolean editCustomer(int customerNumber, String customerName, String contactLastName, String contactFirstName, String phone, String addressLine1, String addressLine2, String city, String state, String postalCode, String country, int salesRepEmployeeNumber, BigDecimal creditLimit) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(
                 "UPDATE customers SET customerName = ?, contactLastName = ?, contactFirstName = ?, phone = ?, addressLine1 = ?, addressLine2 = ?, city = ?, state = ?, postalCode = ?, country = ?, salesRepEmployeeNumber = ?, creditLimit = ? WHERE customerNumber = ?")) {

            pstm.setString(1, customerName);
            pstm.setString(2, contactLastName);
            pstm.setString(3, contactFirstName);
            pstm.setString(4, phone);
            pstm.setString(5, addressLine1);
            pstm.setString(6, addressLine2); // Can be null
            pstm.setString(7, city);
            pstm.setString(8, state); // Can be null
            pstm.setString(9, postalCode); // Can be null
            pstm.setString(10, country);
            pstm.setInt(11, salesRepEmployeeNumber); // Can be null, 0, or negative to indicate no sales rep
            pstm.setBigDecimal(12, creditLimit); // Can be null

            pstm.setInt(13, customerNumber); // The identifier for the customer to be updated

            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches customer data from the database based on the customer number.
     * @param customerNumber The customer number.
     * @return A Customer object representing the customer's data, or null if not found.
     */
    public Customer fetchCustomerData(int customerNumber) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM customers WHERE customerNumber = ?")) {

            pstmt.setInt(1, customerNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Create and return a Customer object from the ResultSet
                return new Customer(
                    rs.getInt("customerNumber"),
                    rs.getString("customerName"),
                    rs.getString("contactLastName"),
                    rs.getString("contactFirstName"),
                    rs.getString("phone"),
                    rs.getString("addressLine1"),
                    rs.getString("addressLine2"),
                    rs.getString("city"),
                    rs.getString("state"),
                    rs.getString("postalCode"),
                    rs.getString("country"),
                    rs.getInt("salesRepEmployeeNumber"),
                    rs.getBigDecimal("creditLimit")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Return null if customer not found
    }

    /**
     * Deletes a customer from the database based on the customer number.
     * @param customerNumber The customer number.
     * @return True if the customer is deleted successfully, false otherwise.
     */
    public boolean deleteCustomer(int customerNumber) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("DELETE FROM customers WHERE customerNumber = ?")) {
            pstm.setInt(1, customerNumber);

            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    public List<Integer> fetchSalesRepEmployeeNumbers() {
        List<Integer> salesRepNumbers = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Use DataBaseConnection class to get the connection
            conn = DataBaseConnection.getConnection();
            stmt = conn.createStatement();

            // SQL query to fetch distinct sales rep employee numbers
            String sql = "SELECT DISTINCT salesRepEmployeeNumber FROM customers";

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                salesRepNumbers.add(rs.getInt("salesRepEmployeeNumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Or use your preferred error handling
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace(); // Or use your preferred error handling
            }
        }
        return salesRepNumbers;
    }


    
    
}