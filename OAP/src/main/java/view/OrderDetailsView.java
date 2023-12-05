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
import model.OrderDAO;
import model.ProductDAO;
import database.DataBaseConnection;
import model.OrderDetails;

/**
 * A graphical user interface for displaying and managing order details. This class extends JFrame
 * and provides a user interface for viewing order details, searching for orders, and interacting
 * with order-related information.
 * orders may also contain order details, which are not implemented in this version.
 * 
 * @author 7094
 */

public class OrderDetailsView extends JFrame {
	/**
	 * The serial version UID for object serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The label for displaying the total.
	 */
	private JLabel totalLabel;

	/**
	 * The input field for entering an order number.
	 */
	private JTextField orderNumberInput;

	/**
	 * The field for displaying quantity in stock.
	 */
	private JTextField quantityInStockField;

	/**
	 * The field for displaying the buy price.
	 */
	private JTextField buyPriceField;

	/**
	 * The field for displaying MSRP (Manufacturer's Suggested Retail Price).
	 */
	private JTextField msrpField;

	/**
	 * The field for entering an order line number.
	 */
	private JTextField orderLineNumberField;

	/**
	 * The field for entering a product code.
	 */
	private JTextField productCodeField;

	/**
	 * The field for entering the quantity ordered.
	 */
	private JTextField quantityOrderedField;

	/**
	 * The table model used for managing table data.
	 */
	private DefaultTableModel tableModel;

	/**
	 * The OrderDAO responsible for handling order-related data access.
	 */
	private OrderDAO orderDAO;

	/**
	 * The JComboBox for selecting product names.
	 */
	private JComboBox<String> productNameDropdown;

	/**
	 * The JComboBox for selecting product codes.
	 */
	private JComboBox<String> productCodeDropdown;

	/**
	 * The JTextField for entering search criteria.
	 */
	private JTextField searchField;

	/**
	 * The JButton for initiating a search.
	 */
	private JButton searchButton;

	/**
	 * The ProductDAO responsible for handling product-related data access.
	 */
	private ProductDAO productHandler;

	/**
	 * A map containing product names and their corresponding codes.
	 */
	private Map<String, String> products;

	/**
	 * The column names for the order details table.
	 */
	private static final String[] COLUMN_NAMES = { "Order Number", "Product Code", "Quantity Ordered", "Price Each",
	    "Order Line Number" };

	/**
	 * The table component for displaying order details.
	 */
	private JTable orderDetailsTable;

	/**
	 * Constructs an instance of the OrderDetailsView class.
	 * This constructor initializes the orderDAO and productHandler, fetches products data,
	 * and sets up the user interface for displaying order details.
	 * It also populates the UI with order details data and initializes event listeners.
	 */	
	
	public OrderDetailsView() {
		super();
		this.orderDAO = new OrderDAO(); 
		this.productHandler = new ProductDAO();
		this.setProducts(productHandler.getProducts());
		this.revalidate();
		this.repaint();

		productHandler = new ProductDAO();

		orderNumberInput = new JTextField(10);
		setOrderLineNumberField(new JTextField(10));
		setQuantityOrderedField(new JTextField(10));
		setProductCodeField(new JTextField(10));

		initializeUI();
		fetchAndDisplayOrderDetails();
		setVisible(true);

		JPanel dummyPanel = new JPanel();
		add(dummyPanel);
		dummyPanel.requestFocusInWindow();
		new UpdateButtonListener(orderDAO, productHandler);

	}

	/**
	 * Initializes the user interface for the OrderDetailsView.
	 * This method sets the title, size, and location of the frame.
	 * It also sets up the control panel, top panel, and order details table.
	 */
	
	private void initializeUI() {
		setTitle("Order Details");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(800, 600);
		setLocationRelativeTo(null);
		setupControlPanel(); 

		setupTopPanel();
		setupTable();
		add(new JScrollPane(orderDetailsTable), BorderLayout.CENTER);
	}

	/**
	 * Sets up the product name and product code dropdowns with data from the productHandler.
	 * It populates the dropdowns with product names and their corresponding codes.
	 * Also, configures the product code dropdown to be non-editable and sets its text color.
	 * Adds an ActionListener to the product code dropdown to update product details fields
	 * when a product code is selected.
	 */
	
	public void setupProductDropdowns() {
		productNameDropdown = new JComboBox<>();
		productCodeDropdown = new JComboBox<>();

		Map<String, String> products = productHandler.getProducts(); 
		for (String productName : products.keySet()) {
			productNameDropdown.addItem(productName);
			productCodeDropdown.addItem(products.get(productName));
		}

		productCodeDropdown.setEditable(false);
		productCodeDropdown.setFocusable(false);
		((JTextField) productCodeDropdown.getEditor().getEditorComponent()).setDisabledTextColor(Color.BLACK);

		productCodeDropdown.addActionListener(e -> {
			String selectedCode = (String) productCodeDropdown.getSelectedItem();
			String productName = productHandler.getProductNameByCode(selectedCode); 
			if (productName != null) {
				productNameDropdown.setSelectedItem(productName);
				Map<String, Object> productDetails = productHandler.getProductDetailsByName(productName); 
				updateProductDetailsFields(productDetails);
			}
		});

	}

	/**
	 * Updates the quantity in stock, buy price, and MSRP fields with the provided product details.
	 * If the product details map is null or empty, clears the fields.
	 *
	 * @param productDetails A map containing product details with keys "quantityInStock," "buyPrice," and "MSRP."
	 */
	
	public void updateProductDetailsFields(Map<String, Object> productDetails) {
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

	/**
	 * Finds and returns the product code associated with the given product name.
	 *
	 * @param code The product name for which to find the product code.
	 * @return The product code, or null if not found.
	 */
	
	public String findProductCodeByName(String code) {
		return productHandler.getProductNameByCode(code); 
	}

	/**
	 * Searches for order details matching the given search criteria and updates the display table with the results.
	 */

	
	public void searchOrderDetails() {
		String searchCriteria = searchField.getText();
		List<OrderDetails> searchResults = orderDAO.searchOrderDetails(searchCriteria);

		if (searchResults == null || searchResults.isEmpty()) {
			System.out.println("No results found for: " + searchCriteria);
		} else {
			updateOrderDetailsTable(searchResults);

			System.out.println("Found " + searchResults.size() + " results for: " + searchCriteria);
		}

	}

	/**
	 * Updates the order details table with the provided list of order details.
	 * 
	 * @param searchResults A list of OrderDetails objects to populate the table with.
	 */
	
	public void updateOrderDetailsTable(List<OrderDetails> searchResults) {
		tableModel = (DefaultTableModel) orderDetailsTable.getModel();
		tableModel.setRowCount(0);
		for (OrderDetails orderDetail : searchResults) {
			Object[] rowData = { orderDetail.getOrderNumber(), orderDetail.getProductCode(),
					orderDetail.getQuantityOrdered(), orderDetail.getPriceEach(), orderDetail.getOrderLineNr() };
			System.out.println(orderDetail.getOrderLineNr());
			tableModel.addRow(rowData);
		}

		for (int i = 0; i < tableModel.getRowCount(); i++) {
			for (int j = 0; j < tableModel.getColumnCount(); j++) {
				System.out.print(tableModel.getValueAt(i, j) + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Sets up the top panel of the Order Details view, which includes the title label and search functionality.
	 * 
	 * The top panel displays the title "Order Details" and allows users to search for specific order details.
	 */
	
	public void setupTopPanel() {
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(new Color(84, 11, 131));

		JLabel titleLabel = new JLabel("Order Details");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setForeground(Color.WHITE);
		topPanel.add(titleLabel, BorderLayout.NORTH);

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		searchField = new JTextField(20);
		searchButton = new JButton("Search");
		searchButton.addActionListener(e -> searchOrderDetails());
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		searchPanel.setBackground(new Color(84, 11, 131));
		topPanel.add(searchPanel, BorderLayout.CENTER);

		getContentPane().add(topPanel, BorderLayout.NORTH);
	}
	
	/**
	 * Sets up the control panel of the Order Details view, which includes buttons for editing and deleting order details.
	 * 
	 * The control panel also provides input fields for specifying the order number.
	 */
	
	public void setupControlPanel() {
		JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

		JButton editButton = createButton("Edit", new UpdateButtonListener(orderDAO, productHandler));
		JButton deleteButton = new JButton("Delete");
		deleteButton.addActionListener(new DeleteButtonListener());

		orderNumberInput = new JTextField("Enter Order Number To Calculate", 18);
		orderNumberInput.setForeground(Color.GRAY);
		orderNumberInput.addFocusListener(new FocusAdapter() {
			
			/**
			 * Called when the focus is gained by the orderNumberInput field.
			 * 
			 * This method removes the placeholder text and changes the text color to black when the input field is focused.
			 * 
			 * @param e The FocusEvent indicating the focus gained event.
			 */
			
			@Override
			public void focusGained(FocusEvent e) {
				JTextField source = (JTextField) e.getComponent();
				if ("Enter Order Number To Calculate".equals(source.getText())) {
					source.setText("");
					source.setForeground(Color.BLACK);
				}
			}
			
			/**
			 * Called when the focus is lost from the orderNumberInput field.
			 * 
			 * This method restores the default placeholder text and color if the input field is empty.
			 * 
			 * @param e The FocusEvent indicating the focus lost event.
			 */

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

		totalLabel = new JLabel("Total: $0.00");
		totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
		totalLabel.setForeground(Color.WHITE);

		controlPanel.add(editButton);
		controlPanel.add(deleteButton);
		controlPanel.add(orderNumberInput);
		controlPanel.add(calculateButton);
		controlPanel.add(totalLabel);

		controlPanel.setBackground(new Color(84, 11, 131));

		add(controlPanel, BorderLayout.SOUTH);
	}

	/**
	 * Creates a custom-styled JButton with the specified text and ActionListener.
	 *
	 * @param text     The text to display on the button.
	 * @param listener The ActionListener to be triggered when the button is clicked.
	 * @return A customized JButton instance.
	 */
	
	public JButton createButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.setForeground(Color.BLACK);
		button.setBackground(new Color(84, 11, 131));
		button.setFocusPainted(false);
		button.addActionListener(listener);
		return button;
	}
	
	/**
	 * Sets up the table for displaying order details.
	 * It configures the table model, column names, and cell editability.
	 */

	public void setupTable() {
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

	/**
	 * ActionListener implementation for the "Update" button in the Order Details view.
	 * This listener is used to handle updating order details.
	 */
	
	public class UpdateButtonListener implements ActionListener {
		
		 /**
	     * The OrderDAO instance for accessing order data.
	     */
		
		private OrderDAO orderDAO;
		
		/**
	     * The ProductDAO instance for accessing product data.
	     */
		
		private ProductDAO productDAO;
		
		 /**
	     * Constructs a new UpdateButtonListener with the provided OrderDAO and ProductDAO instances.
	     *
	     * @param orderDAO    The OrderDAO instance for accessing order data.
	     * @param productDAO  The ProductDAO instance for accessing product data.
	     */

		public UpdateButtonListener(OrderDAO orderDAO, ProductDAO productDAO) {
			this.orderDAO = orderDAO;
			this.productDAO = productDAO;
		}

		 /**
	     * Called when the "Update" button is clicked.
	     *
	     * @param e The ActionEvent associated with the button click.
	     */
		
		@Override
		public void actionPerformed(ActionEvent e) {
			String orderNumberString = JOptionPane.showInputDialog("Enter Order Number to update:");
			String orderLineNumberString = JOptionPane.showInputDialog("Enter Order Line Number:");

			if (orderNumberString != null && !orderNumberString.isEmpty() && orderLineNumberString != null
					&& !orderLineNumberString.isEmpty()) {
				try {
					int orderNumber = Integer.parseInt(orderNumberString);
					int orderLineNumber = Integer.parseInt(orderLineNumberString);

					OrderDetails orderDetails = orderDAO.getOrderDetails(orderNumber, orderLineNumber);
					if (orderDetails != null) {
						JPanel panel = new JPanel(new GridLayout(0, 2));

						JComboBox<String> productDropdown = new JComboBox<>();
						Map<String, String> productNamesToCodes = productDAO.getProductNamesToCodes(); 
						for (String productName : productNamesToCodes.keySet()) {
							productDropdown.addItem(productName);
						}
						String selectedProductName = productDAO.getProductNameByCode(orderDetails.getProductCode());
						productDropdown.setSelectedItem(selectedProductName);

						JTextField productCodeField = new JTextField(orderDetails.getProductCode(), 10);
						productCodeField.setEditable(false);
						productCodeField.setFocusable(false);
						productCodeField.setBackground(new Color(240, 240, 240));

						JTextField orderLineNumberField = new JTextField(String.valueOf(orderDetails.getOrderLineNr()),
								10);
						JTextField quantityOrderedField = new JTextField(
								String.valueOf(orderDetails.getQuantityOrdered()), 10);

						JTextField quantityInStockField = new JTextField("", 10);
						JTextField buyPriceField = new JTextField("", 10);
						JTextField msrpField = new JTextField("", 10);

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
							Map<String, Object> specificProductDetails = productDAO
									.getProductDetailsByCode(productCode);
							quantityInStockField.setText(String.valueOf(specificProductDetails.get("quantityInStock")));
							buyPriceField.setText(specificProductDetails.get("buyPrice").toString());
							msrpField.setText(specificProductDetails.get("MSRP").toString());
						}

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

						int result = JOptionPane.showConfirmDialog(null, panel, "Update Order Details",
								JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
						}
					} else {
						JOptionPane.showMessageDialog(null,
								"Order details not found for the given Order Number and Order Line Number.");
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid input format. Please enter numeric values.");
				}
			}
		}
	}
	
	/**
	 * ActionListener implementation for the "Delete" button in the Order Details view.
	 * This listener is used to handle deleting order details.
	 */
	
	public class DeleteButtonListener implements ActionListener {
		
	    /**
	     * Called when the "Delete" button is clicked.
	     *
	     * @param e The ActionEvent associated with the button click.
	     */

		
		@Override
		public void actionPerformed(ActionEvent e) {
			JPanel panel = new JPanel(new GridLayout(0, 1));
			JTextField orderNumberField = new JTextField(5);
			JTextField orderLineNumberField = new JTextField(5);

			panel.add(new JLabel("Order Number:"));
			panel.add(orderNumberField);
			panel.add(new JLabel("Order Line Number:"));
			panel.add(orderLineNumberField);

			int result = JOptionPane.showConfirmDialog(OrderDetailsView.this, panel,
					"Enter Order Number and Order Line Number", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.PLAIN_MESSAGE);

			if (result == JOptionPane.OK_OPTION) {
				try {
					int orderNumber = Integer.parseInt(orderNumberField.getText().trim());
					int orderLine = Integer.parseInt(orderLineNumberField.getText().trim());

					int confirm = JOptionPane.showConfirmDialog(
							OrderDetailsView.this, "Are you sure you want to delete order number " + orderNumber
									+ " and order line number " + orderLine + "?",
							"Confirm Deletion", JOptionPane.YES_NO_OPTION);

					if (confirm == JOptionPane.YES_OPTION) {
						boolean success = orderDAO.deleteOrderDetail(orderNumber, orderLine);
						if (success) {
							JOptionPane.showMessageDialog(OrderDetailsView.this, "Order detail deleted successfully.");
						} else {
							JOptionPane.showMessageDialog(OrderDetailsView.this, "Error deleting order detail.");
						}
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(OrderDetailsView.this,
							"Please enter valid numbers for Order Number and Order Line Number.");
				}
			}
		}
	}
	
	/**
	 * Calculates and displays the total order amount for the specified order number.
	 * This method retrieves order details from the table, calculates the total, and updates the total label.
	 */

	public void calculateAndDisplayTotalForOrder() {
		String orderNumberStr = orderNumberInput.getText().trim();
		if (orderNumberStr.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Please enter an order number.");
			return;
		}

		try {
			int orderNumber = Integer.parseInt(orderNumberStr);
			if (!isOrderNumberPresentInTable(orderNumber)) {
				fetchSpecificOrderDetails(orderNumber);
			}
			BigDecimal total = calculateTotalForOrderNumber(orderNumber);
			updateOrderTotal(total);
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Invalid order number format.");
		}
	}

	/**
	 * Calculates the total amount for the specified order number by iterating through the table rows,
	 * multiplying the quantity by the price for each item, and summing up the results.
	 *
	 * @param orderNumber The order number for which the total amount should be calculated.
	 * @return The calculated total amount for the specified order number, rounded to 2 decimal places.
	 */
	
	public BigDecimal calculateTotalForOrderNumber(int orderNumber) {
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
	
	/**
	 * Updates the total label in the user interface with the provided total amount.
	 *
	 * @param total The total amount to display in the label.
	 */

	public void updateOrderTotal(BigDecimal total) {
		totalLabel.setText("Total: $" + total.toPlainString());
	}

	/**
	 * Checks if the given order number is present in the table's data.
	 *
	 * @param orderNumber The order number to check for presence in the table.
	 * @return true if the order number is found in the table, false otherwise.
	 */
	
	public boolean isOrderNumberPresentInTable(int orderNumber) {
		for (int i = 0; i < tableModel.getRowCount(); i++) {
			if (((Integer) tableModel.getValueAt(i, 0)).intValue() == orderNumber) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Fetches specific order details from the database for the given order number
	 * and populates them in the tableModel to display in the table.
	 *
	 * @param orderNumber The order number for which to fetch details.
	 */
	
	public void fetchSpecificOrderDetails(int orderNumber) {
		try (Connection conn = DataBaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orderdetails WHERE orderNumber = ?")) {

			pstmt.setInt(1, orderNumber);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					Object[] row = { rs.getInt("orderNumber"), rs.getString("productCode"),
							rs.getInt("quantityOrdered"), rs.getDouble("priceEach"), rs.getInt("orderLineNumber") };
					tableModel.addRow(row);
				}
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error fetching specific order details: " + e.getMessage(),
					"Database Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Fetches all order details from the database and populates them in the tableModel
	 * to display in the table.
	 */

	public void fetchAndDisplayOrderDetails() {
		try (Connection conn = DataBaseConnection.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM orderdetails");
				ResultSet rs = pstmt.executeQuery()) {

			tableModel = (DefaultTableModel) orderDetailsTable.getModel();
			tableModel.setRowCount(0);

			while (rs.next()) {
				Object[] row = { rs.getInt("orderNumber"), rs.getString("productCode"), rs.getInt("quantityOrdered"),
						rs.getDouble("priceEach"), rs.getInt("orderLineNumber") };
				tableModel.addRow(row);
			}
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error fetching order details: " + e.getMessage(), "Database Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	public JTextField getOrderLineNumberField() {
		return orderLineNumberField;
	}

	public void setOrderLineNumberField(JTextField orderLineNumberField) {
		this.orderLineNumberField = orderLineNumberField;
	}

	public JTextField getProductCodeField() {
		return productCodeField;
	}

	public void setProductCodeField(JTextField productCodeField) {
		this.productCodeField = productCodeField;
	}

	public JTextField getQuantityOrderedField() {
		return quantityOrderedField;
	}

	public void setQuantityOrderedField(JTextField quantityOrderedField) {
		this.quantityOrderedField = quantityOrderedField;
	}

	public Map<String, String> getProducts() {
		return products;
	}

	public void setProducts(Map<String, String> products) {
		this.products = products;
	}

}
