
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
	
    public static boolean addCustomer(int customerNr, String companyName, String contactLastName, String contactFirstName, int salesRepEmployeeNr, BigDecimal creditLimit) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("INSERT INTO customers (customerNumber, customerName, contactLastName, contactFirstName, salesRepEmployeeNumber, creditLimit) VALUES (?, ?, ?, ?, ?, ?, )")) {
            pstm.setInt(1, customerNr);
            pstm.setString(2, companyName);
            pstm.setString(3, contactLastName);
            pstm.setString(4, contactFirstName);
            pstm.setInt(5, salesRepEmployeeNr);
            pstm.setBigDecimal(6, creditLimit);

            int affectedRows = pstm.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean editCustomer(int customerNr, String companyName, String contactLastName, String contactFirstName, int salesRepEmployeeNr, BigDecimal creditLimit) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstm = connection.prepareStatement("UPDATE customers SET companyName = ?, contactLastName = ?, contactFirstName = ?, salesRepEmployeeNr = ?, creditLimit = ? WHERE customerNr = ?")) {
            pstm.setString(1, companyName);
            pstm.setString(2, contactLastName);
            pstm.setString(3, contactFirstName);
            pstm.setInt(4, salesRepEmployeeNr);
            pstm.setBigDecimal(5, creditLimit);
            pstm.setInt(6, customerNr);
            
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

