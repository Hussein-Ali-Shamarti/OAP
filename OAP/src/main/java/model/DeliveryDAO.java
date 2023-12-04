package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DataBaseConnection;

/**
 * Manages delivery status updates and queries for orders within a CMS.
 * Interacts with the database to check and update the status of shipments based on order numbers.
 * This class is part of the model layer and interfaces directly with the database for order-related operations.
 * 
 * @author Albert
 * @version 09.11.2023
 */

public class DeliveryDAO {
    
	 /**
     * Checks the current status of a shipment based on the given order number. 
     * Retrieves the status from the database and returns it. Possible statuses include 
     * "Shipped", "In Process", "On Hold", "Cancelled", "Disputed", or "Order Not Found" 
     * if the order number does not exist.
     * 
     * @param orderNumber The unique order number for the shipment.
     * @return The status of the shipment as a String.
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