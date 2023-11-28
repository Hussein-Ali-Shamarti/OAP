package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import model.Order;
import model.OrderDetails;
import model.OrderDAO;
import model.ProductDAO;
import model.Products;


public class OrderView extends MainView {
<<<<<<< HEAD
    private JTextField textField;
    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private OrderDAO orderHandler = new OrderDAO();
    private JComboBox<String> productNameDropdown;
    private JComboBox<String> productCodeDropdown;
    private ProductDAO productHandler;
    private Map<String, String> products; // Declare products here
    private JTextField quantityInStockField;
    private JTextField buyPriceField;
    private JTextField msrpField;
    
 

    public OrderView() {
        super();
        this.orderHandler = new OrderDAO();        // Initialize OrderHandler first
        this.productHandler = new ProductDAO();    // Initialize ProductHandler
        this.products = productHandler.getProducts();  // Initialize products
        this.quantityInStockField = new JTextField(10);
        this.buyPriceField = new JTextField(10);
        this.msrpField = new JTextField(10);
=======
	private JTextField textField;
	private static final long serialVersionUID = 1L;
	private DefaultTableModel tableModel;
	private JTable table;
	private OrderHandler orderHandler = new OrderHandler();
	private JComboBox<String> productNameDropdown;
	private JComboBox<String> productCodeDropdown;
	private ProductHandler productHandler;
	private Map<String, String> products; // Declare products here
	private JTextField quantityInStockField;
	private JTextField buyPriceField;
	private JTextField msrpField;
	private JTextField productCodeField = new JTextField(10);
	private JTextField quantityOrderedField = new JTextField(10);
	private JTextField orderLineNumberField = new JTextField(10);

	public OrderView() {
		super();
		this.orderHandler = new OrderHandler(); // Initialize OrderHandler first
		this.productHandler = new ProductHandler(); // Initialize ProductHandler
		this.products = productHandler.getProducts(); // Initialize products
		this.quantityInStockField = new JTextField(10);
		this.buyPriceField = new JTextField(10);
		this.msrpField = new JTextField(10);
>>>>>>> 299ad0bce36240246f9f22d4c9849579449cffe8

		setLayout(new BorderLayout());
		initializeUI();
		setupProductDropdowns(); // Now setup the product dropdowns
		fetchAndDisplayOrders();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);
		pack(); // Adjusts the frame to fit the components
		setVisible(true); // Make sure the frame is visible
		UpdateButtonListener updateButtonListener = new UpdateButtonListener(orderHandler, productHandler);

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
		JPanel titlePanel = new JPanel();
		titlePanel.setBackground(new Color(84, 11, 131));
		JLabel titleLabel = new JLabel("Order Management");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
		titleLabel.setHorizontalAlignment(JLabel.CENTER);
		titleLabel.setForeground(Color.WHITE);
		titlePanel.add(titleLabel);
		setupControlPanel();
		setupTable();
		add(new JScrollPane(table), BorderLayout.CENTER);
		add(titlePanel, BorderLayout.NORTH);

		// Set frame properties
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);

	}

	private void setupTable() {
		String[] columnNames = { "Order Number", "Order Date", "Required Date", "Shipped Date", "Status", "Comments",
				"Customer Number" };
		tableModel = new DefaultTableModel(null, columnNames) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(tableModel);
	}

	private void setupControlPanel() {
		// Control panel for the Search, Add, Edit, Delete buttons
		JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
		controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
		controlPanel.setBackground(new Color(90, 23, 139));
		JButton searchButton = createButton("Search", new SearchButtonListener());
		JButton addButton = createButton("Add", new AddButtonListener());
		JButton editButton = createButton("Edit", new UpdateButtonListener(orderHandler, productHandler));
		JButton deleteButton = createButton("Delete", new DeleteButtonListener());
		JButton saveOrderButton = createButton("Save to File", new SaveOrderButtonListener());
		JButton orderDetailsButton = createButton("Order Details", new OrderDetailsButtonListener());
		// In your OrderView class constructor or appropriate method

		controlPanel.add(orderDetailsButton); // Add the button to the control panel
		controlPanel.add(searchButton);
		controlPanel.add(addButton);
		controlPanel.add(editButton);
		controlPanel.add(deleteButton);
		controlPanel.add(saveOrderButton); 
		controlPanel.add(orderDetailsButton); // Add the button to the control panel

		// Control panel for the Check Status and Check Payment Status buttons
		JPanel statusPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		statusPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
		statusPanel.setBackground(new Color(100, 25, 150));
		JButton checkStatusButton = createButton("Check Shipping Status", new CheckStatusButtonListener());
		JButton paymentButton = createButton("Check Payment Status", new PaymentButtonListener());
		statusPanel.add(checkStatusButton);
		statusPanel.add(paymentButton);
		// Main panel holder to hold both the status and control panels
		JPanel panelHolder = new JPanel(new BorderLayout());
		panelHolder.add(statusPanel, BorderLayout.NORTH);
		panelHolder.add(controlPanel, BorderLayout.SOUTH);
		this.add(panelHolder, BorderLayout.SOUTH);
	}

	private JButton createButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.setForeground(Color.BLACK);
		button.setBackground(new Color(84, 11, 131));
		button.setFocusPainted(false);
		button.addActionListener(listener);
		return button;
	}

	List<String[]> fetchAndDisplayOrders() {
	    List<String[]> orders = new ArrayList<>();
	    tableModel.setRowCount(0); // Clear the existing rows

	    try (Connection conn = database.DataBaseConnection.getConnection();
	         Statement statement = conn.createStatement()) {
	        String ordersSql = "SELECT o.OrderNumber, o.orderDate, o.requiredDate, o.shippedDate, o.status, o.comments, o.customerNumber "
	                         + "FROM orders o ";
	        ResultSet ordersResultSet = statement.executeQuery(ordersSql);
	        while (ordersResultSet.next()) {
	            String[] order = {
	                ordersResultSet.getString("OrderNumber"),
	                ordersResultSet.getDate("orderDate").toString(),
	                ordersResultSet.getDate("requiredDate").toString(),
	                ordersResultSet.getDate("shippedDate") != null ? ordersResultSet.getDate("shippedDate").toString() : "N/A",
	                ordersResultSet.getString("status"),
	                ordersResultSet.getString("comments"),
	                String.valueOf(ordersResultSet.getInt("customerNumber"))
	            };
	            tableModel.addRow(order); // Add row to the table model
	            orders.add(order); // Add to the list
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Error fetching order data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	    }
	    return orders;
	}

	private JPanel panel = new JPanel(new GridLayout(0, 2));

	private class AddButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			// Fields for customer details
			JTextField orderDateField = new JTextField(10);
			JTextField requiredDateField = new JTextField(10);
			JTextField shippedDateField = new JTextField(10);
			JTextField statusField = new JTextField(10);
			JTextField commentsField = new JTextField(10);
			JTextField customerNumberField = new JTextField(10);

			// Fields for product details, which must be class attributesorderLineNumber
			JTextField productCodeField = new JTextField(10);
			JTextField quantityOrderedField = new JTextField(10);
			JTextField orderLineNumberField = new JTextField(10);
			JTextField quantityInStockField = new JTextField(10);
			JTextField buyPriceField = new JTextField(10);
			JTextField msrpField = new JTextField(10);
			productCodeField.setEditable(false);
			productCodeField.setFocusable(false);
			productCodeField.setBackground(new Color(240, 240, 240)); // Light grey background color

			// Dropdowns for selecting product
			JComboBox<String> productNameDropdown = new JComboBox<>();
			JComboBox<String> productCodeDropdown = new JComboBox<>();

			// Panel for the form

			// Populate the dropdown with product data from the database using an instance
			// of ProductHandler
			Map<String, String> products = productHandler.getProducts(); // Use the instance
			for (String productName : products.keySet()) {
				productNameDropdown.addItem(productName);
				productCodeDropdown.addItem(products.get(productName));
			}

			productNameDropdown.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					String selectedProductName = (String) productNameDropdown.getSelectedItem();
					String productCode = products.get(selectedProductName);
					productCodeField.setText(productCode); // Update the productCodeField with the selected product code

					updateProductDetailsFields(selectedProductName);

					Map<String, Object> productDetails = productHandler.getProductDetailsByName(selectedProductName);
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
			});

			if (productNameDropdown.getItemCount() == 0) {
				for (String productName : products.keySet()) {
					productNameDropdown.addItem(productName);
				}
			}

			if (productCodeDropdown.getItemCount() == 0) {
				for (String productCode : products.values()) {
					productCodeDropdown.addItem(productCode);
				}
			}

			// Trigger the action listener to populate fields for the initially selected
			// product
			if (productNameDropdown.getItemCount() > 0) {
				productNameDropdown.setSelectedIndex(0);
				productNameDropdown.getActionListeners()[0]
						.actionPerformed(new ActionEvent(productNameDropdown, ActionEvent.ACTION_PERFORMED, null));
			}

			// Add components to the panel
			panel.add(new JLabel("Order Date (yyyy-MM-dd):"));
			panel.add(orderDateField);
			panel.add(new JLabel("Required Date:"));
			panel.add(requiredDateField);
			panel.add(new JLabel("Shipped Date:"));
			panel.add(shippedDateField);
			panel.add(new JLabel("Status:"));
			panel.add(statusField);
			panel.add(new JLabel("Comments:"));
			panel.add(commentsField);
			panel.add(new JLabel("Customer Number:"));
			panel.add(customerNumberField);
			panel.add(new JLabel("Product Name:"));
			panel.add(productNameDropdown);
			panel.add(new JLabel("Product Code:"));
			panel.add(productCodeField); // Use the JTextField for Product Code
			panel.add(new JLabel("Product Quantity:"));
			panel.add(quantityOrderedField);
			panel.add(new JLabel("Quantity in Stock:"));
			panel.add(quantityInStockField);
			panel.add(new JLabel("Buy Price:"));
			panel.add(buyPriceField);
			panel.add(new JLabel("MSRP:"));
			panel.add(msrpField);
			panel.add(new JLabel("orderLineNumber:"));
			panel.add(orderLineNumberField);
			
			JButton addMultipleProducts = createButton("Add More Products +", new AddMultipleProductsButtonListener());
			panel.add(addMultipleProducts);
			
			// Show confirm dialog with the form
			int result = JOptionPane.showConfirmDialog(null, panel, "Enter New Order Details",
					JOptionPane.OK_CANCEL_OPTION);
			if (result == JOptionPane.OK_OPTION) {
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Date orderDate = dateFormat.parse(orderDateField.getText());
					Date requiredDate = dateFormat.parse(requiredDateField.getText());
					Date shippedDate = dateFormat.parse(shippedDateField.getText());
					String status = statusField.getText();
					String comments = commentsField.getText();
					int customerNumber = Integer.parseInt(customerNumberField.getText());

					String productCode = productCodeField.getText();
					int quantityOrdered = Integer.parseInt(quantityOrderedField.getText());
					int orderLineNumber = Integer.parseInt(orderLineNumberField.getText());
					double buyPrice = Double.parseDouble(buyPriceField.getText());

					// Assume Order constructor takes these parameters
					Order order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
					OrderDetails orderDetails = new OrderDetails(quantityOrdered, buyPrice, productCode,
							orderLineNumber);
					boolean success = orderHandler.addOrder(order, orderDetails);
					if (success) {
						JOptionPane.showMessageDialog(OrderView.this, "Order added successfully!");
					} else {
						JOptionPane.showMessageDialog(OrderView.this, "Failed to add order.");
					}
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(OrderView.this, "Error: " + ex.getMessage());
				}
			}
		}

		private void updateProductDetailsFields(String productName) {
			Map<String, Object> productDetails = productHandler.getProductDetailsByName(productName);
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
	}

	public class UpdateButtonListener implements ActionListener {

		private OrderHandler orderHandler; // Assuming you have an OrderHandler class
		private ProductHandler productHandler; // Assuming you have a ProductHandler class

		public UpdateButtonListener(OrderHandler orderHandler, ProductHandler productHandler) {
			this.orderHandler = orderHandler;
			this.productHandler = productHandler;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			String orderNumberString = JOptionPane.showInputDialog("Enter Order Number to update:");
			if (orderNumberString != null && !orderNumberString.isEmpty()) {
				try {
					int orderNumber = Integer.parseInt(orderNumberString);
					Order existingOrder = orderHandler.getOrder(orderNumber);

					if (existingOrder != null) {
						JPanel panel = new JPanel(new GridLayout(0, 2));

						// Setup panel with existing order data
						JTextField orderDateField = new JTextField(existingOrder.getOrderDate() != null ? existingOrder.getOrderDate().toString() : "",10);
						JTextField requiredDateField = new JTextField(existingOrder.getRequiredDate() != null ? existingOrder.getRequiredDate().toString(): "",10);
						JTextField shippedDateField = new JTextField(existingOrder.getShippedDate() != null ? existingOrder.getShippedDate().toString() : "",10);
						JTextField statusField = new JTextField(existingOrder.getStatus(), 10);
						JTextField commentsField = new JTextField(existingOrder.getComments(), 10);
						JTextField customerNumberField = new JTextField(String.valueOf(existingOrder.getCustomerNumber()), 10);
						JTextField productCodeField = new JTextField(10);
						JTextField quantityOrderedField = new JTextField(10);
						JTextField orderLineNumberField = new JTextField(10);
						productCodeField.setEditable(false);
						productCodeField.setFocusable(false);
						productCodeField.setBackground(new Color(240, 240, 240)); // Light grey background color
						
						Map<String, String> products = productHandler.getProducts();
						JComboBox<String> productNameDropdown = new JComboBox<>();
						for (String productName : products.keySet()) {
							productNameDropdown.addItem(productName);
						}

						productNameDropdown.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								String selectedProductName = (String) productNameDropdown.getSelectedItem();
								String productCode = products.get(selectedProductName);
								productCodeDropdown.setSelectedItem(productCode);

								Map<String, Object> productDetails = productHandler
										.getProductDetailsByName(selectedProductName);
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
						});

						// Add components to the panel
						panel.add(new JLabel("Order Date (yyyy-MM-dd):"));
						panel.add(orderDateField);
						panel.add(new JLabel("Required Date:"));
						panel.add(requiredDateField);
						panel.add(new JLabel("Shipped Date:"));
						panel.add(shippedDateField);
						panel.add(new JLabel("Status:"));
						panel.add(statusField);
						panel.add(new JLabel("Comments:"));
						panel.add(commentsField);
						panel.add(new JLabel("Customer Number:"));
						panel.add(customerNumberField);
						panel.add(new JLabel("Product Name:"));
						panel.add(productNameDropdown);
						panel.add(new JLabel("Product Code:"));
						panel.add(productCodeField); // Use the JTextField for Product Code
						panel.add(new JLabel("Product Quantity:"));
						panel.add(quantityOrderedField);
						panel.add(new JLabel("Quantity in Stock:"));
						panel.add(quantityInStockField);
						panel.add(new JLabel("Buy Price:"));
						panel.add(buyPriceField);
						panel.add(new JLabel("MSRP:"));
						panel.add(msrpField);
						panel.add(new JLabel("orderLineNumber:"));
						panel.add(orderLineNumberField);

						
						int result = JOptionPane.showConfirmDialog(null, panel, "Update Order Details",
								JOptionPane.OK_CANCEL_OPTION);
						if (result == JOptionPane.OK_OPTION) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date orderDate = orderDateField.getText().isEmpty() ? null
									: dateFormat.parse(orderDateField.getText());
							Date requiredDate = requiredDateField.getText().isEmpty() ? null
									: dateFormat.parse(requiredDateField.getText());
							Date shippedDate = shippedDateField.getText().isEmpty() ? null
									: dateFormat.parse(shippedDateField.getText());
							String status = statusField.getText();
							String comments = commentsField.getText();
							int customerNumber = Integer.parseInt(customerNumberField.getText());

							// Update the order with new details
							existingOrder.setOrderDate(orderDate);
							existingOrder.setRequiredDate(requiredDate);
							existingOrder.setShippedDate(shippedDate);
							existingOrder.setStatus(status);
							existingOrder.setComments(comments);
							existingOrder.setCustomerNumber(customerNumber);

							boolean success = orderHandler.editOrder(existingOrder, orderNumber);
							if (success) {
								JOptionPane.showMessageDialog(null, "Order updated successfully!");
							} else {
								JOptionPane.showMessageDialog(null, "Failed to update order.");
							}
						}
					} else {
						JOptionPane.showMessageDialog(null, "Order with Order Number " + orderNumber + " not found.");
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null, "Invalid Order Number format.");
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
				}
			}
		}
	}

	/*
	 * OrderDetails orderDetails = new OrderDetails(quantityOrdered, buyPrice,
	 * productCode, orderLineNumber); Order updatedOrder = new Order(requiredDate,
	 * shippedDate, statusField.getText(), commentsField.getText(),
	 * Integer.parseInt(customerNumberField.getText()), orderDate);
	 */

	private class DeleteButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Prompt the user to enter the Order Number to delete
			String orderNumberString = JOptionPane.showInputDialog(OrderView.this, "Enter Order Number to delete:");

			if (orderNumberString != null && !orderNumberString.isEmpty()) {
				try {
					int orderNumber = Integer.parseInt(orderNumberString);

					// Confirm deletion with a dialog
					int confirmResult = JOptionPane.showConfirmDialog(OrderView.this,
							"Are you sure you want to delete Order Number " + orderNumber + "?", "Confirm Deletion",
							JOptionPane.YES_NO_OPTION);

					if (confirmResult == JOptionPane.YES_OPTION) {
						// Call the OrderHandler to delete the order
						boolean success = orderHandler.deleteOrder(orderNumber);

						if (success) {
							JOptionPane.showMessageDialog(OrderView.this,
									"Order Number " + orderNumber + " deleted successfully!");
						} else {
							JOptionPane.showMessageDialog(OrderView.this,
									"Failed to delete Order Number " + orderNumber + ".");
						}
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(OrderView.this, "Invalid Order Number format.");
				}
			}
		}
	}

	private class SearchButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Prompt the user to enter an Order Number for the search
			String searchParameter = JOptionPane.showInputDialog(OrderView.this, "Enter Order Number to search:");

			if (searchParameter != "" && !searchParameter.isEmpty()) {
				try {
					tableModel.setRowCount(0);
					List<Order> filter = orderHandler.searchOrder(searchParameter);
					for (Order o : filter) {
						Vector<Object> row = new Vector<>();
						row.add(o.getOrderNumber());
						row.add(o.getOrderDate());
						row.add(o.getRequiredDate());
						row.add(o.getShippedDate());
						row.add(o.getStatus());
						row.add(o.getComments());
						row.add(o.getCustomerNumber());

						tableModel.addRow(row);

					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(OrderView.this, "Invalid Order format");
				}
			}
		}
	}

	// StringBuilder resultMessage = new StringBuilder("Search result:\n");
	// resultMessage.append("Order Number: ").append(filter)

	/*
	 * if (searchParameter != "" && !searchParameter.isEmpty()) { try { // int
	 * orderNumber = Integer.parseInt(orderNumberString);
	 * 
	 * // Call the OrderHandler to retrieve the order // Order order =
	 * orderHandler.searchOrders(); List<Order> filter =
	 * orderHandler.searchOrders(searchParameter); tableModel.setRowCount(0);
	 * for(Order o:filter) { tableModel.ad; } // Display the order details
	 * StringBuilder resultMessage = new StringBuilder("Search result:\n");
	 * resultMessage.append("Order Number: ").append(filter.getOrderNumber()).append
	 * ("\n");
	 * resultMessage.append("Order Date: ").append(filter.getOrderDate()).append(
	 * "\n");
	 * resultMessage.append("Required Date: ").append(filter.getRequiredDate()).
	 * append("\n");
	 * resultMessage.append("Shipped Date: ").append(filter.getShippedDate()).append
	 * ("\n");
	 * resultMessage.append("Status: ").append(filter.getStatus()).append("\n");
	 * resultMessage.append("Comments: ").append(order.getComments()).append("\n");
	 * resultMessage.append("Customer Number: ").append(order.getCustomerNumber()).
	 * append("\n"); JOptionPane.showMessageDialog(OrderView.this,
	 * resultMessage.toString()); } else {
	 * /*JOptionPane.showMessageDialog(OrderView.this,
	 * "No order found for Order Number: "); } } catch (NumberFormatException ex) {
	 * JOptionPane.showMessageDialog(OrderView.this,
	 * "Invalid Order Number format."); } }
	 */

	// Add this method to your CheckStatusButtonListener class
	private class CheckStatusButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Prompt the user to enter an Order Number for status check
			String orderNumberString = JOptionPane.showInputDialog(OrderView.this,
					"Enter Order Number to check status:");

			if (orderNumberString != null && !orderNumberString.isEmpty()) {
				try {
					int orderNumber = Integer.parseInt(orderNumberString);

					// Call the OrderHandler to retrieve the order status
					String status = orderHandler.getOrderStatus(orderNumber);

					if (status != null) {
						// Display the order status
						JOptionPane.showMessageDialog(OrderView.this,
								"Order Status for Order Number " + orderNumber + ": " + status);
					} else {
						JOptionPane.showMessageDialog(OrderView.this,
								"No order found for Order Number: " + orderNumber);
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(OrderView.this, "Invalid Order Number format.");
				}
			}
		}
	}

	private class PaymentButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Prompt the user to enter a customer number for payment status check
			String customerNumberString = JOptionPane.showInputDialog(OrderView.this,
					"Enter Customer Number to check payment status:");

			if (customerNumberString != null && !customerNumberString.isEmpty()) {
				try {
					int customerNumber = Integer.parseInt(customerNumberString);

					// Check if the customer exists before checking payment status
					if (orderHandler.customerExists(customerNumber)) {
						boolean paid = orderHandler.checkPaymentStatus(customerNumber);

						if (paid) {
							JOptionPane.showMessageDialog(OrderView.this,
									"Payment Status for Customer Number " + customerNumber + ": Paid");
						} else {
							JOptionPane.showMessageDialog(OrderView.this,
									"Payment Status for Customer Number " + customerNumber + ": Not Paid");
						}
					} else {
						JOptionPane.showMessageDialog(OrderView.this,
								"Customer with Customer Number " + customerNumber + " does not exist.");
					}
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(OrderView.this, "Invalid Customer Number format.");
				}
			}
		}
	}

	private class OrderDetailsButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			// Create an instance of OrderDetailsView without passing an order number
			new OrderDetailsView(); // You can pass -1 or any default value
		}
	}
	
	private void saveOrdersToFile() {
	    JFileChooser fileChooser = new JFileChooser();
	    fileChooser.setDialogTitle("Specify a CSV file to save");
	    fileChooser.setSelectedFile(new File("Orders.csv")); // Set default file name

	    int userSelection = fileChooser.showSaveDialog(null);

	    if (userSelection == JFileChooser.APPROVE_OPTION) {
	        File fileToSave = fileChooser.getSelectedFile();

	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
	            List<String[]> orders = fetchAndDisplayOrders(); // Fetch order data

	            // Write header row (optional)
	            writer.write("Order Number, Order Date, Required Date, Shipped Date, Status, Comments, Customer Number");
	            writer.newLine();

	            // Write data rows
	            for (String[] order : orders) {
	                String line = String.join(",", order); // Comma as delimiter
	                writer.write(line);
	                writer.newLine();
	            }
	            JOptionPane.showMessageDialog(null, "CSV file saved successfully at " + fileToSave.getAbsolutePath());
	        } catch (IOException ex) {
	            JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
	        }
	    }
	}
	private class SaveOrderButtonListener implements ActionListener {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	        saveOrdersToFile();
	    }
	}
	
	private class AddMultipleProductsButtonListener implements ActionListener {
	    @Override
	    public void actionPerformed(ActionEvent e) {
	    	
			panel.add(new JLabel("Product Name:"));
			panel.add(productNameDropdown);
			panel.add(new JLabel("Product Code:"));
			panel.add(productCodeField); // Use the JTextField for Product Code
			panel.add(new JLabel("Product Quantity:"));
			panel.add(quantityOrderedField);
			panel.add(new JLabel("Quantity in Stock:"));
			panel.add(quantityInStockField);
			panel.add(new JLabel("Buy Price:"));
			panel.add(buyPriceField);
			panel.add(new JLabel("MSRP:"));
			panel.add(msrpField);
			panel.add(new JLabel("orderLineNumber:"));
			panel.add(orderLineNumberField);	    }
	}


}
