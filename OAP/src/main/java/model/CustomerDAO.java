package model;

import java.math.BigDecimal;
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
 * This class represents the Data Access Object (DAO) for managing customer data interactions with the database.
 * It provides methods to perform various operations such as adding, editing, deleting, and fetching customer details.
 * 
 * @author 7080
 * @version 2.12.2023
 */

 public class CustomerDAO {
    
	 /**
	  * SQL query for searching customers in the database.
	  * It searches across multiple customer attributes including customer number, name, contact information, 
	  * phone number, address details, city, state, postal code, country, sales representative employee number, 
	  * and credit limit. This query is designed to perform a flexible search, 
	  * allowing for partial matches in any of these fields.
	  * The query uses CAST for numeric fields to enable string-based search.
	  */

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
 * Searches for customers in the database matching the given search criteria across multiple attributes.
 * The search is flexible, allowing partial matches in fields like customer name, contact info, address, etc.
 *
 * @param searchCriteria The criteria used for searching customers.
 * @return A list of customers that match the search criteria.
 * @throws SQLException If a database access error occurs or the SQL query fails to execute.
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

    /**
     * Maps a ResultSet row to a Customer object.
     *
     * @param resultSet The ResultSet containing customer data.
     * @return A Customer object populated with data from the ResultSet.
     * @throws SQLException If there is an issue accessing the ResultSet data.
     */
   
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
     * Adds a new customer to the database using provided customer details.
     *
     * @param customer The Customer object to be added.
     * @return true if the customer is successfully added, false otherwise.
     * @throws SQLException If a database access error occurs or the SQL query fails to execute.
     */
    
    public boolean addCustomer(Customer customer) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement(
                "INSERT INTO customers (customerNumber, customerName, contactLastName, contactFirstName, " + 
                "phone, addressLine1, addressLine2, city, state, postalCode, country, " +
                "salesRepEmployeeNumber, creditLimit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                
            pstm.setInt(1, customer.getCustomerNumber());
            pstm.setString(2, customer.getCustomerName());
            pstm.setString(3, customer.getContactLastName());
            pstm.setString(4, customer.getContactFirstName());
            pstm.setString(5, customer.getPhone());
            pstm.setString(6, customer.getAddressLine1());
            pstm.setString(7, customer.getAddressLine2());
            pstm.setString(8, customer.getCity());
            pstm.setString(9, customer.getState());
            pstm.setString(10, customer.getPostalCode());
            pstm.setString(11, customer.getCountry());
            pstm.setInt(12, customer.getSalesRepEmployeeNumber());
            pstm.setBigDecimal(13, customer.getCreditLimit());

            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    

    /**
     * Edits an existing customer's details in the database. Updates customer information based on 
     * the provided customer number and new details.
     *
     * @param customerNumber The unique identifier of the customer to be edited.
     * @param customerName New name of the customer.
     * @param contactLastName New last name of the customer's contact.
     * @param contactFirstName New first name of the customer's contact.
     * @param phone New phone number of the customer.
     * @param addressLine1 New primary address line of the customer.
     * @param addressLine2 New secondary address line of the customer.
     * @param city New city of the customer.
     * @param state New state of the customer.
     * @param postalCode New postal code of the customer.
     * @param country New country of the customer.
     * @param salesRepEmployeeNumber New sales rep employee number for the customer.
     * @param creditLimit New credit limit for the customer.
     * @return true if the operation was successful, false otherwise.
     * @throws SQLException If a database access error occurs or the SQL query fails to execute.
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
            pstm.setString(6, addressLine2); 
            pstm.setString(7, city);
            pstm.setString(8, state); 
            pstm.setString(9, postalCode); 
            pstm.setString(10, country);
            pstm.setInt(11, salesRepEmployeeNumber); 
            pstm.setBigDecimal(12, creditLimit); 

            pstm.setInt(13, customerNumber); 

            int affectedRows = pstm.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Fetches a single customer's data from the database based on their customer number.
     *
     * @param customerNumber The unique identifier of the customer.
     * @return The Customer object if found, null otherwise.
     * @throws SQLException If a database access error occurs or the SQL query fails to execute.
     */
    
    public Customer fetchCustomerData(int customerNumber) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT * FROM customers WHERE customerNumber = ?")) {

            pstmt.setInt(1, customerNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                
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
        return null; 
    }


    /**
     * Deletes a customer from the database based on their customer number.
     *
     * @param customerNumber The unique identifier of the customer to be deleted.
     * @return true if the customer was successfully deleted, false otherwise.
     * @throws SQLException If a database access error occurs or the SQL query fails to execute.
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
    
    /**
     * Fetches the employee numbers of all sales representatives from the database.
     *
     * @return A list of integers representing the employee numbers of sales representatives.
     * @throws SQLException If a database access error occurs or the SQL query fails to execute.
     */

    public List<Integer> fetchSalesRepEmployeeNumbers() {
        List<Integer> salesRepNumbers = new ArrayList<>();
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            
            conn = DataBaseConnection.getConnection();
            stmt = conn.createStatement();

           
            String sql = "SELECT DISTINCT salesRepEmployeeNumber FROM customers";

            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                salesRepNumbers.add(rs.getInt("salesRepEmployeeNumber"));
            }
        } catch (SQLException e) {
            e.printStackTrace(); 
        } finally {
           
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace(); 
        }
       }
        return salesRepNumbers; 
    }
    
    /**
     * Fetches all customer data from the database. Each customer's data is represented as a String array.
     *
     * @return A list of String arrays, each representing a customer's data.
     * @throws SQLException If a database access error occurs or the SQL query fails to execute.
     */
   
    
    public List<String[]> fetchCustomers() {
        List<String[]> customers = new ArrayList<>();
        try (Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String sql = "SELECT customerNumber, customerName, contactLastName, contactFirstName, " +
                         "phone, addressLine1, addressLine2, city, state, postalCode, country, " +
                         "salesRepEmployeeNumber, creditLimit FROM customers";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String[] customer = {
                    resultSet.getString("customerNumber"),
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
                    resultSet.getString("salesRepEmployeeNumber"),
                    resultSet.getString("creditLimit")
                };
                customers.add(customer);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching customer data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return customers;
    }
    
}