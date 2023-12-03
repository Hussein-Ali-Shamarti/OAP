/**
 * Represents an order entity with information such as order number, dates, status, comments,

 * customer number, and associated order date.
 * 
 * <p>Orders may also contain order details, which are not implemented in this version.</p>
 * 
 * @author Hussein
 * @version 07.11.2023
 */
package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;

import model.OrderDAO;
import model.ProductDAO;
import database.DataBaseConnection;
import model.OrderDetails;
import model.Products;

public class OrderDetailsView extends JFrame {
	private static final long serialVersionUID = 1L;
    private JLabel totalLabel; // Label for displaying the total
    private JTextField orderNumberInput;
    private JButton calculateButton;
	private JTextField quantityInStockField;
	private JTextField buyPriceField;
	private JTextField msrpField;
	private JTextField textField;
    private JTextField orderLineNumberField;
    private JTextField productCodeField;
    private JTextField quantityOrderedField;
	private DefaultTableModel tableModel;
    private OrderDAO orderDAO;
	private JComboBox<String> productNameDropdown;
	private JComboBox<String> productCodeDropdown;
	private JTextField searchField; 
	private JButton searchButton;  
	private ProductDAO productHandler;
	private Map<String, String> products; // Declare products here

    private static final String[] COLUMN_NAMES = {
        "Order Number",
        "Product Code",
        "Quantity Ordered",
        "Price Each",
        "Order Line Number"
    };

    private JTable orderDetailsTable;

    public OrderDetailsView() {
		super();
        this.orderDAO = new OrderDAO(); // Initialize OrderHandler first
		this.productHandler = new ProductDAO();
		this.products = productHandler.getProducts(); // Initialize products
		this.revalidate();
		this.repaint();

		productHandler = new ProductDAO();
        
        orderNumberInput = new JTextField(10);
        orderLineNumberField = new JTextField(10);
        quantityOrderedField = new JTextField(10);
        productCodeField = new JTextField(10);
        
        initializeUI();
        fetchAndDisplayOrderDetails();
        setVisible(true);
   

                
        JPanel dummyPanel = new JPanel();
        add(dummyPanel);
        dummyPanel.requestFocusInWindow();
		UpdateButtonListener updateButtonListener = new UpdateButtonListener(orderDAO, productHandler);

    }
    
    private void initializeUI() {
        setTitle("Order Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setupControlPanel(); // Ensure this is only called once

        setupTopPanel();
        setupTable();
        add(new JScrollPane(orderDetailsTable), BorderLayout.CENTER);
    }
    
    private void fetchAndDisplayOrderDetails() {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orderdetails");
             ResultSet rs = pstmt.executeQuery()) {

            tableModel  = (DefaultTableModel) orderDetailsTable.getModel();
            tableModel.setRowCount(0);

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("orderNumber"),
                    rs.getString("productCode"),
                    rs.getInt("quantityOrdered"),
                    rs.getDouble("priceEach"),
                    rs.getInt("orderLineNumber")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching order details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    
    private void setupProductDropdowns() {
        productNameDropdown = new JComboBox<>();
        productCodeDropdown = new JComboBox<>();

        // Populate dropdowns using the productDAO instance
        Map<String, String> products = productHandler.getProducts(); // Use instance of ProductDAO
        for (String productName : products.keySet()) {
            productNameDropdown.addItem(productName);
            productCodeDropdown.addItem(products.get(productName));
        }

        productCodeDropdown.setEditable(false);
        productCodeDropdown.setFocusable(false);
        ((JTextField) productCodeDropdown.getEditor().getEditorComponent()).setDisabledTextColor(Color.BLACK);

        productCodeDropdown.addActionListener(e -> {
            String selectedCode = (String) productCodeDropdown.getSelectedItem();
            String productName = productHandler.getProductNameByCode(selectedCode); // Use instance of ProductDAO
            if (productName != null) {
                productNameDropdown.setSelectedItem(productName);
                Map<String, Object> productDetails = productHandler.getProductDetailsByName(productName); // Use instance of ProductDAO
                updateProductDetailsFields(productDetails);
            }
        });
        
    }
    
    private void updateProductDetailsFields(Map<String, Object> productDetails) {
        if (productDetails != null && !productDetails.isEmpty()) {
            quantityInStockField.setText(productDetails.get("quantityInStock").toString());
            buyPriceField.setText(productDetails.get("buyPrice").toString());
            msrpField.setText(productDetails.get("MSRP").toString());
        } else {
            quantityInStockField.setText("");
            buyPriceField.setText("");
            msrpField.setText("");
        }
    }
	
	private String findProductCodeByName(String code) {
		return productHandler.getProductNameByCode(code); // Use the new method from ProductDAO
	}
	
	private void searchOrderDetails() {
	    String searchCriteria = searchField.getText();
	    List<OrderDetails> searchResults = orderDAO.searchOrderDetails(searchCriteria);

	    if (searchResults == null || searchResults.isEmpty()) {
	        System.out.println("No results found for: " + searchCriteria);
	    } else {	   
	    	 updateOrderDetailsTable(searchResults);

	        System.out.println("Found " + searchResults.size() + " results for: " + searchCriteria);
	    }

	    // Update the table on the Event Dispatch Thread
	}



	private void updateOrderDetailsTable(List<OrderDetails> searchResults) {
		tableModel  =  (DefaultTableModel) orderDetailsTable.getModel();
	    tableModel.setRowCount(0); // Clear existing rows
	    for (OrderDetails orderDetail : searchResults) {
	        Object[] rowData = {
	            orderDetail.getOrderNumber(),
	            orderDetail.getProductCode(),
	            orderDetail.getQuantityOrdered(),
	            orderDetail.getPriceEach(),
	            orderDetail.getOrderLineNr()
	        };
	       System.out.println(orderDetail.getOrderLineNr());
	        tableModel.addRow(rowData);
	    }

	    // Print out the updated table data for debugging
	    for (int i = 0; i < tableModel.getRowCount(); i++) {
	        for (int j = 0; j < tableModel.getColumnCount(); j++) {
	            System.out.print(tableModel.getValueAt(i, j) + " ");
	        }
	        System.out.println();
	    }
	}







    
    private void setupTopPanel() {
        // Main top panel with BorderLayout to contain both title and search panels
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(84, 11, 131));

        // Title label
        JLabel titleLabel = new JLabel("Order Details");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        topPanel.add(titleLabel, BorderLayout.NORTH);

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.addActionListener(e -> searchOrderDetails());
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.setBackground(new Color(84, 11, 131));
        topPanel.add(searchPanel, BorderLayout.CENTER);

        // Add the combined top panel to the NORTH region of the content pane
        getContentPane().add(topPanel, BorderLayout.NORTH);
    }


    private void setupControlPanel() {
        // Initialize the control panel with FlowLayout for button alignment
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        // Create edit and delete buttons
        JButton editButton = createButton("Edit", new UpdateButtonListener(orderDAO, productHandler));
        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(new DeleteButtonListener());

        // Initialize the order number input field for total calculation
        orderNumberInput = new JTextField("Enter Order Number To Calculate", 18);
        orderNumberInput.setForeground(Color.GRAY);
        orderNumberInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField) e.getComponent();
                if ("Enter Order Number To Calculate".equals(source.getText())) {
                    source.setText("");
                    source.setForeground(Color.BLACK);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                JTextField source = (JTextField) e.getComponent();
                if (source.getText().isEmpty()) {
                    source.setForeground(Color.GRAY);
                    source.setText("Enter Order Number To Calculate");
                }
            }
        });

        JButton calculateButton = new JButton("Calculate Total Order");
        calculateButton.addActionListener(e -> calculateAndDisplayTotalForOrder());

        // Initialize the total label
        totalLabel = new JLabel("Total: $0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
        totalLabel.setForeground(Color.WHITE);

        // Add the components to the control panel
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(orderNumberInput);
        controlPanel.add(calculateButton);
        controlPanel.add(totalLabel);

        // Set the background color for the control panel
        controlPanel.setBackground(new Color(84, 11, 131));

        // Add the control panel to the bottom (SOUTH) of the frame
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void calculateAndDisplayTotalForOrder() {
        String orderNumberStr = orderNumberInput.getText().trim();
        if (orderNumberStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an order number.");
            return;
        }

        try {
            int orderNumber = Integer.parseInt(orderNumberStr);
            if (!isOrderNumberPresentInTable(orderNumber)) {
                // Fetch the specific order details if not present in the table
                fetchSpecificOrderDetails(orderNumber);
            }
            BigDecimal total = calculateTotalForOrderNumber(orderNumber);
            updateOrderTotal(total);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid order number format.");
        }
    }

    private BigDecimal calculateTotalForOrderNumber(int orderNumber) {
        BigDecimal total = BigDecimal.ZERO;
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (((Integer) tableModel.getValueAt(i, 0)).intValue() == orderNumber) {
                int quantity = (Integer) tableModel.getValueAt(i, 2);
                BigDecimal price = new BigDecimal(tableModel.getValueAt(i, 3).toString());
                total = total.add(price.multiply(BigDecimal.valueOf(quantity)));
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }


    private void updateOrderTotal(BigDecimal total) {
        totalLabel.setText("Total: $" + total.toPlainString());
    }
    
    private boolean isOrderNumberPresentInTable(int orderNumber) {
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (((Integer) tableModel.getValueAt(i, 0)).intValue() == orderNumber) {
                return true;
            }
        }
        return false;
    }

    private void fetchSpecificOrderDetails(int orderNumber) {
        try (Connection conn = DataBaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orderdetails WHERE orderNumber = ?")) {
            
            pstmt.setInt(1, orderNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("orderNumber"),
                        rs.getString("productCode"),
                        rs.getInt("quantityOrdered"),
                        rs.getDouble("priceEach"),
                        rs.getInt("orderLineNumber")
                    };
                    tableModel.addRow(row);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching specific order details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }


	
	private JButton createButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.setForeground(Color.BLACK);
		button.setBackground(new Color(84, 11, 131));
		button.setFocusPainted(false);
		button.addActionListener(listener);
		return button;
	}
	
    private void setupTable() {
    	tableModel = new DefaultTableModel(null, COLUMN_NAMES) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        orderDetailsTable = new JTable(tableModel);
        orderDetailsTable.setPreferredScrollableViewportSize(new Dimension(750, 400));
        orderDetailsTable.setFillsViewportHeight(true);
    }

    
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Create a panel to hold the input fields
            JPanel panel = new JPanel(new GridLayout(0, 1));
            JTextField orderNumberField = new JTextField(5);
            JTextField orderLineNumberField = new JTextField(5);

            // Add the fields to the panel with labels
            panel.add(new JLabel("Order Number:"));
            panel.add(orderNumberField);
            panel.add(new JLabel("Order Line Number:"));
            panel.add(orderLineNumberField);

            // Show the panel in a confirm dialog
            int result = JOptionPane.showConfirmDialog(
                OrderDetailsView.this,
                panel,
                "Enter Order Number and Order Line Number",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
            );

            if (result == JOptionPane.OK_OPTION) {
                try {
                    int orderNumber = Integer.parseInt(orderNumberField.getText().trim());
                    int orderLine = Integer.parseInt(orderLineNumberField.getText().trim());

                    // Confirm deletion
                    int confirm = JOptionPane.showConfirmDialog(
                        OrderDetailsView.this,
                        "Are you sure you want to delete order number " + orderNumber +
                        " and order line number " + orderLine + "?",
                        "Confirm Deletion",
                        JOptionPane.YES_NO_OPTION
                    );

                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = orderDAO.deleteOrderDetail(orderNumber, orderLine);
                        if (success) {
                            // Remove row from table and show success message
                            // Note: You may need to implement a method to find and remove the row based on orderNumber and orderLine
                            JOptionPane.showMessageDialog(
                                OrderDetailsView.this,
                                "Order detail deleted successfully."
                            );
                        } else {
                            JOptionPane.showMessageDialog(
                                OrderDetailsView.this,
                                "Error deleting order detail."
                            );
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        OrderDetailsView.this,
                        "Please enter valid numbers for Order Number and Order Line Number."
                    );
                }
            }
        }
    }


    
    public class UpdateButtonListener implements ActionListener {
        private OrderDAO orderDAO;
        private ProductDAO productDAO;

        public UpdateButtonListener(OrderDAO orderDAO, ProductDAO productDAO) {
            this.orderDAO = orderDAO;
            this.productDAO = productDAO;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String orderNumberString = JOptionPane.showInputDialog("Enter Order Number to update:");
            String orderLineNumberString = JOptionPane.showInputDialog("Enter Order Line Number:");

            if (orderNumberString != null && !orderNumberString.isEmpty() &&
                orderLineNumberString != null && !orderLineNumberString.isEmpty()) {
                try {
                    int orderNumber = Integer.parseInt(orderNumberString);
                    int orderLineNumber = Integer.parseInt(orderLineNumberString);

                    OrderDetails orderDetails = orderDAO.getOrderDetails(orderNumber, orderLineNumber);
                    if (orderDetails != null) {
                        JPanel panel = new JPanel(new GridLayout(0, 2));

                        // Create and populate the product dropdown
                        JComboBox<String> productDropdown = new JComboBox<>();
                        Map<String, String> productNamesToCodes = productDAO.getProductNamesToCodes(); // Use the new method to get names and codes
                        for (String productName : productNamesToCodes.keySet()) {
                            productDropdown.addItem(productName);
                        }
                        String selectedProductName = productDAO.getProductNameByCode(orderDetails.getProductCode());
                        productDropdown.setSelectedItem(selectedProductName);

                        JTextField productCodeField = new JTextField(orderDetails.getProductCode(), 10);
                        productCodeField.setEditable(false);
                        productCodeField.setFocusable(false);
                        productCodeField.setBackground(new Color(240, 240, 240));

                        JTextField orderLineNumberField = new JTextField(String.valueOf(orderDetails.getOrderLineNr()), 10);
                        JTextField quantityOrderedField = new JTextField(String.valueOf(orderDetails.getQuantityOrdered()), 10);

                        JTextField quantityInStockField = new JTextField("", 10);
                        JTextField buyPriceField = new JTextField("", 10);
                        JTextField msrpField = new JTextField("", 10);

                        // Add an ActionListener to the productDropdown to update the product code
                        productDropdown.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String selectedProductName = (String) productDropdown.getSelectedItem();
                                String productCode = productNamesToCodes.get(selectedProductName);
                                productCodeField.setText(productCode);
                            }
                        });

                        if (productNamesToCodes != null && !productNamesToCodes.isEmpty()) {
                            selectedProductName = (String) productDropdown.getSelectedItem();
                            String productCode = productNamesToCodes.get(selectedProductName);
                            Map<String, Object> specificProductDetails = productDAO.getProductDetailsByCode(productCode);
                            quantityInStockField.setText(String.valueOf(specificProductDetails.get("quantityInStock")));
                            buyPriceField.setText(specificProductDetails.get("buyPrice").toString());
                            msrpField.setText(specificProductDetails.get("MSRP").toString());
                        }

                        // Add components to the panel
                        panel.add(new JLabel("Product Name:"));
                        panel.add(productDropdown);
                        panel.add(new JLabel("Product Code:"));
                        panel.add(productCodeField);
                        panel.add(new JLabel("Order Line Number:"));
                        panel.add(orderLineNumberField);
                        panel.add(new JLabel("Quantity Ordered:"));
                        panel.add(quantityOrderedField);
                        panel.add(new JLabel("Quantity in Stock:"));
                        panel.add(quantityInStockField);
                        panel.add(new JLabel("Buy Price:"));
                        panel.add(buyPriceField);
                        panel.add(new JLabel("MSRP:"));
                        panel.add(msrpField);

                        int result = JOptionPane.showConfirmDialog(null, panel, "Update Order Details", JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            // Update logic here, if necessary
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Order details not found for the given Order Number and Order Line Number.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input format. Please enter numeric values.");
                }
            }
        }
    }


}
