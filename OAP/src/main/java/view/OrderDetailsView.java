package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.OrderDAO;
import model.ProductDAO;
import database.DataBaseConnection;
import model.Order;
import view.OrderView.UpdateButtonListener;
import model.OrderDetails;
import model.Products; // Adjust the package name as necessary

public class OrderDetailsView extends MainView {
	private static final long serialVersionUID = 1L;
    private JLabel totalLabel; // Label for displaying the total
    private JTextField orderNumberInput;
    private JButton calculateButton;
	private JTextField quantityInStockField;
	private JTextField buyPriceField;
	private JTextField msrpField;
	private JTextField textField;
	private DefaultTableModel tableModel;
	private OrderDAO orderDAO = new OrderDAO();
	private JComboBox<String> productNameDropdown;
	private JComboBox<String> productCodeDropdown;
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
        initializeUI();
		setupProductDropdowns(); // Now setup the product dropdowns
        fetchAndDisplayOrderDetails();
        setVisible(true);
    }
	private void setupProductDropdowns() {
		productNameDropdown = new JComboBox<>();
		productCodeDropdown = new JComboBox<>();

		// Use the 'products' field that's already populated in the constructor
		Map<String, String> products = productHandler.getProducts(); // Fetch products

		for (String productName : products.keySet()) {
			productNameDropdown.addItem(productName);
			productCodeDropdown.addItem(products.get(productName));
		}

		// Make the productCodeDropdown non-editable and disable user input
		productCodeDropdown.setEditable(false);
		productCodeDropdown.setFocusable(false); // Add this line
		((JTextField) productCodeDropdown.getEditor().getEditorComponent()).setDisabledTextColor(Color.BLACK);

		productCodeDropdown.addActionListener(e -> {
			String selectedCode = (String) productCodeDropdown.getSelectedItem();
			String productName = findProductNameByCode(selectedCode); // Implement this method
			if (productName != null) {
				productNameDropdown.setSelectedItem(productName);
				// Fetch and display product details
				Map<String, Object> productDetails = productHandler.getProductDetailsByName(productName);
				if (productDetails != null && !productDetails.isEmpty()) {
					quantityInStockField.setText(productDetails.get("quantityInStock").toString());
					buyPriceField.setText(productDetails.get("buyPrice").toString());
					msrpField.setText(productDetails.get("MSRP").toString());
				} else {
					// Clear the fields or show a message if details are not found
					quantityInStockField.setText("");
					buyPriceField.setText("");
					msrpField.setText("");
				}
			}
		});
	}
	private String findProductNameByCode(String code) {
		return productHandler.getProductNameByCode(code); // Use the new method from ProductHandler
	}
	
    private void initializeUI() {
        setTitle("Order Details");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
		//setupControlPanel();

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
    
	/*private void setupControlPanel() {
		JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
		controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
		controlPanel.setBackground(new Color(90, 23, 139));
		
		JButton editButton = createButton("Edit", new UpdateButtonListener(orderHandler, productHandler));
		controlPanel.add(editButton);
		
		JPanel panelHolder = new JPanel(new BorderLayout());
		panelHolder.add(controlPanel, BorderLayout.SOUTH);
		this.add(panelHolder, BorderLayout.SOUTH);

	}*/
	
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
		
	//	JPanel panelHolder = new JPanel(new BorderLayout());
		//panelHolder.add(controlPanel, BorderLayout.SOUTH);
	//	this.add(panelHolder, BorderLayout.SOUTH);

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
    
    
    
    
    
    public class UpdateButtonListener implements ActionListener {

		public UpdateButtonListener(OrderDAO orderHandler, ProductDAO productHandler) {
			// TODO Auto-generated constructor stub
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			
		}
    
    }
    
    
    

}
