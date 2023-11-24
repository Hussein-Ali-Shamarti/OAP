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
import java.text.SimpleDateFormat;
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

import controller.OrderHandler;
import controller.ProductHandler;
import model.Order;
import model.Products; // Adjust the package name as necessary




public class OrderView extends MainView {
   private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private OrderHandler orderHandler=new OrderHandler();
    private JComboBox<String> productNameDropdown;
    private JComboBox<String> productCodeDropdown;
    private ProductHandler productHandler;
    private Map<String, String> products; // Declare products here

    public OrderView() {
        super();
        this.orderHandler = new OrderHandler();        // Initialize OrderHandler first
        this.productHandler = new ProductHandler();    // Initialize ProductHandler
        this.products = productHandler.getProducts();  // Initialize products

        setLayout(new BorderLayout());
        initializeUI();
        setupProductDropdowns();                       // Now setup the product dropdowns
        fetchAndDisplayOrders();
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        pack(); // Adjusts the frame to fit the components
        setVisible(true); // Make sure the frame is visible
    }
    
    private void setupProductDropdowns() {
        productNameDropdown = new JComboBox<>();
        productCodeDropdown = new JComboBox<>();

        // Use the 'products' field that's already populated in the constructor
        for (String productName : this.products.keySet()) {
            productNameDropdown.addItem(productName);
            productCodeDropdown.addItem(this.products.get(productName));
        }

        productNameDropdown.addActionListener(e -> {
            String selectedName = (String) productNameDropdown.getSelectedItem();
            if (selectedName != null && this.products.containsKey(selectedName)) {
                productCodeDropdown.setSelectedItem(this.products.get(selectedName));
            }
        });
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
        // Set frame properties
       setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
    }
    private void setupTable() {
        String[] columnNames = {"Order Number", "Order Date", "Required Date", "Shipped Date", "Status", "Comments", "Customer Number"};
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
        JButton editButton = createButton("Edit", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());
        controlPanel.add(searchButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        // Control panel for the Check Status and Check Payment Status buttons
        JPanel statusPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        statusPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        statusPanel.setBackground(new Color(100, 25, 150));
        JButton checkStatusButton = createButton("Check Status", new CheckStatusButtonListener());
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

    private void fetchAndDisplayOrders() {

        try {
            tableModel.setRowCount(0);
        	Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement(); 

            // Adjusted query to join orders with orderDetails to fetch productCode
            String ordersSql = "SELECT o.OrderNumber, o.orderDate, o.requiredDate, o.shippedDate, o.status, o.comments, o.customerNumber  " + //od.productCode
                               "FROM orders o ";
                               //"JOIN orderDetails od ON o.OrderNumber = od.OrderNumber"; //join inner or join outer osv
            ResultSet ordersResultSet = statement.executeQuery(ordersSql);

            // Iterate through the orders and display the data
            while (ordersResultSet.next()) {
                Object[] row = {
                    ordersResultSet.getString("OrderNumber"),
                    ordersResultSet.getDate("orderDate"),
                    ordersResultSet.getDate("requiredDate"),
                    ordersResultSet.getDate("shippedDate"),
                    ordersResultSet.getString("status"),
                    ordersResultSet.getString("comments"),
                    ordersResultSet.getInt("customerNumber"), // Assuming customerNumber is an int
                    //ordersResultSet.getString("productCode") // This now comes from the joined orderDetails table

                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching order data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
 

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
            


            // Panel for the form
            JPanel panel = new JPanel(new GridLayout(0, 2));
            
            JComboBox<String> productNameDropdown = new JComboBox<>();
            JComboBox<String> productCodeDropdown = new JComboBox<>();
            
            // Populate the dropdown with product data from the database using an instance of ProductHandler
            Map<String, String> products = productHandler.getProducts(); // Use the instance
            for (String productName : products.keySet()) {
                productNameDropdown.addItem(productName);
            }          
            
            // Adding labels and text fields to the panel
            panel.add(new JLabel("Order Date (yyyy-MM-dd) :"));
            panel.add(orderDateField);
            panel.add(new JLabel("Required Date:"));
            panel.add(requiredDateField);
            panel.add(new JLabel("Shipped Date:"));
            panel.add(shippedDateField);
            panel.add(new JLabel("Status:"));
            panel.add(statusField);
            panel.add(new JLabel("Comments:"));
            panel.add(commentsField);
            panel.add(new JLabel("customerNumber:"));
            panel.add(customerNumberField);
            panel.add(new JLabel("Product Name:"));
            panel.add(productNameDropdown);
            panel.add(new JLabel("Product Code:"));
            panel.add(productCodeDropdown);

            // Show confirm dialog with the form
            int result = JOptionPane.showConfirmDialog(null, panel, "Enter New Order Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    Date orderDate = dateFormat.parse(orderDateField.getText());
                    Date requiredDate = dateFormat.parse(requiredDateField.getText());
                    Date shippedDate = dateFormat.parse(shippedDateField.getText());
                    String status = statusField.getText();
                    String comments = commentsField.getText();
                    int customerNumber = Integer.parseInt(customerNumberField.getText());

                    String selectedProductName = (String) productNameDropdown.getSelectedItem();
                    String productCode = productHandler.getProductCodeByName(selectedProductName);

                    Order order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
                    boolean success = orderHandler.addOrder(order);
                    if (success) {
                        JOptionPane.showMessageDialog(OrderView.this, "Order added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(OrderView.this, "Failed to add Order.");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(OrderView.this, "Error: " + ex.getMessage());
                }
            }
        }
    }


    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String orderNumberString = JOptionPane.showInputDialog(OrderView.this, "Enter Order Number to update:");
            if (orderNumberString != null && !orderNumberString.isEmpty()) {
                try {
                    int orderNumber = Integer.parseInt(orderNumberString);
                    Order existingOrder = orderHandler.getOrder(orderNumber);

                    if (existingOrder != null) {
                        JTextField orderDateField = new JTextField(existingOrder.getOrderDate() != null ? existingOrder.getOrderDate().toString() : "", 10);
                        JTextField requiredDateField = new JTextField(existingOrder.getRequiredDate() != null ? existingOrder.getRequiredDate().toString() : "", 10);
                        JTextField shippedDateField = new JTextField(existingOrder.getShippedDate() != null ? existingOrder.getShippedDate().toString() : "", 10);
                        JTextField statusField = new JTextField(existingOrder.getStatus(), 10);
                        JTextField commentsField = new JTextField(existingOrder.getComments(), 10);
                        JTextField customerNumberField = new JTextField(String.valueOf(existingOrder.getCustomerNumber()), 10);

                        JPanel panel = new JPanel(new GridLayout(0, 2));
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

                        // Reset and set product dropdowns based on associated product
                        // Fetch associated product details for the order (this part needs to be implemented)
                        Products associatedProduct = fetchProductsForOrder(existingOrder);
                        if (associatedProduct != null) {
                            productNameDropdown.setSelectedItem(associatedProduct.getProductName());
                            productCodeDropdown.setSelectedItem(associatedProduct.getProductCode());
                        }

                        panel.add(new JLabel("Product Name:"));
                        panel.add(productNameDropdown);
                        panel.add(new JLabel("Product Code:"));
                        panel.add(productCodeDropdown);

                        int result = JOptionPane.showConfirmDialog(null, panel, "Update Order Details", JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            Date orderDate = !orderDateField.getText().isEmpty() ? dateFormat.parse(orderDateField.getText()) : null;
                            Date requiredDate = !requiredDateField.getText().isEmpty() ? dateFormat.parse(requiredDateField.getText()) : null;
                            Date shippedDate = !shippedDateField.getText().isEmpty() ? dateFormat.parse(shippedDateField.getText()) : null;
                            
                            // Update the Order object with the new details
                            Order updatedOrder = new Order(requiredDate, shippedDate, statusField.getText(), commentsField.getText(), Integer.parseInt(customerNumberField.getText()), orderDate);
                            boolean success = orderHandler.editOrder(updatedOrder, orderNumber);
                            if (success) {
                                JOptionPane.showMessageDialog(OrderView.this, "Order updated successfully!");
                            } else {
                                JOptionPane.showMessageDialog(OrderView.this, "Failed to update Order.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(OrderView.this, "Order with Order Number " + orderNumber + " not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(OrderView.this, "Invalid Order Number format.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(OrderView.this, "Error: " + ex.getMessage());
                }
            }
        }

        private Products fetchProductsForOrder(Order order) {
            // Implement the logic to fetch the Product associated with the given order
            // This should return a Product object based on your application's data model
            // Example: return productHandler.getProductByOrder(order);
            return null; // Replace this with actual implementation
        }
    }



    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Prompt the user to enter the Order Number to delete
            String orderNumberString = JOptionPane.showInputDialog(OrderView.this, "Enter Order Number to delete:");

            if (orderNumberString != null && !orderNumberString.isEmpty()) {
                try {
                    int orderNumber = Integer.parseInt(orderNumberString);

                    // Confirm deletion with a dialog
                    int confirmResult = JOptionPane.showConfirmDialog(OrderView.this, "Are you sure you want to delete Order Number " + orderNumber + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    
                    if (confirmResult == JOptionPane.YES_OPTION) {
                        // Call the OrderHandler to delete the order
                        boolean success = orderHandler.deleteOrder(orderNumber);
                        
                        if (success) {
                            JOptionPane.showMessageDialog(OrderView.this, "Order Number " + orderNumber + " deleted successfully!");
                        } else {
                            JOptionPane.showMessageDialog(OrderView.this, "Failed to delete Order Number " + orderNumber + ".");
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
                try {tableModel.setRowCount(0);
                	List<Order> filter = orderHandler.searchOrder(searchParameter);
                	for(Order o : filter) {
                		System.out.println(o);
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
                	System.out.println(filter);
                }catch(NumberFormatException ex) {
                	JOptionPane.showMessageDialog(OrderView.this,  "Invalid Order format");
                }
            }}}

                	//StringBuilder resultMessage = new StringBuilder("Search result:\n");
                	//resultMessage.append("Order Number: ").append(filter)

          /*  if (searchParameter != "" && !searchParameter.isEmpty()) {
                try {
                   // int orderNumber = Integer.parseInt(orderNumberString);

                    // Call the OrderHandler to retrieve the order
                 //  Order order = orderHandler.searchOrders();
                	List<Order> filter = orderHandler.searchOrders(searchParameter);
                	tableModel.setRowCount(0); 
                	for(Order o:filter) {
                	tableModel.ad;
                	}
                        // Display the order details
                        StringBuilder resultMessage = new StringBuilder("Search result:\n");
                        resultMessage.append("Order Number: ").append(filter.getOrderNumber()).append("\n");
                        resultMessage.append("Order Date: ").append(filter.getOrderDate()).append("\n");
                        resultMessage.append("Required Date: ").append(filter.getRequiredDate()).append("\n");
                        resultMessage.append("Shipped Date: ").append(filter.getShippedDate()).append("\n");
                        resultMessage.append("Status: ").append(filter.getStatus()).append("\n");
                        resultMessage.append("Comments: ").append(order.getComments()).append("\n");
                        resultMessage.append("Customer Number: ").append(order.getCustomerNumber()).append("\n");
                        JOptionPane.showMessageDialog(OrderView.this, resultMessage.toString());
                    } else {
                        /*JOptionPane.showMessageDialog(OrderView.this, "No order found for Order Number: ");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(OrderView.this, "Invalid Order Number format.");
                }
            } */
        
    
    
    
    
 // Add this method to your CheckStatusButtonListener class
    private class CheckStatusButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Prompt the user to enter an Order Number for status check
            String orderNumberString = JOptionPane.showInputDialog(OrderView.this, "Enter Order Number to check status:");

            if (orderNumberString != null && !orderNumberString.isEmpty()) {
                try {
                    int orderNumber = Integer.parseInt(orderNumberString);

                    // Call the OrderHandler to retrieve the order status
                    String status = orderHandler.getOrderStatus(orderNumber);

                    if (status != null) {
                        // Display the order status
                        JOptionPane.showMessageDialog(OrderView.this, "Order Status for Order Number " + orderNumber + ": " + status);
                    } else {
                        JOptionPane.showMessageDialog(OrderView.this, "No order found for Order Number: " + orderNumber);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(OrderView.this, "Invalid Order Number format.");
                }
            }}}
        
    
    
    private class PaymentButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Prompt the user to enter a customer number for payment status check
            String customerNumberString = JOptionPane.showInputDialog(OrderView.this, "Enter Customer Number to check payment status:");

            if (customerNumberString != null && !customerNumberString.isEmpty()) {
                try {
                    int customerNumber = Integer.parseInt(customerNumberString);

                    // Check if the customer exists before checking payment status
                    if (orderHandler.customerExists(customerNumber)) {
                        boolean paid = orderHandler.checkPaymentStatus(customerNumber);

                        if (paid) {
                            JOptionPane.showMessageDialog(OrderView.this, "Payment Status for Customer Number " + customerNumber + ": Paid");
                        } else {
                            JOptionPane.showMessageDialog(OrderView.this, "Payment Status for Customer Number " + customerNumber + ": Not Paid");
                        }
                    } else {
                        JOptionPane.showMessageDialog(OrderView.this, "Customer with Customer Number " + customerNumber + " does not exist.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(OrderView.this, "Invalid Customer Number format.");
                }
            }
        }
    }



}
    
    