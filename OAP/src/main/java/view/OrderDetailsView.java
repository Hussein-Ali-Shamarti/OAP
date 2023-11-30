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
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.EmptyBorder;

import model.OrderDAO;
import model.ProductDAO;
import database.DataBaseConnection;
import model.OrderDetails;
import model.Products;
import view.OrderView.UpdateButtonListener;

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
	private OrderDAO orderDAO = new OrderDAO();
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

    private DefaultTableModel orderDetailsTableModel;
    private JTable orderDetailsTable;

    public OrderDetailsView() {
		super();
        this.orderDAO = new OrderDAO(); // Initialize OrderHandler first
		this.productHandler = new ProductDAO();
		this.products = productHandler.getProducts(); // Initialize products
        productHandler = new ProductDAO();
        
        orderNumberInput = new JTextField(10);
        orderLineNumberField = new JTextField(10);
        quantityOrderedField = new JTextField(10);
        productCodeField = new JTextField(10);
        
        initializeUI();
        setupSearchComponents();
        fetchAndDisplayOrderDetails();
        setVisible(true);
                
        JPanel dummyPanel = new JPanel();
        add(dummyPanel);
        dummyPanel.requestFocusInWindow();
		UpdateButtonListener updateButtonListener = new UpdateButtonListener(orderDAO, productHandler);

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
	
    private void initializeUI() {
        setTitle("Order Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
		//setupControlPanel();

        
        setupTitlePanel();
        setupSearchComponents();
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
    
    private void setupSearchComponents() {
        // Create a panel for search components and calculate order
        JPanel searchAndCalculatePanel = new JPanel(new BorderLayout());

        // Create a sub-panel for search components
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create the search field
        searchField = new JTextField(20);
        searchPanel.add(searchField);

        // Create the search button
        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchOrderDetails();
            }
        });
        searchPanel.add(searchButton);

        // Add the search panel to the left side
        searchAndCalculatePanel.add(searchPanel, BorderLayout.WEST);

        // Create a sub-panel for the "Calculate Order" field and button
        JPanel calculateOrderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // Initialize your JTextField with a "placeholder" text
        orderNumberInput = new JTextField("Enter Order Number To Calculate", 17);
        // Set the foreground color to a lighter shade to mimic placeholder style
        orderNumberInput.setForeground(Color.GRAY);

        // Add focus listener to clear the "placeholder" when the field gains focus
        orderNumberInput.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                JTextField source = (JTextField) e.getComponent();
                source.setText("");
                orderNumberInput.setFont(new Font(orderNumberInput.getFont().getName(), Font.PLAIN, 12));
                source.setForeground(Color.BLACK); // Reset to default color when focused
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

        calculateButton = new JButton("Calculate Total Order");
        calculateButton.addActionListener(e -> calculateAndDisplayTotalForOrder());

        // Add the JTextField and Calculate button to the sub-panel
        calculateOrderPanel.add(orderNumberInput);
        calculateOrderPanel.add(calculateButton);

        // Add the "Calculate Order" sub-panel to the right side
        searchAndCalculatePanel.add(calculateOrderPanel, BorderLayout.EAST);

        // Add the search and calculate panel to the frame's NORTH region
        getContentPane().add(searchAndCalculatePanel, BorderLayout.NORTH);

        // Set background and other properties
        searchAndCalculatePanel.setBackground(new Color(84, 11, 131));
        searchPanel.setBackground(new Color(84, 11, 131));
        calculateOrderPanel.setBackground(new Color(84, 11, 131));
    }
    
    private void searchOrderDetails() {
        String searchCriteria = searchField.getText();
        // Call the search method from OrderDAO and display the results
        List<OrderDetails> searchResults = orderDAO.searchOrderDetails(searchCriteria);

        // Assuming you have a method to update the table with search results, update it here
        updateOrderDetailsTable(searchResults);
    }

    // Method to update the order details table with search results
    private void updateOrderDetailsTable(List<OrderDetails> searchResults) {
        DefaultTableModel model = (DefaultTableModel) orderDetailsTable.getModel();
        model.setRowCount(0); // Clear the existing rows

        for (OrderDetails orderDetail : searchResults) {
            // Assuming you have appropriate getters in the OrderDetails class
            model.addRow(new Object[]{
                    orderDetail.getOrderNumber(),
                    orderDetail.getProductCode(),
                    orderDetail.getQuantityOrdered(),
                    orderDetail.getPriceEach(),
                    orderDetail.getOrderLineNumber()
            });
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

		
		JButton editButton = createButton("Edit", new UpdateButtonListener(orderDAO, productHandler));
		controlPanel.add(editButton);
		
		//JPanel panelHolder = new JPanel(new BorderLayout());
		//panelHolder.add(controlPanel, BorderLayout.SOUTH);
		//this.add(panelHolder, BorderLayout.SOUTH);

        controlPanel.setBackground(new Color(84, 11, 131)); // Ensure this is the color you want for the background
        controlPanel.add(totalLabel, BorderLayout.CENTER);
        controlPanel.add(editButton, BorderLayout.SOUTH);
        add(controlPanel, BorderLayout.SOUTH);
        
    }
   
    private JPanel controlPanel = new JPanel(new BorderLayout());

    
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
            String orderDetailsSql = "SELECT * FROM orderdetails ";
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

                    // Check if getOrderDetails method exists and is implemented correctly
                    OrderDetails orderDetails = orderDAO.getOrderDetails(orderNumber, orderLineNumber);
                    if (orderDetails != null) {
                        JPanel panel = new JPanel(new GridLayout(0, 2));
                        orderLineNumberField.setText(String.valueOf(orderDetails.getOrderLineNumber()));
                        JComboBox<String> productDropdown = new JComboBox<>();
                        List<String> productNames = productDAO.getAllProductNames(); // Assuming this method exists
                        for (String productName : productNames) {
                            productDropdown.addItem(productName);
                        }
                        productDropdown.setSelectedItem(productDAO.getProductNameByCode(orderDetails.getProductCode()));

                        JTextField productCodeField = new JTextField(orderDetails.getProductCode(), 10);
                        JTextField orderLineNumberField = new JTextField(String.valueOf(orderDetails.getOrderLineNumber()), 10);
                        JTextField quantityOrderedField = new JTextField(String.valueOf(orderDetails.getQuantityOrdered()), 10);

                        productCodeField.setEditable(false);
                        productCodeField.setFocusable(false);
                        productCodeField.setBackground(new Color(240, 240, 240)); // Light grey background color

                        JComboBox<String> productNameDropdown = new JComboBox<>();
            			JComboBox<String> productCodeDropdown = new JComboBox<>();

                        
                        String productName = productDAO.getProductNameByCode(orderDetails.getProductCode()); // Assuming this method exists
                        Map<String, Object> productDetails = productDAO.getProductDetailsByName(productName);
						for (String productName1 : products.keySet()) {
							productNameDropdown.addItem(productName1);
							productCodeDropdown.addItem(products.get(productName));

						}
						
                        JTextField quantityInStockField;
                        JTextField buyPriceField;
                        JTextField msrpField;
                        if (productDetails != null && !productDetails.isEmpty()) {
                            quantityInStockField = new JTextField(String.valueOf(productDetails.get("quantityInStock")), 10);
                            buyPriceField = new JTextField(productDetails.get("buyPrice").toString(), 10);
                            msrpField = new JTextField(productDetails.get("MSRP").toString(), 10);
                        } else {
                            quantityInStockField = new JTextField("", 10);
                            buyPriceField = new JTextField("", 10);
                            msrpField = new JTextField("", 10);
                        }

                        // Add components to the panel
                        panel.add(new JLabel("Product Name:"));
						panel.add(productNameDropdown);
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
