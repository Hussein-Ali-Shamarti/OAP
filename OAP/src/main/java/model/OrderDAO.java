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

/**
 * The OrderDAO class is responsible for handling all database operations related to orders.
 * This includes searching, adding, editing, fetching, and deleting order records.
 * The class utilizes prepared statements for secure interaction with the database.
 *
 * @author 7094
 */

public class OrderDAO {

	private static final String SEARCH_ORDER_SQL = "SELECT * FROM orders WHERE "
			+ "CAST(orderNumber AS CHAR) LIKE ? OR " + "CAST(orderDate AS CHAR) LIKE ? OR "
			+ "CAST(requiredDate AS CHAR) LIKE ? OR " + "CAST(shippedDate AS CHAR )LIKE ? OR " + "status LIKE ? OR "
			+ "comments LIKE ? OR " + "CAST(customerNumber AS CHAR) LIKE ?  ";


	
	
	/**
	 * Fetches orders from the database and returns them as a list of string arrays.
	 *
	 * @return A list of string arrays representing the fetched orders. Each array contains
	 *         order details such as OrderNumber, orderDate, requiredDate, shippedDate, status,
	 *         comments, and customerNumber.
	 * @throws SQLException If there is an error while interacting with the database.
	 */
    
	
	
	public List<String[]> fetchOrders() {
        List<String[]> orders = new ArrayList<>();

        try (Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String ordersSql = "SELECT o.OrderNumber, o.orderDate, o.requiredDate, o.shippedDate, o.status, o.comments, o.customerNumber "
                    + "FROM orders o ";
            ResultSet ordersResultSet = statement.executeQuery(ordersSql);
            while (ordersResultSet.next()) {
                String[] order = { ordersResultSet.getString("OrderNumber"),
                        ordersResultSet.getDate("orderDate").toString(),
                        ordersResultSet.getDate("requiredDate").toString(),
                        ordersResultSet.getDate("shippedDate") != null
                                ? ordersResultSet.getDate("shippedDate").toString()
                                : "N/A",
                        ordersResultSet.getString("status"), ordersResultSet.getString("comments"),
                        String.valueOf(ordersResultSet.getInt("customerNumber")) };
                orders.add(order);
            }
        } catch (SQLException e) {
            // Handle exceptions or log errors
        }

        return orders;
    }
	
	/**
	 * Adds a new order along with its details to the database.
	 *
	 * @param order The Order object containing order information.
	 * @param orderDetailsList The list of OrderDetails associated with this order.
	 * @return true if the order is successfully added, false otherwise..
	 */
	
	public boolean addOrder(Order order, List<OrderDetails> orderDetailsList) {
	    String insertOrderSQL = "INSERT INTO orders(requiredDate, shippedDate, status, comments, customerNumber, orderDate) VALUES(?, ?, ?, ?, ?, ?)";
	    String insertOrderDetailsSQL = "INSERT INTO orderdetails(orderNumber, productCode, quantityOrdered, priceEach, orderLineNumber) VALUES(?, ?, ?, ?, ?)";

	    Connection conn = null;
	    PreparedStatement pstmtOrder = null;
	    PreparedStatement pstmtOrderDetails = null;

	    try {
	        conn = DataBaseConnection.getConnection();
	        conn.setAutoCommit(false); 

	        
	        pstmtOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
	        pstmtOrder.setDate(1, order.getRequiredDate() != null ? new java.sql.Date(order.getRequiredDate().getTime()) : null);
	        pstmtOrder.setDate(2, order.getShippedDate() != null ? new java.sql.Date(order.getShippedDate().getTime()) : null);
	        pstmtOrder.setString(3, order.getStatus());
	        pstmtOrder.setString(4, order.getComments());
	        pstmtOrder.setInt(5, order.getCustomerNumber());
	        pstmtOrder.setDate(6, order.getOrderDate() != null ? new java.sql.Date(order.getOrderDate().getTime()) : null);

	        int affectedRowsOrder = pstmtOrder.executeUpdate();
	        if (affectedRowsOrder == 0) {
	            conn.rollback(); 
	            return false;
	        }

	       
	        ResultSet generatedKeys = pstmtOrder.getGeneratedKeys();
	        if (!generatedKeys.next()) {
	            conn.rollback(); 
	            return false;
	        }
	        int generatedOrderNumber = generatedKeys.getInt(1);

	       
	        pstmtOrderDetails = conn.prepareStatement(insertOrderDetailsSQL);
	        for (OrderDetails orderDetail : orderDetailsList) {
	            pstmtOrderDetails.setInt(1, generatedOrderNumber);
	            pstmtOrderDetails.setString(2, orderDetail.getProductCode()); 
	            pstmtOrderDetails.setInt(3, orderDetail.getQuantityOrdered());
	            pstmtOrderDetails.setDouble(4, orderDetail.getPriceEach());
	            pstmtOrderDetails.setInt(5, orderDetail.getOrderLineNr());
	            System.out.println("productCode"+orderDetail.getProductCode());
	            pstmtOrderDetails.addBatch();
	        }
	        pstmtOrderDetails.executeBatch();

	        conn.commit(); 
	        return true;
	    } catch (SQLException e) {
	        e.printStackTrace();
	        if (conn != null) {
	            try {
	                conn.rollback(); 
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        return false;
	    } finally {
	        
	        try {
	            if (pstmtOrder != null) pstmtOrder.close();
	            if (pstmtOrderDetails != null) pstmtOrderDetails.close();
	            if (conn != null) {
	                conn.setAutoCommit(true); 
	                conn.close();
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	/**
	 * Edits an existing order in the database based on the provided Order object and OrderNumber.
	 *
	 * @param order The Order object containing the updated information.
	 * @param OrderNumber The unique identifier of the order to be updated.
	 * @return true if the update is successful, false otherwise.
	 */

	
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
	
	/**
	 * Deletes an order and its associated details from the database based on the order number.
	 *
	 * @param orderNumber The unique identifier of the order to be deleted.
	 * @return true if the deletion is successful, false otherwise.
	 */
	
	public boolean deleteOrder(int orderNumber) {
	    String deleteOrderDetailsSQL = "DELETE FROM orderdetails WHERE OrderNumber = ?";
	    String deleteOrderSQL = "DELETE FROM orders WHERE OrderNumber = ?";

	    Connection conn = null;
	    PreparedStatement pstmtOrderDetails = null;
	    PreparedStatement pstmtOrder = null;

	    try {
	        conn = DataBaseConnection.getConnection();
	        conn.setAutoCommit(false); 

	        pstmtOrderDetails = conn.prepareStatement(deleteOrderDetailsSQL);
	        pstmtOrderDetails.setInt(1, orderNumber);
	        pstmtOrderDetails.executeUpdate();

	        pstmtOrder = conn.prepareStatement(deleteOrderSQL);
	        pstmtOrder.setInt(1, orderNumber);
	        int affectedRows = pstmtOrder.executeUpdate();

	        conn.commit(); 

	        return affectedRows > 0;
	    } catch (SQLException e) {
	        if (conn != null) {
	            try {
	                conn.rollback(); 
	            } catch (SQLException ex) {
	                ex.printStackTrace();
	            }
	        }
	        e.printStackTrace();
	        return false;
	    } finally {
	        try {
	            if (pstmtOrderDetails != null) pstmtOrderDetails.close();
	            if (pstmtOrder != null) pstmtOrder.close();
	            if (conn != null) conn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}
	
	
	/**
     * Searches for orders in the database based on the provided search criteria.
     * The method uses a SQL query with LIKE clauses to find matches across various order attributes.
     *
     * @param searchCriteria The string to search for in various order attributes.
     * @return A list of Order objects that match the search criteria.
     */
	
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
	
	/**
     * Maps a row from a ResultSet to an Order object.
     *
     * @param resultSet The ResultSet from a SQL query.
     * @return An Order object populated with data from the ResultSet.
     * @throws SQLException If there is an issue accessing the ResultSet data.
     */

	private Order mapResultSetToOrder(ResultSet resultSet) throws SQLException {
		return new Order(resultSet.getInt("orderNumber"), resultSet.getDate("requiredDate"),
				resultSet.getDate("shippedDate"), resultSet.getString("status"), resultSet.getString("comments"),
				resultSet.getInt("customerNumber"), resultSet.getDate("orderDate")

		);
	}

	
	/**
	 * Retrieves an order from the database based on the order number.
	 *
	 * @param OrderNumber The unique identifier of the order to be retrieved.
	 * @return An Order object if found, null otherwise.
	 */

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
				
				order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return order;
	}

	
	
	
    
	/**
	 * Inserts a list of orders into the database using batch processing.
	 *
	 * @param orders A List of Order objects representing the orders to be inserted.
	 * @return true if all orders were successfully inserted, false otherwise.
	 * @throws Exception If any error occurs during the database insertion process.
	 */
	
	 public boolean insertOrdersIntoDatabase(List<Order> orders) throws Exception {
	        String sql = "INSERT INTO orders (orderDate, requiredDate, shippedDate, status, comments, customerNumber) VALUES (?, ?, ?, ?, ?, ?)";
	        try (PreparedStatement statement = DataBaseConnection.prepareStatement(sql)) {
	            for (Order order : orders) {
	                java.sql.Date sqlOrderDate = new java.sql.Date(order.getOrderDate().getTime());
	                java.sql.Date sqlRequiredDate = new java.sql.Date(order.getRequiredDate().getTime());
	                java.sql.Date sqlShippedDate = new java.sql.Date(order.getShippedDate().getTime());

	                statement.setDate(1, sqlOrderDate);
	                statement.setDate(2, sqlRequiredDate);
	                statement.setDate(3, sqlShippedDate);
	                statement.setString(4, order.getStatus());
	                statement.setString(5, order.getComments());
	                statement.setInt(6, order.getCustomerNumber());
	                statement.addBatch();
	            }
	            int[] counts = statement.executeBatch();
	            return counts.length == orders.size();
	        } 
	    }
	 
	 /**
		 * Retrieves the status of an order based on its order number.
		 *
		 * @param orderNumber The unique identifier of the order.
		 * @return The status of the order as a String.
		 */
		
	 
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
		
		/**
		 * Checks if a customer has made any payments.
		 *
		 * @param customerNumber The unique identifier of the customer.
		 * @return true if the customer has made payments, false otherwise.
		 */

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


	 
	 //---------------------------------OrderDetails----------------------------------------------
	  /**
	     * Updates order details in the database.
	     *
	     * @param orderDetails The OrderDetails object containing the updated information.
	     * @return true if the update is successful, false otherwise.
	     * @throws SQLException If there is an error executing the SQL query.
	     */
		public boolean updateOrderDetails(OrderDetails orderDetails) throws SQLException {
		    String sql = "UPDATE orderdetails SET quantityOrdered = ?, priceEach = ? WHERE orderNumber = ? AND productCode = ?";

		    try (Connection conn = DataBaseConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        // Set the parameters for the prepared statement
		        pstmt.setInt(1, orderDetails.getQuantityOrdered());
		        pstmt.setDouble(2, orderDetails.getPriceEach());
		        pstmt.setInt(3, orderDetails.getOrderNumber());
		        pstmt.setString(4, orderDetails.getProductCode());

		        // Execute the update
		        int rowsAffected = pstmt.executeUpdate();

		        if (rowsAffected > 0) {
		            // Update stock quantity
		            return updateProductStock(orderDetails.getProductCode(), -orderDetails.getQuantityOrdered());
		        }
		    }
		    return false;
		}
		
		/**
		 * Searches for order details in the database based on the provided search criteria.
		 *
		 * @param searchCriteria The string to search for in various order detail attributes.
		 * @return A list of OrderDetails objects that match the search criteria.
		 */

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
		
		/**
		 * Deletes a specific order detail based on order number and line number.
		 *
		 * @param orderNumber The unique identifier of the order.
		 * @param orderLineNumber The line number of the order detail to be deleted.
		 * @return true if the deletion is successful, false otherwise.
		 */
		
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
		
		/**
		 * Retrieves order details based on the order number and line number.
		 *
		 * @param orderNumber The unique identifier of the order.
		 * @param orderLineNumber The line number of the order detail.
		 * @return An OrderDetails object if found, null otherwise.
		 */
		
		public OrderDetails getOrderDetails(int orderNumber, int orderLineNumber) {
		    String sql = "SELECT * FROM orderdetails WHERE orderNumber = ? AND orderLineNumber = ?";

		    try (Connection conn = DataBaseConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {
		        pstmt.setInt(1, orderNumber);
		        pstmt.setInt(2, orderLineNumber);

		        try (ResultSet rs = pstmt.executeQuery()) {
		            if (rs.next()) {
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
		    return null; 
		}
		
		
		/**
		 * SQL query for selecting all order details from the database.
		 */
		
		private static final String SELECT_ORDER_DETAILS_SQL = "SELECT * FROM orderDetails";

		
		/**
		 * Retrieves a list of order details for a specific order number from the database.
		 *
		 * @return A list of OrderDetails objects representing the details of the order.
		 */
		
		
		public List<OrderDetails> getOrderDetailsByOrderNumber() {
			List<OrderDetails> orderDetailsList = new ArrayList<>();

			try (Connection conn = DataBaseConnection.getConnection();
					PreparedStatement pstmt = conn.prepareStatement(SELECT_ORDER_DETAILS_SQL)) {

				ResultSet rs = pstmt.executeQuery();

				while (rs.next()) {

					int orderNumber = rs.getInt("orderNumber");
					String productCode = rs.getString("productCode");
					int quantityOrdered = rs.getInt("quantityOrdered");
					double priceEach = rs.getDouble("priceEach");
					int orderLineNumber = rs.getInt("orderLineNumber");

					OrderDetails orderDetails = new OrderDetails(quantityOrdered, priceEach, productCode, orderNumber,
							orderLineNumber);
					orderDetailsList.add(orderDetails);
				}

			} catch (SQLException e) {
				e.printStackTrace();

			}
			return orderDetailsList; 
		}
		

		/**
		 * Updates the quantity in stock for a given product in the database.
		 *
		 * This method adjusts the quantity in stock of a specified product. 
		 * It can be used to increase or decrease the stock based on the quantityChange value.
		 *
		 * @param productCode The code of the product for which the stock is to be updated.
		 * @param quantityChange The amount by which the stock quantity should be adjusted. 
		 *                       This value can be negative (to decrease stock) or positive (to increase stock).
		 * @return true if the update is successful (i.e., the stock quantity is updated in the database), false otherwise.
		 * @throws SQLException If there is an error executing the SQL query.
		 */
		private boolean updateProductStock(String productCode, int quantityChange) throws SQLException {
		    String sql = "UPDATE products SET quantityInStock = quantityInStock + ? WHERE productCode = ?";
		    try (Connection conn = DataBaseConnection.getConnection();
		         PreparedStatement pstmt = conn.prepareStatement(sql)) {

		        pstmt.setInt(1, quantityChange);
		        pstmt.setString(2, productCode);

		        int rowsAffected = pstmt.executeUpdate();
		        return rowsAffected > 0;
		    }
		}
		
	    /**
	     * Checks if the requested quantity of a product is available in stock.
	     * 
	     * This method queries the database to find the current stock quantity for a given product
	     * and compares it against the requested order quantity.
	     *
	     * @param productCode The product code for which stock availability is checked.
	     * @param quantityOrdered The quantity of the product being ordered.
	     * @return true if the requested quantity is less than or equal to the quantity in stock, false otherwise.
	     * @throws SQLException If a database access error occurs or the SQL query is incorrect.
	     */
	    public boolean isStockAvailable(String productCode, int quantityOrdered) throws SQLException {
	        String sql = "SELECT quantityInStock FROM products WHERE productCode = ?";
	        try (Connection conn = DataBaseConnection.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setString(1, productCode);
	            ResultSet rs = pstmt.executeQuery();
	            if (rs.next()) {
	                int quantityInStock = rs.getInt("quantityInStock");
	                return quantityOrdered <= quantityInStock;
	            }
	        }
	        return false;
	    }
	    
	    /**
		 * Checks if a customer exists in the database.
		 *
		 * @param customerNumber The unique identifier of the customer.
		 * @return true if the customer exists, false otherwise.
		 */

	    public boolean customerExists(int customerNumber) {

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
