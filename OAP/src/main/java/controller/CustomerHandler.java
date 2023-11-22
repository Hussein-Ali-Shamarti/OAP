
/**
 * File: CustomerHandler.java
 * Description:
 * The CustomerHandler class is responsible for managing customer records in our CMS.
 * It provides methods for adding, editing, deleting, and generating customer reports, interfacing with a database.
 * @author Albert
 * @version 09.11.2023
 */

package controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import database.DataBaseConnection;
import model.Customer;



public class CustomerHandler {
	
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


    }

