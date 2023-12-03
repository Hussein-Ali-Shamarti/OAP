
/**
 * File: OrderHandler.java
 * Description:
 * Manages order operations (add, update, delete, retrieve) within a CMS, ensuring proper handling and status tracking of orders.
 * @author Hussein
 * @version 09.11.2023
 */

package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import database.DataBaseConnection;


public class OrderDAO {

	private static final String SEARCH_ORDER_SQL = "SELECT * FROM orders WHERE "
			+ "CAST(orderNumber AS CHAR) LIKE ? OR " + "CAST(orderDate AS CHAR) LIKE ? OR "
			+ "CAST(requiredDate AS CHAR) LIKE ? OR " + "CAST(shippedDate AS CHAR )LIKE ? OR " + "status LIKE ? OR "
			+ "comments LIKE ? OR " + "CAST(customerNumber AS CHAR) LIKE ?  ";

	public List<Order> searchOrder(String searchCriteria) {
		List<Order> searchResults = new ArrayList<>();

		try (Connection connection = DataBaseConnection.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(SEARCH_ORDER_SQL)) {

			for (int i = 1; i <= 7; i++) {
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
		return new Order(resultSet.getInt("orderNumber"), resultSet.getDate("requiredDate"),
				resultSet.getDate("shippedDate"), resultSet.getString("status"), resultSet.getString("comments"),
				resultSet.getInt("customerNumber"), resultSet.getDate("orderDate")

		);
	}


	// CRUD-methods

	public boolean addOrder(Order order, List<OrderDetails> orderDetailsList) {
	    String insertOrderSQL = "INSERT INTO orders(requiredDate, shippedDate, status, comments, customerNumber, orderDate) VALUES(?, ?, ?, ?, ?, ?)";
	    String insertOrderDetailsSQL = "INSERT INTO orderdetails(orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber) VALUES(?, ?, ?, ?, ?)";

	    Connection conn = null;
	    PreparedStatement pstmtOrder = null;
	    PreparedStatement pstmtOrderDetails = null;

	    try {
	        conn = DataBaseConnection.getConnection();
	        conn.setAutoCommit(false); // Start transaction

	        // Insert the order
	        pstmtOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
	        pstmtOrder.setDate(1, order.getRequiredDate() != null ? new java.sql.Date(order.getRequiredDate().getTime()) : null);
	        pstmtOrder.setDate(2, order.getShippedDate() != null ? new java.sql.Date(order.getShippedDate().getTime()) : null);
	        pstmtOrder.setString(3, order.getStatus());
	        pstmtOrder.setString(4, order.getComments());
	        pstmtOrder.setInt(5, order.getCustomerNumber());
	        pstmtOrder.setDate(6, order.getOrderDate() != null ? new java.sql.Date(order.getOrderDate().getTime()) : null);

	        int affectedRowsOrder = pstmtOrder.executeUpdate();
	        if (affectedRowsOrder == 0) {
	            conn.rollback(); // Rollback transaction if the order insertion failed
	            return false;
	        }

	        // Retrieve generated order number
	        ResultSet generatedKeys = pstmtOrder.getGeneratedKeys();
	        if (!generatedKeys.next()) {
	            conn.rollback(); // Rollback transaction if no order number generated
	            return false;
	        }
	        int generatedOrderNumber = generatedKeys.getInt(1);

	        // Insert each order detail
	        pstmtOrderDetails = conn.prepareStatement(insertOrderDetailsSQL);
	        for (OrderDetails orderDetail : orderDetailsList) {
	            pstmtOrderDetails.setInt(1, generatedOrderNumber);
	            pstmtOrderDetails.setString(2, orderDetail.getProductCode());
	            pstmtOrderDetails.setInt(3, orderDetail.getQuantityOrdered());
	            pstmtOrderDetails.setDouble(4, orderDetail.getPriceEach());
	            pstmtOrderDetails.setInt(5, orderDetail.getOrderLineNr());

	            pstmtOrderDetails.addBatch();
	        }
	        pstmtOrderDetails.executeBatch();

	        conn.commit(); // Commit the transaction
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        if (conn != null) {
	            try {
	                conn.rollback(); // Rollback transaction on error
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return false;
	    } finally {
	        // Clean up resources
	        try {
	            if (pstmtOrder != null) pstmtOrder.close();
	            if (pstmtOrderDetails != null) pstmtOrderDetails.close();
	            if (conn != null) {
	                conn.setAutoCommit(true); // Reset auto-commit to default
	                conn.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}


	// Update
	public boolean editOrder(Order order, int OrderNumber) {
		String updateOrderSQL = "UPDATE orders SET requiredDate = ?, shippedDate = ?, status = ?, comments = ?, customerNumber = ?, orderDate = ? WHERE OrderNumber = ?";

		try (Connection conn = DataBaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(updateOrderSQL)) {
			pstmt.setDate(1,
					order.getRequiredDate() != null ? new java.sql.Date(order.getRequiredDate().getTime()) : null);
			pstmt.setDate(2,
					order.getShippedDate() != null ? new java.sql.Date(order.getShippedDate().getTime()) : null);
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
	//Delete for OrderDetailsView
	public OrderDetails getOrderDetails(int orderNumber, int orderLineNumber) {
	    String sql = "SELECT * FROM orderdetails WHERE orderNumber = ? AND orderLineNumber = ?";

	    try (Connection conn = DataBaseConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {
	        pstmt.setInt(1, orderNumber);
	        pstmt.setInt(2, orderLineNumber);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                // Assuming you have a constructor in OrderDetails class that accepts these parameters
	                return new OrderDetails(
	                    rs.getInt("quantityOrdered"),
	                    rs.getDouble("priceEach"),
	                    rs.getString("productCode"),
	                    orderNumber,
	                    orderLineNumber
	                );
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return null; // or handle this case as needed
	}
	// Delete
	public boolean deleteOrder(int orderNumber) {
	    String deleteOrderDetailsSQL = "DELETE FROM orderdetails WHERE OrderNumber = ?";
	    String deleteOrderSQL = "DELETE FROM orders WHERE OrderNumber = ?";

	    Connection conn = null;
	    PreparedStatement pstmtOrderDetails = null;
	    PreparedStatement pstmtOrder = null;

	    try {
	        conn = DataBaseConnection.getConnection();
	        conn.setAutoCommit(false); // Start transaction

	        // First, delete child records in orderdetails
	        pstmtOrderDetails = conn.prepareStatement(deleteOrderDetailsSQL);
	        pstmtOrderDetails.setInt(1, orderNumber);
	        pstmtOrderDetails.executeUpdate();

	        // Then, delete the order
	        pstmtOrder = conn.prepareStatement(deleteOrderSQL);
	        pstmtOrder.setInt(1, orderNumber);
	        int affectedRows = pstmtOrder.executeUpdate();

	        conn.commit(); // Commit transaction

	        return affectedRows > 0;
	    } catch (SQLException e) {
	        if (conn != null) {
	            try {
	                conn.rollback(); // Rollback transaction on error
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        // Clean up resources
	        try {
	            if (pstmtOrderDetails != null) pstmtOrderDetails.close();
	            if (pstmtOrder != null) pstmtOrder.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
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
	
	// In your OrderDAO class
	public boolean deleteOrderDetail(int orderNumber, int orderLineNumber) {
	    String deleteOrderDetailSQL = "DELETE FROM orderdetails WHERE OrderNumber = ? AND orderLineNumber = ?";

	    try (Connection conn = DataBaseConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(deleteOrderDetailSQL)) {

	        pstmt.setInt(1, orderNumber);
	        pstmt.setInt(2, orderLineNumber);

	        int affectedRows = pstmt.executeUpdate();
	        System.out.println("Deleted order detail with OrderNumber: " + orderNumber + " and orderLineNumber: " + orderLineNumber);

	        return affectedRows > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    }
	}


	// Define SQL queries for the "orderDetails" table
	private static final String SELECT_ORDER_DETAILS_SQL = "SELECT * FROM orderDetails";

	// Add more SQL queries for CRUD operations if needed

	// Retrieve order details by order number
	public List<OrderDetails> getOrderDetailsByOrderNumber() {
		List<OrderDetails> orderDetailsList = new ArrayList<>();

		try (Connection conn = DataBaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SELECT_ORDER_DETAILS_SQL)) {

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				// Retrieve data from the result set and create OrderDetails objects
				int orderNumber = rs.getInt("orderNumber");
				String productCode = rs.getString("productCode");
				int quantityOrdered = rs.getInt("quantityOrdered");
				double priceEach = rs.getDouble("priceEach");
				int orderLineNumber = rs.getInt("orderLineNumber");

				// Pass orderNumber as well when creating OrderDetails
				OrderDetails orderDetails = new OrderDetails(quantityOrdered, priceEach, productCode, orderNumber,
						orderLineNumber);
				orderDetailsList.add(orderDetails);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// Add logging here to log the error message and details
		}
		return orderDetailsList; // Return the list of order details
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
	
	// Add this method to the OrderDAO class
	public List<OrderDetails> searchOrderDetails(String searchCriteria) {
	    List<OrderDetails> searchResults = new ArrayList<>();
	    String searchOrderDetailsSQL = "SELECT * FROM orderdetails WHERE " +
	        "CAST(orderNumber AS CHAR) LIKE ? OR " +
	        "productCode LIKE ? OR " +
	        "CAST(quantityOrdered AS CHAR) LIKE ? OR " +
	        "CAST(priceEach AS CHAR) LIKE ? OR " +
	        "CAST(orderLineNumber AS CHAR) LIKE ?";

	    try (Connection connection = DataBaseConnection.getConnection();
	         PreparedStatement preparedStatement = connection.prepareStatement(searchOrderDetailsSQL)) {
	        
	        // Setting the search criteria for all the placeholders
	        for (int i = 1; i <= 5; i++) {
	            preparedStatement.setString(i, "%" + searchCriteria + "%");
	        }

	        try (ResultSet resultSet = preparedStatement.executeQuery()) {
	            while (resultSet.next()) {
	                OrderDetails orderDetails = new OrderDetails(
	                    resultSet.getInt("quantityOrdered"),
	                    resultSet.getDouble("priceEach"),
	                    resultSet.getString("productCode"),
	                    resultSet.getInt("orderNumber"),
	                    resultSet.getInt("orderLineNumber")
	                );
	                searchResults.add(orderDetails);
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return searchResults;
	}

	/*public boolean checkProductCodeExists(String productCode) {
	    String query = "SELECT COUNT(*) FROM products WHERE productCode = ?";
	    try (Connection conn = DataBaseConnection.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(query)) {
	        
	        pstmt.setString(1, productCode);
	        
	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) > 0; // Check if count is greater than zero
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        // Handle exception, possibly log it or throw a custom exception
	    }
	    return false; // Return false if productCode does not exist or in case of an exception
	}
*/

}
