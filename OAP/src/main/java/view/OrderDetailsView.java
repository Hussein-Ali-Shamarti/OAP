package view;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import database.DataBaseConnection;

public class OrderDetailsView extends JFrame {
    private static final long serialVersionUID = 1L;
    private JLabel totalLabel; // Label for displaying the total
    private JTextField orderNumberInput;
    private JButton calculateButton;
    
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
        setupOrderInput();
        setupTable();
        setupTotalLabel();
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
    
    private void setupOrderInput() {
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel inputLabel = new JLabel("Enter Order Number:");
        orderNumberInput = new JTextField(10);
        calculateButton = new JButton("Calculate Total Order");
        calculateButton.addActionListener(e -> calculateAndDisplayTotalForOrder());
        inputPanel.add(inputLabel);
        inputPanel.add(orderNumberInput);
        inputPanel.add(calculateButton);

        // Place the input panel at the top, above the title panel
        getContentPane().add(inputPanel, BorderLayout.NORTH);
        inputPanel.setBackground(new Color(84, 11, 131));
        inputLabel.setForeground(Color.WHITE); // Set the text color to white

    }
    private void setupTable() {
        orderDetailsTableModel = new DefaultTableModel(null, COLUMN_NAMES) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderDetailsTable = new JTable(orderDetailsTableModel);
        orderDetailsTable.setPreferredScrollableViewportSize(new Dimension(750, 400));
        orderDetailsTable.setFillsViewportHeight(true);
    }

    private void setupTotalLabel() {
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        totalLabel.setForeground(Color.WHITE); // Set the text color to white

        JPanel totalPanel = new JPanel(new BorderLayout());
        totalPanel.setBackground(new Color(84, 11, 131)); // Ensure this is the color you want for the background
        totalPanel.add(totalLabel, BorderLayout.CENTER);

        add(totalPanel, BorderLayout.SOUTH);
    }

    
    private void calculateAndDisplayTotalForOrder() {
        String orderNumberStr = orderNumberInput.getText();
        if (orderNumberStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an order number.");
            return;
        }

        int orderNumber;
        try {
            orderNumber = Integer.parseInt(orderNumberStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid order number format.");
            return;
        }

        BigDecimal total = calculateTotalForOrderNumber(orderNumber);
        updateOrderTotal(total); // Update the total label
    }

    // Helper method to calculate the total for a specific order number
    private BigDecimal calculateTotalForOrderNumber(int orderNumber) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < orderDetailsTableModel.getRowCount(); i++) {
            int currentOrderNumber = (Integer) orderDetailsTableModel.getValueAt(i, 0); // Order Number column
            if (currentOrderNumber == orderNumber) {
                int quantity = (Integer) orderDetailsTableModel.getValueAt(i, 2); // Quantity Ordered column
                BigDecimal price = BigDecimal.valueOf((Double) orderDetailsTableModel.getValueAt(i, 3)); // Price Each column
                total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP); // Assuming a scale of 2 for currency
    }


    private void fetchAndDisplayOrderDetails() {
        try {
            orderDetailsTableModel.setRowCount(0); // Clear existing data

            Connection conn = DataBaseConnection.getConnection();
            String orderDetailsSql = "SELECT * FROM orderDetails ";
            PreparedStatement pstmt = conn.prepareStatement(orderDetailsSql);
            ResultSet orderDetailsResultSet = pstmt.executeQuery();

            BigDecimal total = BigDecimal.ZERO; // Initialize total

            // Iterate through the order details and display the data
            while (orderDetailsResultSet.next()) {
                int quantity = orderDetailsResultSet.getInt("quantityOrdered");
                double priceEach = orderDetailsResultSet.getDouble("priceEach");
                BigDecimal price = BigDecimal.valueOf(priceEach);
                BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
                total = total.add(subtotal); // Add to total

                Object[] row = {
                    orderDetailsResultSet.getInt("orderNumber"),
                    orderDetailsResultSet.getString("productCode"),
                    quantity,
                    priceEach,
                    orderDetailsResultSet.getInt("orderLineNumber")
                };
                orderDetailsTableModel.addRow(row);
            }

            updateOrderTotal(total); // Update the total label

            // Close resources
            orderDetailsResultSet.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching order details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateOrderTotal(BigDecimal total) {
        totalLabel.setText("Total: $" + total.setScale(2, RoundingMode.HALF_UP).toPlainString());
    }
}
