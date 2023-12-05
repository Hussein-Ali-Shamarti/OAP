package view;

import java.awt.BorderLayout;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import java.awt.event.ActionListener;

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

public class OrderView extends JFrame {


	
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
	 * A JTextField used for entering or displaying the product name. It allows users to input or view
	 * information related to the product's name, such as during data entry or display.
	 */
	
	JTextField productNameField = new JTextField(10);

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
	 * Button used for adding more products to the order.
	 */
	JButton addMoreProductsButton = new JButton("Add More Products +");

	/**
	 * Label indicating the input field for the order date in the format "yyyy-MM-dd".
	 */
	JLabel orderDateLabel = new JLabel("Order Date (yyyy-MM-dd):");

	/**
	 * Label indicating the input field for the required date.
	 */
	JLabel requiredDateLabel = new JLabel("Required Date:");

	/**
	 * Label indicating the input field for the shipped date.
	 */
	JLabel shippedDateLabel = new JLabel("Shipped Date:");

	/**
	 * Label indicating the input field for the order status.
	 */
	JLabel statusLabel = new JLabel("Status:");

	/**
	 * Label indicating the input field for comments related to the order.
	 */
	JLabel commentsLabel = new JLabel("Comments:");

	/**
	 * Label indicating the input field for the customer number.
	 */
	JLabel customerNumberLabel = new JLabel("Customer Number:");

	/**
	 * Label indicating the input field for the product name.
	 */
	JLabel productNameLabel = new JLabel("Product Name:");

	/**
	 * Label indicating the input field for the product code.
	 */
	JLabel productCodeLabel = new JLabel("Product Code:");

	/**
	 * Label indicating the input field for the quantity ordered.
	 */
	JLabel quantityOrderedLabel = new JLabel("Quantity:");

	/**
	 * Label indicating the input field for the buy price of the product.
	 */
	JLabel buyPriceLabel = new JLabel("Buy Price:");

	/**
	 * Label indicating the input field for the quantity in stock of the product.
	 */
	JLabel quantityInStockLabel = new JLabel("Quantity in Stock:");

	/**
	 * Label indicating the input field for the Manufacturer's Suggested Retail Price (MSRP) of the product.
	 */
	JLabel msrpLabel = new JLabel("MSRP:");


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
	
	public void setupProductDropdowns() {
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
	
	public String findProductNameByCode(String code) {
		return ProductDAO.getProductNameByCode(code); 
	}
	
	/**
	 * Initializes the user interface components and its layout.
	 */

	public void initializeUI() {
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
	
	public void setupControlPanel() {
		JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
		controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
		controlPanel.setBackground(new Color(90, 23, 139));
		JButton searchButton = createButton("Search", orderHandler.getSearchOrderButtonListener());
		JButton addButton = createButton("Add", orderHandler.getAddOrderButtonListener());
		JButton editButton = createButton("Edit", orderHandler.getUpdateOrderButtonListener());
		JButton deleteButton = createButton("Delete", orderHandler.getDeleteOrderButtonListener());
		JButton saveOrderButton = createButton("Save to File", orderHandler.getSaveOrderButtonListener());
		JButton orderDetailsButton = createButton("Order Details", orderHandler.getorderDetailsView());

		controlPanel.add(searchButton);
		controlPanel.add(addButton);
		controlPanel.add(editButton);
		controlPanel.add(deleteButton);
		controlPanel.add(saveOrderButton); 
		controlPanel.add(orderDetailsButton);

		JPanel statusPanel = new JPanel(new GridLayout(1, 2, 10, 10));
		statusPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
		statusPanel.setBackground(new Color(100, 25, 150));
		JButton checkStatusButton = createButton("Check Shipping Status", e -> orderHandler.checkOrderStatus(e));
		JButton paymentButton = createButton("Check Payment Status", e -> orderHandler.checkPaymentStatus(e));
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
	 * Panel used for organizing components with a grid layout of 0 rows and 2 columns.
	 */

    JPanel panel = new JPanel(new GridLayout(0, 2));

    /**
     * Gathers user input for adding a new order and its associated order details.
     * Displays a dialog for the user to input the order and order details, such as order date,
     * required date, shipped date, status, comments, customer number, quantity ordered, order line number,
     * buy price, MSRP, and product code.
     *
     * @return An instance of OrderInput containing the user-inputted order and order details,
     *         or null if the user cancels the operation or encounters an error.
     */
    
    public OrderInput gatherUserInputForAddOrder() {
        JScrollPane scrollPane = new JScrollPane(panel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        orderDateField.setText("");
        requiredDateField.setText("");
        shippedDateField.setText("");
        statusField.setText("");
        commentsField.setText("");
        customerNumberField.setText("");
        quantityOrderedField.setText("");
        orderLineNumberField.setText("");
        buyPriceField.setText("");
        msrpField.setText("");
        productCodeField.setText("");

        panel.add(orderDateLabel);
        panel.add(orderDateField);
        panel.add(requiredDateLabel);
        panel.add(requiredDateField);
        panel.add(shippedDateLabel); 
        panel.add(shippedDateField);
        panel.add(statusLabel);
        panel.add(statusField);
        panel.add(commentsLabel); 
        panel.add(commentsField);
        panel.add(customerNumberLabel);
        panel.add(customerNumberField);

	    panel.add(productNameLabel);
	    panel.add(productNameDropdown);
	    panel.add(productCodeLabel);
	    panel.add(productCodeField);
	    panel.add(quantityOrderedLabel);
	    panel.add(quantityOrderedField);
	    panel.add(quantityInStockLabel);
	    panel.add(quantityInStockField);
	    panel.add(buyPriceLabel);
	    panel.add(buyPriceField);
	    panel.add(msrpLabel);
	    panel.add(msrpField);

		
	    productCodeField.setEditable(false);
	    productCodeField.setFocusable(false);
	    productCodeField.setBackground(new Color(240, 240, 240)); 
	    makeFieldReadOnly(productCodeField);

	    quantityInStockField.setEditable(false);
	    quantityInStockField.setFocusable(false);
	    quantityInStockField.setBackground(new Color(240, 240, 240)); 

	    buyPriceField.setEditable(false);
	    buyPriceField.setFocusable(false);
	    buyPriceField.setBackground(new Color(240, 240, 240)); 

	    msrpField.setEditable(false);
	    msrpField.setFocusable(false);
	    msrpField.setBackground(new Color(240, 240, 240)); 

		 scrollPane.validate();
	     scrollPane.repaint();
		
	     Map<String, String> products = ProductDAO.getProducts();
	        for (String productName : products.keySet()) {
	            productNameDropdown.addItem(productName);
	            productCodeDropdown.addItem(products.get(productName));
	        }
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

	    int result = JOptionPane.showConfirmDialog(null, scrollPane, "Enter New Order Details", JOptionPane.OK_CANCEL_OPTION);
	    OrderInput orderInput = null;
	    if (result == JOptionPane.OK_OPTION) {
	        try {
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	            Date orderDate = dateFormat.parse(orderDateField.getText());
	            Date requiredDate = dateFormat.parse(requiredDateField.getText());
	            Date shippedDate = dateFormat.parse(shippedDateField.getText());
	            String status = statusField.getText();
	            String comments = commentsField.getText();
	            int customerNumber = customerNumberField.getText().isEmpty() ? 0 : Integer.parseInt(customerNumberField.getText());

	            String productCode = productCodeField.getText();
	            int quantityOrdered = quantityOrderedField.getText().isEmpty() ? 0 : Integer.parseInt(quantityOrderedField.getText());
	            
	            if (ProductDAO.isStockAvailable(productCode, quantityOrdered)) {
	                int orderLineNumber = orderLineNumberField.getText().isEmpty() ? 0 : Integer.parseInt(orderLineNumberField.getText());
	                double buyPrice = buyPriceField.getText().isEmpty() ? 0.0 : Double.parseDouble(buyPriceField.getText());
	                
	                OrderDetails orderDetails = new OrderDetails(quantityOrdered, buyPrice, productCode, orderLineNumber);
	                List<OrderDetails> orderDetailsList = new ArrayList<>();
	                orderDetailsList.add(orderDetails);    
	                Order order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
	                orderInput = new OrderInput(order, orderDetailsList);

	                // Update stock quantity
	                ProductDAO.updateProductStock(productCode, -quantityOrdered);
	            } else {
	                JOptionPane.showMessageDialog(null, "Not enough stock available.");
	            }
	        } catch (Exception ex) {
	            JOptionPane.showMessageDialog(null, "Error: " + ex.getMessage());
	        }
	    }

	    if(result == JOptionPane.CANCEL_OPTION) {
	        // Clearing fields if cancel is clicked
	        panel.remove(productCodeField);
	        panel.remove(productCodeLabel);
	        panel.remove(buyPriceField);
	        panel.remove(buyPriceLabel);
	        panel.remove(quantityOrderedField);
	        panel.remove(quantityOrderedLabel);
	        panel.remove(orderDateLabel);
	        panel.remove(orderDateField);
	        panel.remove(requiredDateField);
	        panel.remove(requiredDateLabel);
	        panel.remove(shippedDateField);
	    }
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
	 * Fetches and displays orders in the table.
	 *
	 * @return A list of String arrays representing the fetched orders.
	 */

	 
	
	public List<String[]> fetchAndDisplayOrders() {
		List<String[]> orders = OrderDAO.fetchOrders(); // Fetch data using DAO
        updateTableWithOrders(orders);

        return orders; // If you need to use the orders elsewhere in your class
    }
	
	private List<String[]> updateTableWithOrders(List<String[]> orders) {
        // Clear the existing table data
        tableModel.setRowCount(0);

        // Update UI by adding orders to the table
        for (String[] order : orders) {
            tableModel.addRow(order);
        }

        return orders; 

}
	
}

