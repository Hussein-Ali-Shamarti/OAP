
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
import java.sql.SQLException;
import database.DataBaseConnection;



public class CustomerHandler {
	
	 public static boolean addCustomer(int customerNumber, String companyName, String contactLastName, String contactFirstName, 
             String phone, String addressLine1, String addressLine2, String city, 
             String state, String postalCode, String country, 
             int salesRepEmployeeNr, BigDecimal creditLimit) {
		try (Connection connection = DataBaseConnection.getConnection();
			PreparedStatement pstm = connection.prepareStatement(
			"INSERT INTO customers (customerNumber, customerName, contactLastName, contactFirstName, " + 
			"phone, addressLine1, addressLine2, city, state, postalCode, country, " +
			"salesRepEmployeeNumber, creditLimit) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
			pstm.setInt(1, customerNumber);
			pstm.setString(2, companyName);
			pstm.setString(3, contactLastName);
			pstm.setString(4, contactFirstName);
			pstm.setString(5, phone);
			pstm.setString(6, addressLine1);
			pstm.setString(7, addressLine2);
			pstm.setString(8, city);
			pstm.setString(9, state);
			pstm.setString(10, postalCode);
			pstm.setString(11, country);
			pstm.setInt(12, salesRepEmployeeNr);
			pstm.setBigDecimal(13, creditLimit);
			
			int affectedRows = pstm.executeUpdate();
			return affectedRows > 0;
			} catch (SQLException e) {
			e.printStackTrace();
			return false;
}
}

    public boolean editCustomer(int customerNumber,String companyName, String contactLastName, String contactFirstName, int salesRepEmployeeNr, BigDecimal creditLimit) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("UPDATE customers SET companyName = ?, contactLastName = ?, contactFirstName = ?, salesRepEmployeeNr = ?, creditLimit = ? WHERE customerNr = ?")) {
            pstm.setString(1, companyName);
            pstm.setString(2, contactLastName);
            pstm.setString(3, contactFirstName);
            pstm.setInt(4, salesRepEmployeeNr);
            pstm.setBigDecimal(5, creditLimit);
            pstm.setInt(6, customerNumber);
            
            int affectedRows = pstm.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteCustomer(int customerNr) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("DELETE FROM customers WHERE customerNr = ?")) {
            pstm.setInt(1, customerNr);

            int affectedRows = pstm.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    }

