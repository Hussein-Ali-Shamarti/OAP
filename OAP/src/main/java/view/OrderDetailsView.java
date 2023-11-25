package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import database.DataBaseConnection;
import controller.OrderHandler;

public class OrderDetailsView {
    private static final String[] COLUMN_NAMES = {
    	"Order Number",
        "Product Code",
        "Quantity Ordered",
        "Price Each",
        "Order Line Number"
    };

    private DefaultTableModel orderDetailsTableModel;
    private JTable orderDetailsTable;
    private OrderHandler orderHandler;

    public OrderDetailsView() {
        // Create a new JFrame for the order details table
        JFrame orderDetailsFrame = new JFrame("Order Details");
        orderDetailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        orderDetailsFrame.setSize(800, 600);

        // Initialize OrderHandler (ensure it is correctly initialized)
        orderHandler = new OrderHandler();

        // Create a panel to hold the order details table
        JPanel orderDetailsPanel = new JPanel(new BorderLayout());

        // Create a table to display the order details
        orderDetailsTableModel = new DefaultTableModel(null, COLUMN_NAMES) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderDetailsTable = new JTable(orderDetailsTableModel);
        orderDetailsTable.setPreferredScrollableViewportSize(new Dimension(750, 400));

        // Add the table to a scroll pane for scrolling if needed
        JScrollPane scrollPane = new JScrollPane(orderDetailsTable);
        orderDetailsPanel.add(scrollPane, BorderLayout.CENTER);

        // Add the panel to the order details frame
        orderDetailsFrame.add(orderDetailsPanel);

        // Fetch and display order details
        fetchAndDisplayOrderDetails();

        // Set the order details frame to be visible
        orderDetailsFrame.setVisible(true);
    }

    private void fetchAndDisplayOrderDetails() {
        try {
            orderDetailsTableModel.setRowCount(0); // Clear existing data

            Connection conn = null;
            Statement statement = null;
            ResultSet orderDetailsResultSet = null;

            try {
                // Establish a database connection
                conn = DataBaseConnection.getConnection();
                statement = conn.createStatement();

                // Define the SQL query with a parameterized query
                String orderDetailsSql = "SELECT * FROM orderDetails ";

                // Create a PreparedStatement with the parameter
                PreparedStatement pstmt = conn.prepareStatement(orderDetailsSql);

                // Execute the query
                orderDetailsResultSet = pstmt.executeQuery();

                // Iterate through the order details and display the data
                while (orderDetailsResultSet.next()) {
                    Object[] row = {
                    	orderDetailsResultSet.getInt("orderNumber"),
                        orderDetailsResultSet.getString("productCode"),
                        orderDetailsResultSet.getInt("quantityOrdered"),
                        orderDetailsResultSet.getDouble("priceEach"),
                        orderDetailsResultSet.getInt("orderLineNumber")
                    };
                    orderDetailsTableModel.addRow(row);
                }
            } finally {
                // Close database resources in a finally block
                if (orderDetailsResultSet != null) {
                    orderDetailsResultSet.close();
                }
                if (statement != null) {
                    statement.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching order details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
