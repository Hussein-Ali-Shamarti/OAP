package view;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import database.DataBaseConnection;
import controller.OrderHandler;

public class OrderDetailsView extends JFrame {
    private static final long serialVersionUID = 1L;

	private static final String[] COLUMN_NAMES = {
        "Order Number",
        "Product Code",
        "Quantity Ordered",
        "Price Each",
        "Order Line Number"
    };

    private DefaultTableModel orderDetailsTableModel;
    private JTable orderDetailsTable;

    public OrderDetailsView() {
        initializeUI();
        fetchAndDisplayOrderDetails();
        setVisible(true);
    }

    private void initializeUI() {
        setTitle("Order Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        setupTitlePanel();
        setupTable();
        add(new JScrollPane(orderDetailsTable), BorderLayout.CENTER);
    }

    private void setupTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131));
        JLabel titleLabel = new JLabel("Order Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);
    }


 

    private void setupTable() {
        orderDetailsTableModel = new DefaultTableModel(null, COLUMN_NAMES) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderDetailsTable = new JTable(orderDetailsTableModel);
        orderDetailsTable.setPreferredScrollableViewportSize(new Dimension(750, 400));
        orderDetailsTable.setFillsViewportHeight(true);
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
