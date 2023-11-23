  

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import database.DataBaseConnection;
import model.Employee;
import model.Order;


public class OrderHandler {
	
	/*public List<Order> searchOrders(String searchText) {
		System.out.println("sjekk parameter "+searchText);
	    List<Order> searchResults = new ArrayList<>();
	    //String searchQuery = "SELECT * FROM orders WHERE CONCAT(orderDate, requiredDate, shippedDate, status, comments, customerNumber) LIKE ?";
	    String searchQuery = "SELECT * FROM orders WHERE orderDate LIKE ? OR requiredDate = ? OR shippedDate LIKE ? OR status LIKE ? OR comments LIKE ? OR customerNumber LIKE ?";
	    try {
	    	Connection conn = DataBaseConnection.getConnection();

     PreparedStatement pstmt = conn.prepareStatement(searchQuery); 
	        
	        pstmt.setString(1, "%" + searchText + "%");
	        ResultSet rs = pstmt.executeQuery();
	        System.out.println(rs);
	        Order order= null; 
	        if (rs.next()) {
                Date orderDate = rs.getDate("orderDate");
                Date requiredDate = rs.getDate("requiredDate");
                Date shippedDate = rs.getDate("shippedDate");
                String status = rs.getString("status");
                String comments = rs.getString("comments");
                int customerNumber = rs.getInt("customerNumber");
                String productCode = rs.getString("productCode"); // Fetch the product code

                order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
                searchResults.add(order);
	        }
	        System.out.println("searchresult: "+searchResults);
	    }catch(SQLException e) {
	    	e.printStackTrace();
	    }
		return searchResults;

	    
	  
	} */
	
	private static final String SEARCH_ORDER_SQL = 
	        "SELECT * FROM orders WHERE " +
	        "CAST(orderDate AS CHAR) LIKE ? OR " +
	        "CAST(requiredDate AS CHAR) LIKE ? OR " +
	        "CAST(shippedDate AS CHAR )LIKE ? OR " +
	        "status LIKE ? OR " +
	        "comments LIKE ? OR " +
	        "CAST(customerNumber AS CHAR) LIKE ?  ";
	       

	    public List<Order> searchOrder(String searchCriteria) {
	        List<Order> searchResults = new ArrayList<>();
	        
	        try (Connection connection = DataBaseConnection.getConnection();
	             PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_ORDER_SQL)) {

	            for (int i = 1; i <= 6; i++) {
	                preparedStatement.setString(i, "%" + searchCriteria + "%");
	            }

	            try (ResultSet resultSet = preparedStatement.executeQuery()) {
	                while (resultSet.next()) {
	                    Order order = mapResultSetToOrder(resultSet);
	                    searchResults.add(order);
	                }
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return searchResults;
	    }
	    private Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
	        return new Order(
	        		resultSet.getDate("requiredDate"),
	            resultSet.getDate("shippedDate"),
	            resultSet.getString("status"),
	            resultSet.getString("comments"),
	            resultSet.getInt("customerNumber"),
	            resultSet.getDate("orderDate")


	        );
	    }
	


    // CRUD-methods

	public boolean addOrder(Order order) {
	    String insertOrderSQL = "INSERT INTO orders( requireddate, shippeddate, status, comments, customernumber, orderdate) VALUES ( ?, ?, ?, ?, ?, ?)";
	    
	    try (Connection conn = DataBaseConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(insertOrderSQL)) {
	       // pstmt.setInt(1, order.getOrderNumber());
	        pstmt.setDate(1, order.getRequiredDate() != null ? new java.sql.Date(order.getRequiredDate().getTime()) : null);
	        pstmt.setDate(2, order.getShippedDate() != null ? new java.sql.Date(order.getShippedDate().getTime()) : null);
	        pstmt.setString(3, order.getStatus());
	        pstmt.setString(4, order.getComments());
	        pstmt.setInt(5, order.getCustomerNumber());
	        pstmt.setDate(6, new java.sql.Date(order.getOrderDate().getTime()));

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
               // String productCode = rs.getString("productCode"); // Fetch the product code

                order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return order;
    }


    // Add this method to your OrderHandler class
    public String getOrderStatus(int orderNumber) {
        String selectOrderStatusSQL = "SELECT status FROM orders WHERE OrderNumber = ?";
        String status = null;
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(selectOrderStatusSQL)) {
            pstmt.setInt(1, orderNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                status = rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
    
    public boolean checkPaymentStatus(int customerNumber) {
        String checkPaymentStatusSQL = "SELECT COUNT(*) FROM payments WHERE customerNumber = ? AND paymentDate IS NOT NULL";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkPaymentStatusSQL)) {
            pstmt.setInt(1, customerNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public boolean customerExists(int customerNumber) {
        // Your code to check if the customer exists
        String checkCustomerExistsSQL = "SELECT COUNT(*) FROM customers WHERE customerNumber = ?";
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(checkCustomerExistsSQL)) {
            pstmt.setInt(1, customerNumber);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}

