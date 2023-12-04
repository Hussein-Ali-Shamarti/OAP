package view;

import java.awt.BorderLayout;
import java.awt.Color;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;


import model.ProductDAO;
import model.ProductEntry;
import model.OrderDAO;
import model.Order;
import model.OrderDetails;
import model.OrderInput;
import controller.OrderHandler;

/**
 * Represents an order entity with information such as order number, dates, status, comments,
 * customer number, and associated order date.
 * <p>Orders may also contain order details, which are not implemented in this version.</p>
 * 
 * @author 7094
 */

public class OrderView extends MainView {
	
	/**
	 * The serial version UID for object serialization.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The table model used for managing table data.
	 */
	private DefaultTableModel tableModel;

	/**
	 * The table component for displaying data.
	 */
	private JTable table;

	/**
	 * Data Access Object (DAO) instance for handling orders.
	 */
	private OrderDAO OrderDAO = new OrderDAO();

	/**
	 * A dropdown list for selecting product names.
	 */
	private JComboBox<String> productNameDropdown;

	/**
	 * A dropdown list for selecting product codes.
	 */
	private JComboBox<String> productCodeDropdown;

	/**
	 * Data Access Object (DAO) instance for handling products.
	 */
	private ProductDAO ProductDAO;

	/**
	 * The handler for order-related actions.
	 */
	private OrderHandler orderHandler;

	/**
	 * A text field for displaying the quantity in stock.
	 */
	private JTextField quantityInStockField;

	/**
	 * A text field for displaying the buy price of a product.
	 */
	private JTextField buyPriceField;

	/**
	 * A text field for displaying the Manufacturer's Suggested Retail Price (MSRP) of a product.
	 */
	private JTextField msrpField;

	/**
	 * A text field for entering order line numbers.
	 */
	private JTextField orderLineNumberField = new JTextField(10);

	/**
	 * A text field for displaying the product code.
	 */
	JTextField productCodeField = new JTextField(10);

	/**
	 * A text field for entering order dates.
	 */
	JTextField orderDateField = new JTextField(10);

	/**
	 * A text field for entering required dates.
	 */
	JTextField requiredDateField = new JTextField(10);

	/**
	 * A text field for entering shipped dates.
	 */
	JTextField shippedDateField = new JTextField(10);

	/**
	 * A text field for entering order statuses.
	 */
	JTextField statusField = new JTextField(10);

	/**
	 * A text field for entering order comments.
	 */
	JTextField commentsField = new JTextField(10);

	/**
	 * A text field for entering customer numbers.
	 */
	JTextField customerNumberField = new JTextField(10);

	/**
	 * A text field for entering ordered quantities.
	 */
	JTextField quantityOrderedField = new JTextField(10);


	/**
	 * Constructs a new OrderView instance.
	 * Initializes the OrderDAO, ProductDAO, and orderHandler.
	 * Sets up product dropdowns, fetches and displays orders, and configures the UI.
	 */
	
	public OrderView() {
		super();
		this.OrderDAO = new OrderDAO(); 
		this.ProductDAO = new ProductDAO(); 
		this.orderHandler = new OrderHandler(this, this.OrderDAO);
		ProductDAO.getProducts();
		this.quantityInStockField = new JTextField(10);
		this.buyPriceField = new JTextField(10);
		this.msrpField = new JTextField(10);

		setLayout(new BorderLayout());
		initializeUI();
		setupProductDropdowns(); 
		fetchAndDisplayOrders();

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(1000, 600);
		setLocationRelativeTo(null);
		pack(); 
		setVisible(true); 
	}
	
	/**
	 * Sets up product dropdowns for product selection.
	 * Populates product name and code dropdowns with data from the ProductDAO.
	 * Configures event listeners to display product details when a product code is selected.
	 */
	
	private void setupProductDropdowns() {
		productNameDropdown = new JComboBox<>();
		productCodeDropdown = new JComboBox<>();

		
		Map<String, String> products = ProductDAO.getProducts(); 

		for (String productName : products.keySet()) {
			productNameDropdown.addItem(productName);
			productCodeDropdown.addItem(products.get(productName));
		}

		
		productCodeDropdown.setEditable(false);
		productCodeDropdown.setFocusable(false); 
		((JTextField) productCodeDropdown.getEditor().getEditorComponent()).setDisabledTextColor(Color.BLACK);

		productCodeDropdown.addActionListener(e -> {
			String selectedCode = (String) productCodeDropdown.getSelectedItem();
			String productName = findProductNameByCode(selectedCode); 
			if (productName != null) {
				productNameDropdown.setSelectedItem(productName);
				
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
		});
	}

	/**
	 * Finds a product name by its product code.
	 *
	 * @param code The product code to search for.
	 * @return The corresponding product name, or null if not found.
	 */
	
	private String findProductNameByCode(String code) {
		return ProductDAO.getProductNameByCode(code); 
	}
	
	/**
	 * Initializes the user interface components and its layout.
	 */

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

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setSize(600, 400);
		setLocationRelativeTo(null);


	}
	
	/**
	 * Sets up the table for displaying orders.
	 */
    
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

	/**
	 * Sets up the control panel with buttons for various actions.
	 */
	
	private void setupControlPanel() {
		JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
		controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
		controlPanel.setBackground(new Color(90, 23, 139));
		JButton searchButton = createButton("Search", orderHandler.getSearchOrderButtonListener());
		JButton addButton = createButton("Add", orderHandler.getAddOrderButtonListener());
		JButton editButton = createButton("Edit", orderHandler.getUpdateOrderButtonListener());
		JButton deleteButton = createButton("Delete", orderHandler.getDeleteOrderButtonListener());
		JButton saveOrderButton = createButton("Save to File", orderHandler.getSaveOrderButtonListener());
		JButton orderDetailsButton = createButton("Order Details", new OrderDetailsButtonListener());

		controlPanel.add(searchButton);
		controlPanel.add(addButton);
		controlPanel.add(editButton);
		controlPanel.add(deleteButton);
		controlPanel.add(saveOrderButton); 
		controlPanel.add(orderDetailsButton); 

		JPanel statusPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		statusPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
		statusPanel.setBackground(new Color(100, 25, 150));
		JButton checkStatusButton = createButton("Check Shipping Status", new CheckStatusButtonListener());
		JButton paymentButton = createButton("Check Payment Status", new PaymentButtonListener());
		statusPanel.add(checkStatusButton);
		statusPanel.add(paymentButton);

		JPanel panelHolder = new JPanel(new BorderLayout());
		panelHolder.add(statusPanel, BorderLayout.NORTH);
		panelHolder.add(controlPanel, BorderLayout.SOUTH);
		this.add(panelHolder, BorderLayout.SOUTH);
	}
	
	/**
	 * Creates a customized JButton with the specified text and ActionListener.
	 *
	 * @param text     The text to display on the button.
	 * @param listener The ActionListener to associate with the button.
	 * @return The customized JButton.
	 */

	private JButton createButton(String text, ActionListener listener) {
		JButton button = new JButton(text);
		button.setForeground(Color.BLACK);
		button.setBackground(Color.WHITE);
		button.setFocusPainted(false);
		button.addActionListener(listener);
		return button;
	}

	/**
	 * Fetches and displays orders in the table.
	 *
	 * @return A list of String arrays representing the fetched orders.
	 */
	
	public List<String[]> fetchAndDisplayOrders() {
	    List<String[]> orders = new ArrayList<>();
	    tableModel.setRowCount(0); 

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
	            tableModel.addRow(order); 
	            orders.add(order); 
	        }
	    } catch (SQLException e) {
	        JOptionPane.showMessageDialog(null, "Error fetching order data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	    }
	    return orders;
	}

	/**
	 * Adds product-related fields to the specified panel, including dropdowns for product name and product code.
	 * Updates field values based on user selection from the dropdowns.
	 *
	 * @param panel                The JPanel to which the fields will be added.
	 * @param products             A map of product names and their corresponding product codes.
	 * @param addMoreProductsButton The button to add more product fields.
	 */
	
	private void addProductFields(JPanel panel, Map<String, String> products, JButton addMoreProductsButton) {
	    JComboBox<String> productNameDropdown = new JComboBox<>();

	    productCodeField.setEditable(false);
	    productCodeField.setFocusable(false);
	    productCodeField.setBackground(new Color(240, 240, 240)); 

	    quantityInStockField.setEditable(false);
	    quantityInStockField.setFocusable(false);
	    quantityInStockField.setBackground(new Color(240, 240, 240)); 

	    buyPriceField.setEditable(false);
	    buyPriceField.setFocusable(false);
	    buyPriceField.setBackground(new Color(240, 240, 240)); 

	    msrpField.setEditable(false);
	    msrpField.setFocusable(false);
	    msrpField.setBackground(new Color(240, 240, 240)); 

	    productNameDropdown.removeAllItems();
	    for (String productName : products.keySet()) {
	        productNameDropdown.addItem(productName);
	    }

	    productNameDropdown.addActionListener(e -> {
	        String selectedProductName = (String) productNameDropdown.getSelectedItem();
	        String productCode = products.get(selectedProductName);
	        productCodeField.setText(productCode);
	        
	        Map<String, Object> productDetails = ProductDAO.getProductDetailsByName(selectedProductName);
	        if (productDetails != null && !productDetails.isEmpty()) {
	            quantityInStockField.setText(productDetails.get("quantityInStock").toString());
	            buyPriceField.setText(productDetails.get("buyPrice").toString());
	            msrpField.setText(productDetails.get("MSRP").toString());
	        } else {
	        	quantityInStockField.setText("");
	            buyPriceField.setText("");
	            msrpField.setText("");
	        }
	    });

	    panel.add(new JLabel("Product Name:"));
	    panel.add(productNameDropdown);
	    panel.add(new JLabel("Product Code:"));
	    panel.add(productCodeField);
	    panel.add(new JLabel("Quantity:"));
	    panel.add(quantityOrderedField);
	    panel.add(new JLabel("Quantity in Stock:"));
	    panel.add(quantityInStockField);
	    panel.add(new JLabel("Buy Price:"));
	    panel.add(buyPriceField);
	    panel.add(new JLabel("MSRP:"));
	    panel.add(msrpField);



	    panel.remove(addMoreProductsButton);
	    int numRows = panel.getComponentCount() / 2 + 1; 
	    panel.setLayout(new GridLayout(numRows, 2)); 
	    panel.add(addMoreProductsButton); 
	    panel.revalidate();
	    panel.repaint();
	}
	
	/**
	 * Gathers user input for adding a new order, including order details and product information.
	 * Displays an input dialog with fields for order information and product selection.
	 *
	 * @return An OrderInput object containing the user-entered order and order details.
	 */
    
    public OrderInput gatherUserInputForAddOrder() {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

	    productCodeField.setEditable(false);
	    productCodeField.setFocusable(false);
	    productCodeField.setBackground(new Color(240, 240, 240)); 
	    quantityInStockField.setEditable(false);
	    quantityInStockField.setFocusable(false);
	    quantityInStockField.setBackground(new Color(240, 240, 240)); 

	    buyPriceField.setEditable(false);
	    buyPriceField.setFocusable(false);
	    buyPriceField.setBackground(new Color(240, 240, 240)); 

	    msrpField.setEditable(false);
	    msrpField.setFocusable(false);
	    msrpField.setBackground(new Color(240, 240, 240)); 
	    makeFieldReadOnly(productCodeField);

		JComboBox<String> productNameDropdown = new JComboBox<>();
		JComboBox<String> productCodeDropdown = new JComboBox<>();

		 scrollPane.validate();
	     scrollPane.repaint();

	     Map<String, String> products = ProductDAO.getProducts();
	        for (String productName : products.keySet()) {
	            productNameDropdown.addItem(productName);
	            productCodeDropdown.addItem(products.get(productName));
	        }

        panel.add(new JLabel("Order Date (yyyy-MM-dd):")); panel.add(orderDateField);
        panel.add(new JLabel("Required Date:")); panel.add(requiredDateField);
        panel.add(new JLabel("Shipped Date:")); panel.add(shippedDateField);
        panel.add(new JLabel("Status:")); panel.add(statusField);
        panel.add(new JLabel("Comments:")); panel.add(commentsField);
        panel.add(new JLabel("Customer Number:")); panel.add(customerNumberField);
       

        JButton addMoreProductsButton = new JButton("Add More Products +");
        addProductFields(panel, ProductDAO.getProducts(), addMoreProductsButton);

        addMoreProductsButton.addActionListener(e -> {
            addProductFields(panel, ProductDAO.getProducts(), addMoreProductsButton);
            scrollPane.getViewport().revalidate();
        });
        
        panel.add(addMoreProductsButton); 

        int result = JOptionPane.showConfirmDialog(null, scrollPane, "Enter New Order Details", JOptionPane.OK_CANCEL_OPTION);
       OrderInput orderInput= null;
        if (result == JOptionPane.OK_OPTION) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date orderDate = dateFormat.parse(orderDateField.getText());
                Date requiredDate = dateFormat.parse(requiredDateField.getText());
                Date shippedDate = dateFormat.parse(shippedDateField.getText());
                String status = statusField.getText();
                String comments = commentsField.getText();
                int customerNumber = customerNumberField.getText().isEmpty() ? 0 : Integer.parseInt(customerNumberField.getText());

                List<OrderDetails> orderDetailsList = new ArrayList<>();

				String productCode = productCodeField.getText();
				//System.out.println("411 "+productCode);
	            int quantityOrdered = quantityOrderedField.getText().isEmpty() ? 0 : Integer.parseInt(quantityOrderedField.getText());
				System.out.println("413 "+status);
	            int orderLineNumber = orderLineNumberField.getText().isEmpty() ? 0 : Integer.parseInt(orderLineNumberField.getText());
	            double buyPrice = buyPriceField.getText().isEmpty() ? 0.0 : Double.parseDouble(buyPriceField.getText());
	            
	          
                OrderDetails orderDetails = new OrderDetails(quantityOrdered, buyPrice, productCode, orderLineNumber);
                orderDetailsList.add(orderDetails);    
                Order order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
                orderInput= new OrderInput(order, orderDetailsList);
             
            } catch (Exception ex) {
            	System.out.println("test");
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            }
        }
    	System.out.println("test2 "+orderInput);

        return orderInput;
    }
    
    /**
     * Makes the specified JTextField read-only by disabling editing and setting its background color.
     *
     * @param field The JTextField to make read-only.
     */
    
    private void makeFieldReadOnly(JTextField field) {
        field.setEditable(false);
        field.setFocusable(false);
        field.setBackground(new Color(240, 240, 240));
    }

    /**
     * Displays a dialog with the provided JScrollPane and waits for the user to make a choice.
     *
     * @param scrollPane The JScrollPane containing the dialog content.
     * @return An integer representing the user's choice (e.g., OK or Cancel).
     */

    private int showDialogAndGetResult(JScrollPane scrollPane) {
        return JOptionPane.showConfirmDialog(null, scrollPane, "Enter New Order Details", JOptionPane.OK_CANCEL_OPTION);
    }

    /**
     * Gathers user input for updating an existing order and displays an input dialog with editable fields
     * pre-filled with the existing order's information.
     *
     * @param order The Order object to be updated.
     * @return An updated Order object with modified information or null if the user cancels the operation.
     */

	public Order gatherUserInputForUpdateOrder(Order order) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	     orderDateField = new JTextField(order.getOrderDate() != null ? dateFormat.format(order.getOrderDate()) : "", 10);
	     requiredDateField = new JTextField(order.getRequiredDate() != null ? dateFormat.format(order.getRequiredDate()) : "", 10);
	     shippedDateField = new JTextField(order.getShippedDate() != null ? dateFormat.format(order.getShippedDate()) : "", 10);
	     statusField = new JTextField(order.getStatus(), 10);
	     commentsField = new JTextField(order.getComments(), 10);
	     customerNumberField = new JTextField(String.valueOf(order.getCustomerNumber()), 10);

	    JPanel panel = new JPanel(new GridLayout(0, 2));
	    panel.add(new JLabel("Order Date (yyyy-MM-dd):")); panel.add(orderDateField);
	    panel.add(new JLabel("Required Date (yyyy-MM-dd):")); panel.add(requiredDateField);
	    panel.add(new JLabel("Shipped Date (yyyy-MM-dd):")); panel.add(shippedDateField);
	    panel.add(new JLabel("Status:")); panel.add(statusField);
	    panel.add(new JLabel("Comments:")); panel.add(commentsField);
	    panel.add(new JLabel("Customer Number:")); panel.add(customerNumberField);

	    int result = JOptionPane.showConfirmDialog(null, panel, "Edit Order Details", JOptionPane.OK_CANCEL_OPTION);
	    if (result == JOptionPane.OK_OPTION) {
	        try {

	            order.setOrderDate(dateFormat.parse(orderDateField.getText()));
	            order.setRequiredDate(dateFormat.parse(requiredDateField.getText()));
	            order.setShippedDate(shippedDateField.getText().isEmpty() ? null : dateFormat.parse(shippedDateField.getText()));
	            order.setStatus(statusField.getText());
	            order.setComments(commentsField.getText());
	            order.setCustomerNumber(Integer.parseInt(customerNumberField.getText()));
	            
	            return order; 
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Error in updating order: " + ex.getMessage());
	        }
	    }
	    return null; 
	}

	/**
	 * Gathers user input for deleting an order by displaying a dialog prompting for the order number and
	 * confirming the deletion operation.
	 *
	 * @return The order number to be deleted if confirmed by the user; otherwise, null.
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
	
	/**
	 * Gathers user input for searching orders by displaying a dialog with a text input field for the search criteria.
	 *
	 * @return The search criteria entered by the user or null if the user cancels the operation.
	 */

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
    
    /**
     * Gathers user input for searching and displays the search results in a table.
     *
     * @param orders The list of Order objects to be displayed in the table.
     */
	
    public void gatherUserInputForSearchOrder(List<Order> orders) {
        tableModel.setRowCount(0); 
        for (Order order : orders) {
            Vector<Object> row = new Vector<>();
            row.add(order.getOrderNumber());
            row.add(order.getOrderDate());
            row.add(order.getRequiredDate());
            row.add(order.getShippedDate());
            row.add(order.getStatus());
            row.add(order.getComments());
            row.add(order.getCustomerNumber());
            tableModel.addRow(row); 
            
        }

    }
    
    /**
     * Gathers user input for checking payment status by displaying a dialog prompting for the customer number.
     *
     * @return The customer number entered by the user.
     */
    
    public String gatherInfoForPaymentCheck() {
        return JOptionPane.showInputDialog(this, "Enter Customer Number to check payment status:");


	}
    
    /**
     * Gathers user input for checking order delivery status by displaying a dialog prompting for the order number.
     *
     * @return The order number entered by the user.
     */
	
	public String gatherInfoForDeliverCheck() {
        return JOptionPane.showInputDialog(OrderView.this, "Enter Order Number to check status:");
    }

	/**
	 * ActionListener implementation for the "Check Status" button. It prompts the user for an order number and
	 * displays the order status.
	 */

	public class CheckStatusButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {

			String orderNumberString = JOptionPane.showInputDialog(OrderView.this,
					"Enter Order Number to check status:");

			if (orderNumberString != null && !orderNumberString.isEmpty()) {
				try {
					int orderNumber = Integer.parseInt(orderNumberString);

					String status = OrderDAO.getOrderStatus(orderNumber);

					if (status != null) {
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
	
	/**
	 * ActionListener implementation for the "Payment" button. It prompts the user for a customer number and
	 * displays the payment status.
	 */

	private class PaymentButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String customerNumberString = JOptionPane.showInputDialog(OrderView.this,
					"Enter Customer Number to check payment status:");

			if (customerNumberString != null && !customerNumberString.isEmpty()) {
				try {
					int customerNumber = Integer.parseInt(customerNumberString);

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
	
	/**
	 * ActionListener implementation for an "Order Details" button. It opens a new OrderDetailsView.
	 */

	private class OrderDetailsButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			new OrderDetailsView(); 
		}
	}
}

