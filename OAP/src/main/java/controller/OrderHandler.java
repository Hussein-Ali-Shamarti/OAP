package controller;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import javax.swing.JFileChooser;

import model.Order;
import model.OrderDAO;
import model.OrderDetails;
import model.OrderInput;
import view.OrderView;

/**
 * Manages business logic related to order processing in the application.
 * Interacts with OrderView for UI elements and OrderDAO for database operations.
 * Facilitates adding, updating, deleting, searching orders, and saving order data to file.
 * 
 * @author 
 * @version 2.12.2023
 */

public class OrderHandler {

    private final OrderView orderView;
    private final OrderDAO orderDAO;

    
    /**
     * Constructs a new OrderHandler with the specified OrderView and OrderDAO.
     *
     * @param orderView The OrderView instance used for UI interactions.
     * @param orderDAO The OrderDAO instance used for data access operations.
     */

//    private static final String placeholderProductCode = "UNKNOWN"; // Define it here


    public OrderHandler(OrderView orderView, OrderDAO orderDAO) {
        this.orderView = orderView;
        this.orderDAO = orderDAO;
    }
    
    /**
     * Creates an ActionListener for adding a new order.
     *
     * @return ActionListener to add a new order.
     */

    /**
     * Creates an ActionListener for adding a new order.
     *
     * @return ActionListener to add a new order.
     */
    public ActionListener getAddOrderButtonListener() {
        return this::addOrder;
    }

    /**
     * Creates an ActionListener for updating an existing order.
     *
     * @return ActionListener to update an order.
     */
    public ActionListener getUpdateOrderButtonListener() {
        return this::updateOrder;
    }

    /**
     * Creates an ActionListener for deleting an order.
     *
     * @return ActionListener to delete an order.
     */
    public ActionListener getDeleteOrderButtonListener() {
        return this::deleteOrder;
    }

    /**
     * Creates an ActionListener for searching orders.
     *
     * @return ActionListener to search orders.
     */
    public ActionListener getSearchOrderButtonListener() {
        return this::searchOrder;
    }

    /**
     * Creates an ActionListener for saving orders to a file.
     *
     * @return ActionListener to save orders.
     */
    public ActionListener getSaveOrderButtonListener() {
        return this::saveOrdersToFile;
    }

    /**
     * Creates an ActionListener for checking payment status.
     *
     * @return ActionListener to check payment status.
     */
    public ActionListener getPaymentButtonListener() {
        return this::checkPaymentStatus;
    }

    /**
     * Creates an ActionListener for checking order status.
     *
     * @return ActionListener to check order status.
     */
    public ActionListener getCheckStatusButtonListener() {
        return this::checkOrderStatus;
    }

    /**
     * Handles the addition of a new order. Gathers user input for order details
     * and processes the addition of the order through OrderDAO.
     *
     * @param e The action event that triggers the add operation.
     */

    private void addOrder(ActionEvent e) {
        OrderInput orderAndDetails = orderView.gatherUserInputForAddOrder();
        System.out.println(orderView.gatherUserInputForAddOrder());

        if (orderAndDetails != null) {
            Order order = orderAndDetails.getOrder();
            List<OrderDetails> orderDetailsList = orderAndDetails.getOrderDetailsList();
            System.out.println(orderDetailsList.toString()+"test10");
          //  String placeholderProductCode = "UNKNOWN"; // Define the placeholderProductCode here

          //  for(OrderDetails orderdetails:orderDetailsList) {
            	
                boolean success = orderDAO.addOrder(order, orderDetailsList);
                if (success) {
                    JOptionPane.showMessageDialog(orderView, "New order added successfully!");
                } else {
                    JOptionPane.showMessageDialog(orderView, "Failed to add new order.");
                }

            }

        }

    /**
     * Handles updating an existing order. Fetches the order based on the provided
     * order number and prompts the user for update details.
     *
     * @param e The action event that triggers the update operation.
     */

    private void updateOrder(ActionEvent e) {
        String orderNumberString = JOptionPane.showInputDialog("Enter Order Number to update:");
        if (orderNumberString != null && !orderNumberString.isEmpty()) {
            try {
                int orderNumber = Integer.parseInt(orderNumberString);
                Order existingOrder = orderDAO.getOrder(orderNumber);
                if (existingOrder != null) {
                    Order updatedOrder = orderView.gatherUserInputForUpdateOrder(existingOrder);
                    if (updatedOrder != null) {
                        boolean success = orderDAO.editOrder(updatedOrder, orderNumber);
                        if (success) {
                            JOptionPane.showMessageDialog(orderView, "Order updated successfully!");
                        } else {
                            JOptionPane.showMessageDialog(orderView, "Failed to update order.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(orderView, "Order not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(orderView, "Invalid order number format.");
            }
        }
    }

    /**
     * Handles the deletion of an order. Deletes the order specified by the user.
     *
     * @param e The action event that triggers the delete operation.
     */
    
    private void deleteOrder(ActionEvent e) {
        Integer orderNumberToDelete = orderView.gatherUserInputForDeleteOrder();

        if (orderNumberToDelete != null) {
            boolean success = orderDAO.deleteOrder(orderNumberToDelete);

            if (success) {
                JOptionPane.showMessageDialog(orderView, "Order deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(orderView, "Failed to delete order.");
            }
        }
    }
    
    /**
     * Handles searching for orders based on user-provided criteria.
     *
     * @param e The action event that triggers the search operation.
     */

    private void searchOrder(ActionEvent e) {
        String searchCriteria = orderView.gatherUserInputForSearch();
        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            List<Order> searchResults = orderDAO.searchOrder(searchCriteria);
            orderView.gatherUserInputForSearchOrder(searchResults);
        }
    }
    
    /**
     * Handles saving the current order data to a file. Prompts the user
     * to choose a file destination and writes the data in CSV format.
     *
     * @param e The action event that triggers the save operation.
     */

    public void saveOrdersToFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a CSV file to save");
        fileChooser.setSelectedFile(new File("Orders.csv"));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                List<String[]> orders = orderView.fetchAndDisplayOrders();

                writer.write("Order Number, Order Date, Required Date, Shipped Date, Status, Comments, Customer Number");
                writer.newLine();

                for (String[] order : orders) {
                    String line = String.join(",", order);
                    writer.write(line);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "CSV file saved successfully at " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    /**
     * Checks the payment status of a customer. Verifies if the specified customer
     * has made all required payments.
     *
     * @param e The action event that triggers the payment status check.
     */

    private void checkPaymentStatus(ActionEvent e) {
        String customerNumberString = orderView.gatherInfoForPaymentCheck();

        if (customerNumberString != null && !customerNumberString.isEmpty()) {
            try {
                int customerNumber = Integer.parseInt(customerNumberString);
                checkPaymentStatus(orderView, customerNumber);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(orderView, "Invalid Customer Number format.");
            }
        }
    }
    
 
    public void checkPaymentStatus(OrderView orderView, int customerNumber) {
        if (orderDAO.customerExists(customerNumber)) {
            boolean paid = orderDAO.checkPaymentStatus(customerNumber);
            if (paid) {
                JOptionPane.showMessageDialog(orderView, "Payment Status for Customer Number " + customerNumber + ": Paid");
            } else {
                JOptionPane.showMessageDialog(orderView, "Payment Status for Customer Number " + customerNumber + ": Not Paid");
            }
        } else {
            JOptionPane.showMessageDialog(orderView, "Customer with Customer Number " + customerNumber + " does not exist.");
        }
    }
    
    /**
     * Checks the status of an order. Retrieves and displays the current status
     * of the specified order.
     *
     * @param e The action event that triggers the order status check.
     */


    public void checkOrderStatus(ActionEvent e) {
        String orderNumberString = orderView.gatherInfoForDeliverCheck();

        if (orderNumberString != null && !orderNumberString.isEmpty()) {
            try {
                int orderNumber = Integer.parseInt(orderNumberString);
                checkOrderStatus(orderView, orderNumber);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(orderView, "Invalid Order Number format.");
            }
        }
    }

    public void checkOrderStatus(OrderView orderView, int orderNumber) {
        String status = orderDAO.getOrderStatus(orderNumber);

        if (status != null) {
            JOptionPane.showMessageDialog(orderView, "Order Status for Order Number " + orderNumber + ": " + status);
        } else {
            JOptionPane.showMessageDialog(orderView, "No order found for Order Number: " + orderNumber);
            
            
        }
        
    }
  
 }
    
    

