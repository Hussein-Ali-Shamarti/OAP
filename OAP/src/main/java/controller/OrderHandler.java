/**
 * File: OrderHandler.java
 * Description:
 * Manages order operations (add, update, delete, retrieve) within a CMS, ensuring proper handling and status tracking of orders.
 * @author Hussein
 * @version 09.11.2023
 */


package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DataBaseConnection;
import model.Order;


public class OrderHandler {


    // CRUD-methods

	public boolean addOrder(Order order) {
	    String insertOrderSQL = "INSERT INTO orders(ordernumber, requireddate, shippeddate, status, comments, customernumber, orderdate) VALUES (?, ?, ?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DataBaseConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(insertOrderSQL)) {
	        pstmt.setInt(1, order.getOrderNumber());
	        pstmt.setDate(2, order.getRequiredDate() != null ? new java.sql.Date(order.getRequiredDate().getTime()) : null);
	        pstmt.setDate(3, order.getShippedDate() != null ? new java.sql.Date(order.getShippedDate().getTime()) : null);
	        pstmt.setString(4, order.getStatus());
	        pstmt.setString(5, order.getComments());
	        pstmt.setInt(6, order.getCustomerNumber());
	        pstmt.setDate(7, new java.sql.Date(order.getOrderDate().getTime()));

	        int affectedRows = pstmt.executeUpdate();
	        if (affectedRows > 0) {
	            return true;
	        } else {
	            return false;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


    // Update
    public boolean editOrder(Order order, int OrderNumber) {
        String updateOrderSQL = "UPDATE orders SET requiredDate = ?, shippedDate = ?, status = ?, comments = ?, customerNumber = ?, orderDate = ? WHERE OrderNumber = ?";
        
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(updateOrderSQL)) {
            pstmt.setDate(1, order.getRequiredDate() != null ? new java.sql.Date(order.getRequiredDate().getTime()) : null);
            pstmt.setDate(2, order.getShippedDate() != null ? new java.sql.Date(order.getShippedDate().getTime()) : null);
            pstmt.setString(3, order.getStatus());
            pstmt.setString(4, order.getComments());
            pstmt.setInt(5, order.getCustomerNumber());
            pstmt.setDate(6, new java.sql.Date(order.getOrderDate().getTime()));
            pstmt.setInt(7, OrderNumber);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Delete
    public boolean deleteOrder(int OrderNumber) {
        String deleteOrderSQL = "DELETE FROM orders WHERE OrderNumber = ?";
        
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteOrderSQL)) {
            
            pstmt.setInt(1, OrderNumber);
            
            int affectedRows = pstmt.executeUpdate();
            System.out.println("slettet");
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read
    public Order getOrder(int OrderNumber) {
        String selectOrderSQL = "SELECT * FROM orders WHERE OrderNumber = ?";
        Order order = null;

        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectOrderSQL)) {

            pstmt.setInt(1, OrderNumber);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Date orderDate = rs.getDate("orderDate");
                Date requiredDate = rs.getDate("requiredDate");
                Date shippedDate = rs.getDate("shippedDate");
                String status = rs.getString("status");
                String comments = rs.getString("comments");
                int customerNumber = rs.getInt("customerNumber");

                order = new Order(OrderNumber, requiredDate, shippedDate, status, comments, customerNumber, orderDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }



}
