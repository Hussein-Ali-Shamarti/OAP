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

import javax.swing.Box;
import javax.swing.JButton;
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
import model.Order;


public class OrderView extends MainView {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField textField;  // Assuming you have a JTextField for search
    private OrderHandler orderHandler=new OrderHandler();
    public OrderView() {
        super();

        setLayout(new BorderLayout());
        initializeUI();
        fetchAndDisplayOrders();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        pack(); // Adjusts the frame to fit the components
        setVisible(true); // Make sure the frame is visible
    }

    private void initializeUI() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131));
        JLabel titleLabel = new JLabel("Order Management");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        setupTable();
        setupControlPanel();

        add(titlePanel, BorderLayout.NORTH);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center on screen
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
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        controlPanel.setBackground(new Color(90, 23, 139));

        JButton searchButton = createButton("Search", new SearchButtonListener());
        JButton addButton = createButton("Add", new AddButtonListener());
        JButton editButton = createButton("Edit", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());
        JButton checkStatusButton = createButton("Check Status", new CheckStatusButtonListener());
        JButton paymentButton = createButton("Check Payment Status", new PaymentButtonListener());


        controlPanel.add(searchButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(checkStatusButton);
        controlPanel.add(checkStatusButton); // Add the "Check Status" button to the control panel
        controlPanel.add(paymentButton); // Add the "Check Status" button to the control panel


        JPanel buttonPanelHolder = new JPanel(new BorderLayout());
        buttonPanelHolder.add(controlPanel, BorderLayout.NORTH);
        buttonPanelHolder.add(Box.createVerticalStrut(10), BorderLayout.CENTER); // Add space
        this.add(buttonPanelHolder, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(new Color(84, 11, 131));
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }

    void fetchAndDisplayOrders() {
        tableModel.setRowCount(0);
        try (Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String sql = "SELECT OrderNumber, orderDate, requiredDate, shippedDate, status, comments, customerNumber FROM orders";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Object[] row = {
                        resultSet.getString("OrderNumber"),
                        resultSet.getString("orderDate"),
                        resultSet.getString("requiredDate"),
                        resultSet.getString("shippedDate"),
                        resultSet.getString("status"),
                        resultSet.getString("comments"),
                        resultSet.getString("customerNumber")
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

         // Show confirm dialog with the form
         int result = JOptionPane.showConfirmDialog(null, panel, "Enter New Order Details", JOptionPane.OK_CANCEL_OPTION);
         if (result == JOptionPane.OK_OPTION) {
             try {
            	 String orderDateString = orderDateField.getText();
            	 String requiredDateString = requiredDateField.getText();
            	 String shippedDateString = shippedDateField.getText();
            	 SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            	 
            	 // Retrieving values from text fields
                 Date orderDate = dateFormat.parse(orderDateString);
                 Date requiredDate = dateFormat.parse(requiredDateString);
                 Date shippedDate = dateFormat.parse(shippedDateString);
                 String status = statusField.getText();
                 String comments = commentsField.getText();
                 int customerNumber = Integer.parseInt(customerNumberField.getText());
               
                 // Call to CustomerHandler to add customer
                 Order order = new Order(requiredDate, shippedDate, status, comments, customerNumber,orderDate);
                 boolean success = orderHandler.addOrder(order);
                 if (success) {
                     JOptionPane.showMessageDialog(OrderView.this, "Order added successfully!");
                 } else {
                     JOptionPane.showMessageDialog(OrderView.this, "Failed to add Order.");
                 }
             } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(OrderView.this, "Invalid input format.");
             } catch (Exception ex) {
                 JOptionPane.showMessageDialog(OrderView.this, "Error: " + ex.getMessage());
             }
         }
     }
 }

    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Prompt the user to enter the Order Number to update
            String orderNumberString = JOptionPane.showInputDialog(OrderView.this, "Enter Order Number to update:");

            if (orderNumberString != null && !orderNumberString.isEmpty()) {
                try {
                    int orderNumber = Integer.parseInt(orderNumberString);
                    Order existingOrder = orderHandler.getOrder(orderNumber);

                    if (existingOrder != null) {
                        // Fields for updating order details
                        JTextField orderDateField = new JTextField(10);
                        JTextField requiredDateField = new JTextField(10);
                        JTextField shippedDateField = new JTextField(10);
                        JTextField statusField = new JTextField(10);
                        JTextField commentsField = new JTextField(10);
                        JTextField customerNumberField = new JTextField(10);

                        // Set default values in the fields
                        orderDateField.setText(existingOrder.getOrderDate() != null ? existingOrder.getOrderDate().toString() : "");
                        requiredDateField.setText(existingOrder.getRequiredDate() != null ? existingOrder.getRequiredDate().toString() : "");
                        shippedDateField.setText(existingOrder.getShippedDate() != null ? existingOrder.getShippedDate().toString() : "");
                        statusField.setText(existingOrder.getStatus());
                        commentsField.setText(existingOrder.getComments());
                        customerNumberField.setText(String.valueOf(existingOrder.getCustomerNumber()));

                        // Panel for the update form
                        JPanel panel = new JPanel(new GridLayout(0, 2));

                        // Adding labels and text fields to the panel
                        panel.add(new JLabel("Order Date (yyyy-MM-dd):"));
                        panel.add(orderDateField);
                        panel.add(new JLabel("Required Date (yyyy-MM-dd):"));
                        panel.add(requiredDateField);
                        panel.add(new JLabel("Shipped Date (yyyy-MM-dd):"));
                        panel.add(shippedDateField);
                        panel.add(new JLabel("Status:"));
                        panel.add(statusField);
                        panel.add(new JLabel("Comments:"));
                        panel.add(commentsField);
                        panel.add(new JLabel("Customer Number:"));
                        panel.add(customerNumberField);

                        // Show confirm dialog with the update form
                        int result = JOptionPane.showConfirmDialog(null, panel, "Update Order Details", JOptionPane.OK_CANCEL_OPTION);
                        if (result == JOptionPane.OK_OPTION) {
                            try {
                                String orderDateString = orderDateField.getText();
                                String requiredDateString = requiredDateField.getText();
                                String shippedDateString = shippedDateField.getText();
                                String status = statusField.getText();
                                String comments = commentsField.getText();
                                int customerNumber = Integer.parseInt(customerNumberField.getText());

                                // Parse dates
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                Date orderDate = null;
                                Date requiredDate = null;
                                Date shippedDate = null;
                                if (!orderDateString.isEmpty()) {
                                    orderDate = new Date(dateFormat.parse(orderDateString).getTime());
                                }
                                if (!requiredDateString.isEmpty()) {
                                    requiredDate = new Date(dateFormat.parse(requiredDateString).getTime());
                                }
                                if (!shippedDateString.isEmpty()) {
                                    shippedDate = new Date(dateFormat.parse(shippedDateString).getTime());
                                }

                                // Create a new Order object with updated values
                                Order updatedOrder = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);

                                // Call the OrderHandler to update the order
                                boolean success = orderHandler.editOrder(updatedOrder, orderNumber);
                                if (success) {
                                    JOptionPane.showMessageDialog(OrderView.this, "Order updated successfully!");
                                } else {
                                    JOptionPane.showMessageDialog(OrderView.this, "Failed to update Order.");
                                }
                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(OrderView.this, "Invalid input format.");
                            } catch (Exception ex) {
                                JOptionPane.showMessageDialog(OrderView.this, "Error: " + ex.getMessage());
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(OrderView.this, "Order with Order Number " + orderNumber + " not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(OrderView.this, "Invalid Order Number format.");
                }
            }
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
            String orderNumberString = JOptionPane.showInputDialog(OrderView.this, "Enter Order Number to search:");

            if (orderNumberString != null && !orderNumberString.isEmpty()) {
                try {
                    int orderNumber = Integer.parseInt(orderNumberString);

                    // Call the OrderHandler to retrieve the order
                    Order order = orderHandler.getOrder(orderNumber);

                    if (order != null) {
                        // Display the order details
                        StringBuilder resultMessage = new StringBuilder("Search result:\n");
                        resultMessage.append("Order Number: ").append(order.getOrderNumber()).append("\n");
                        resultMessage.append("Order Date: ").append(order.getOrderDate()).append("\n");
                        resultMessage.append("Required Date: ").append(order.getRequiredDate()).append("\n");
                        resultMessage.append("Shipped Date: ").append(order.getShippedDate()).append("\n");
                        resultMessage.append("Status: ").append(order.getStatus()).append("\n");
                        resultMessage.append("Comments: ").append(order.getComments()).append("\n");
                        resultMessage.append("Customer Number: ").append(order.getCustomerNumber()).append("\n");
                        JOptionPane.showMessageDialog(OrderView.this, resultMessage.toString());
                    } else {
                        JOptionPane.showMessageDialog(OrderView.this, "No order found for Order Number: " + orderNumber);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(OrderView.this, "Invalid Order Number format.");
                }
            }
        }
    }
    
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
            }
        }
    }
    
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
            JOptionPane.showMessageDialog(OrderView.this, "Search button pressed");
        }
    }



}
    
    



   

