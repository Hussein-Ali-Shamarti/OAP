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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
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
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.AddOrderButtonListener;
import controller.DeleteOrderButtonListener;
import controller.SearchOrderButtonListener;
import controller.UpdateOrderButtonListener;
import model.ProductDAO;
import model.OrderDAO;
import model.Order;
import model.OrderDetails;
import model.Products; // Adjust the package name as necessary


public class OrderView extends MainView {
	private JTextField textField;
	private static final long serialVersionUID = 1L;
	private DefaultTableModel tableModel;
	private JTable table;
	private OrderDAO OrderDAO = new OrderDAO();
	private JComboBox<String> productNameDropdown;
	private JComboBox<String> productCodeDropdown;
    private JTextField additionalProductCodeField;
    private JComboBox<String> additionalProductNameDropdown;

	private ProductDAO ProductDAO;
	private Map<String, String> products; // Declare products here
	private JTextField quantityInStockField;
	private JTextField buyPriceField;
	private JTextField msrpField;
	private JTextField productCodeField = new JTextField(10);
	private JTextField quantityOrderedField = new JTextField(10);
	private JTextField orderLineNumberField = new JTextField(10);
    private JPanel panel = new JPanel(new GridLayout(0, 2));
    private JScrollPane scrollPane = new JScrollPane(panel); // This will contain your panel and provide scrolling
  //  private boolean searchDialogVisible = false;

    /*   public boolean isSearchDialogVisible() {
        return searchDialogVisible;
    }

   public void setSearchDialogVisible(boolean isVisible) {
        this.searchDialogVisible = isVisible;
    }*/
    
	public OrderView() {
		super();
		this.OrderDAO = new OrderDAO(); // Initialize OrderDAO first
		this.ProductDAO = new ProductDAO(); // Initialize ProductDAO
		this.products = ProductDAO.getProducts(); // Initialize products
		this.quantityInStockField = new JTextField(10);
		this.buyPriceField = new JTextField(10);
		this.msrpField = new JTextField(10);
        //this.searchOrderButtonListener = new SearchOrderButtonListener(this, new OrderDAO());


		setLayout(new BorderLayout());
		initializeUI();
		setupProductDropdowns(); // Now setup the product dropdowns
		fetchAndDisplayOrders();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);
		pack(); // Adjusts the frame to fit the components
		setVisible(true); // Make sure the frame is visible
		//UpdateButtonListener updateButtonListener = new UpdateButtonListener(OrderDAO, ProductDAO);

	}


	private void setupProductDropdowns() {
		productNameDropdown = new JComboBox<>();
		productCodeDropdown = new JComboBox<>();

		// Use the 'products' field that's already populated in the constructor
		Map<String, String> products = ProductDAO.getProducts(); // Fetch products

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
				Map<String, Object> productDetails = ProductDAO.getProductDetailsByName(productName);
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
		return ProductDAO.getProductNameByCode(code); // Use the new method from ProductDAO
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
       // searchButton = new JButton("Search");
       // searchButton.addActionListener(e -> gatherUserInputForSearch());

	}
	
  /*  private void initializeComponents() {
        panel = new JPanel(new GridLayout(0, 2));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JButton addMultipleProducts = createButton("Add More Products +", new AddMultipleProductsButtonListener());
        panel.add(addMultipleProducts);
    }*/
    
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
		JButton searchButton = createButton("Search", new SearchOrderButtonListener(this, OrderDAO));
		JButton addButton = createButton("Add", new AddOrderButtonListener(this, OrderDAO));
		JButton editButton = createButton("Edit", new UpdateOrderButtonListener(this, OrderDAO, ProductDAO));
		JButton deleteButton = createButton("Delete", new DeleteOrderButtonListener(this, OrderDAO));
		JButton saveOrderButton = createButton("Save to File", new SaveOrderButtonListener());
		JButton orderDetailsButton = createButton("Order Details", new OrderDetailsButtonListener());
		// In your OrderView class constructor or appropriate method

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
	    // set other button properties
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


    private void addProductFields(JPanel panel, Map<String, String> products) {
    	JComboBox<String> productNameDropdown = new JComboBox<>();
        JTextField productCodeField = new JTextField(10);
        JTextField quantityOrderedField = new JTextField(10);
        JTextField orderLineNumberField = new JTextField(10);

        productCodeField.setEditable(false);
        productCodeField.setFocusable(false);

        for (String productName : products.keySet()) {
            productNameDropdown.addItem(productName);
        }

        productNameDropdown.addActionListener(e -> {
            String selectedProductName = productNameDropdown.getSelectedItem().toString();
            String productCode = products.get(selectedProductName);
            productCodeField.setText(productCode);
        });
        panel.add(new JLabel("Product Name:"));
        panel.add(productNameDropdown);
        panel.add(new JLabel("Product Code:"));
        panel.add(productCodeField);
        panel.add(new JLabel("Quantity Ordered:"));
        panel.add(quantityOrderedField);
        panel.add(new JLabel("Order Line Number:"));
        panel.add(orderLineNumberField);

        panel.revalidate();
        panel.repaint();
    }
    
    public Order gatherUserInputForAddOrder() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

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
		 scrollPane.validate();
	     scrollPane.repaint();
		// Populate the dropdown with product data from the database using an instance
		// of ProductDAO
		Map<String, String> products = ProductDAO.getProducts(); // Use the instance
		for (String productName : products.keySet()) {
			productNameDropdown.addItem(productName);
			productCodeDropdown.addItem(products.get(productName));
		}

        // Adding initial fields to the panel
        panel.add(new JLabel("Order Date (yyyy-MM-dd):")); panel.add(orderDateField);
        panel.add(new JLabel("Required Date:")); panel.add(requiredDateField);
        panel.add(new JLabel("Shipped Date:")); panel.add(shippedDateField);
        panel.add(new JLabel("Status:")); panel.add(statusField);
        panel.add(new JLabel("Comments:")); panel.add(commentsField);
        panel.add(new JLabel("Customer Number:")); panel.add(customerNumberField);

        // Adding initial product fields
        addProductFields(panel, ProductDAO.getProducts());

        // Adding button to add more products
        JButton addMoreProductsButton = new JButton("Add More Products +");
        addMoreProductsButton.addActionListener(e -> addProductFields(panel, ProductDAO.getProducts()));
        panel.add(addMoreProductsButton);

        // Show the dialog
        int result = JOptionPane.showConfirmDialog(null, scrollPane, "Enter New Order Details", JOptionPane.OK_CANCEL_OPTION);
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

                // Create Order and OrderDetails objects
                Order order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
                OrderDetails orderDetails = new OrderDetails(quantityOrdered, buyPrice, productCode,
						orderLineNumber);
				boolean success = OrderDAO.addOrder(order, orderDetails);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Order added successfully!");
                    // Optionally update the table or UI
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add order.");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
		return null;
    }
		
		
	  
		
		private void updateProductDetailsFields(String productName) {
			Map<String, Object> productDetails = ProductDAO.getProductDetailsByName(productName);
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
	

	public Order gatherUserInputForUpdateOrder(Order order) {
	    // Define fields for editing order details
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    JTextField orderDateField = new JTextField(order.getOrderDate() != null ? dateFormat.format(order.getOrderDate()) : "", 10);
	    JTextField requiredDateField = new JTextField(order.getRequiredDate() != null ? dateFormat.format(order.getRequiredDate()) : "", 10);
	    JTextField shippedDateField = new JTextField(order.getShippedDate() != null ? dateFormat.format(order.getShippedDate()) : "", 10);
	    JTextField statusField = new JTextField(order.getStatus(), 10);
	    JTextField commentsField = new JTextField(order.getComments(), 10);
	    JTextField customerNumberField = new JTextField(String.valueOf(order.getCustomerNumber()), 10);

	    // Panel to hold the form
	    JPanel panel = new JPanel(new GridLayout(0, 2));
	    panel.add(new JLabel("Order Date (yyyy-MM-dd):")); panel.add(orderDateField);
	    panel.add(new JLabel("Required Date (yyyy-MM-dd):")); panel.add(requiredDateField);
	    panel.add(new JLabel("Shipped Date (yyyy-MM-dd):")); panel.add(shippedDateField);
	    panel.add(new JLabel("Status:")); panel.add(statusField);
	    panel.add(new JLabel("Comments:")); panel.add(commentsField);
	    panel.add(new JLabel("Customer Number:")); panel.add(customerNumberField);

	    // Show confirm dialog with the form
	    int result = JOptionPane.showConfirmDialog(null, panel, "Edit Order Details", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	        try {
	            // Update the order object with new values from the form
	            order.setOrderDate(dateFormat.parse(orderDateField.getText()));
	            order.setRequiredDate(dateFormat.parse(requiredDateField.getText()));
	            order.setShippedDate(shippedDateField.getText().isEmpty() ? null : dateFormat.parse(shippedDateField.getText()));
	            order.setStatus(statusField.getText());
	            order.setComments(commentsField.getText());
	            order.setCustomerNumber(Integer.parseInt(customerNumberField.getText()));
	            
	            return order; // Return updated order
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Error in updating order: " + ex.getMessage());
	        }
	    }
	    return null; // Return null if the user cancels the operation
	}

	/*
	 * OrderDetails orderDetails = new OrderDetails(quantityOrdered, buyPrice,
	 * productCode, orderLineNumber); Order updatedOrder = new Order(requiredDate,
	 * shippedDate, statusField.getText(), commentsField.getText(),
	 * Integer.parseInt(customerNumberField.getText()), orderDate);
	 */

	public Integer gatherUserInputForDeleteOrder() {
        String orderNumberStr = JOptionPane.showInputDialog(this, "Enter Order Number to delete:");
        if (orderNumberStr != null && !orderNumberStr.isEmpty()) {
            try {
                int orderNumber = Integer.parseInt(orderNumberStr);
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete Order Number " + orderNumber + "?", 
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    return orderNumber;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Order Number format.");
            }
        }
        return null;
    }

    public String gatherUserInputForSearch() {
        JTextField searchField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Enter search criteria:"));
        panel.add(searchField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Search Orders", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
        	return searchField.getText().trim();
        }
        return null;
    }
	
    public void gatherUserInputForSearchOrder(List<Order> orders) {
        tableModel.setRowCount(0); // Clear the table
        for (Order order : orders) {
            Vector<Object> row = new Vector<>();
            row.add(order.getOrderNumber());
            row.add(order.getOrderDate());
            row.add(order.getRequiredDate());
            row.add(order.getShippedDate());
            row.add(order.getStatus());
            row.add(order.getComments());
            row.add(order.getCustomerNumber());
            tableModel.addRow(row); // Add row to the table
            
        }
    //    setSearchDialogVisible(false); // Hide the dialog after displaying results

    }

  /*  // Call this method instead of gatherUserInputForSearchOrder when you need to show the dialog
    public void showSearchDialog() {
    	//   if (!isSearchDialogVisible()) {
            JTextField searchField = new JTextField(20);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Enter Order Detail To Search:"));
            panel.add(searchField);

            int result = JOptionPane.showConfirmDialog(this, panel, "Search Orders", JOptionPane.OK_CANCEL_OPTION);
           System.out.println("test");
            if (result == JOptionPane.OK_OPTION) {
             System.out.println("bbb");
            	String searchParameter = searchField.getText().trim();
                if (!searchParameter.isEmpty()) {
                    System.out.println("test2");

                	// Use the instance to call the method
                    this.searchOrderButtonListener.searchOrders(searchParameter);
                 //  setSearchDialogVisible(false);

                }
                
            } else {
             //   setSearchDialogVisible(false);
            }
            // }
    }

	 */

	// StringBuilder resultMessage = new StringBuilder("Search result:\n");
	// resultMessage.append("Order Number: ").append(filter)

	/*
	 * if (searchParameter != "" && !searchParameter.isEmpty()) { try { // int
	 * orderNumber = Integer.parseInt(orderNumberString);
	 * 
	 * // Call the OrderDAO to retrieve the order // Order order =
	 * OrderDAO.searchOrders(); List<Order> filter =
	 * OrderDAO.searchOrders(searchParameter); tableModel.setRowCount(0);
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

					// Call the OrderDAO to retrieve the order status
					String status = OrderDAO.getOrderStatus(orderNumber);

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
					if (OrderDAO.customerExists(customerNumber)) {
						boolean paid = OrderDAO.checkPaymentStatus(customerNumber);

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
	
	
	//adding multiple products
	private class AddMultipleProductsButtonListener1 implements ActionListener {
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
			panel.add(orderLineNumberField);	   
			
	    }
	    
	}




}
