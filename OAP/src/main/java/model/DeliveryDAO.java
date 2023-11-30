/**
 * File: DeliveryHandler.java
 * Description:  
 * DeliveryHandler class manages the delivery status updates and queries for orders within a CMS, interfacing with OrderHandler for database interactions.
 * It provides a method to check the order status.
 * @author Albert
 * @version 09.11.2023
 */

package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DataBaseConnection;


public class DeliveryDAO {
    
    /**
     * Checks the status of a shipment based on the order number.
     * @param orderNumber The unique order number for the shipment.
     * @return The status of the shipment (e.g., "Shipped", "In Process", "On Hold", "Cancelled", "Disputed").
     */
    public String checkShipmentStatus(int orderNumber) {
        try (Connection connection = DataBaseConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement("SELECT status FROM orders WHERE orderNumber = ?")) {
            pstmt.setInt(1, orderNumber);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("status");
                } else {
                    return "Order Not Found";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error Checking Status";
        }
    }
}