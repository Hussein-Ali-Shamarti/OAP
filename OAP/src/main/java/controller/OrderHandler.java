package controller;

import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;

import model.Order;
import model.OrderDAO;
import model.OrderDetails;
import model.OrderInput;
import view.OrderDetailsView;
import view.OrderView;

/**
 * Manages business logic related to order processing in the application.
 * Interacts with OrderView for UI elements and OrderDAO for database operations.
 * Facilitates adding, updating, deleting, searching orders, and saving order data to file.
 * 
 * @author 7094
 * 
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
    
    public ActionListener getorderDetailsView() {
        return this::orderDetailsView;
    }
    
    //Controller for CRUD-methods + search

    /**
     * Handles the addition of a new order. Gathers user input for order details
     * and processes the addition of the order through OrderDAO.
     *
     * @param e The action event that triggers the add operation.
     */

    public void addOrder(ActionEvent e) {
        OrderInput orderAndDetails = orderView.gatherUserInputForAddOrder();
        if (orderAndDetails != null) {
            Order order = orderAndDetails.getOrder();
            List<OrderDetails> orderDetailsList = orderAndDetails.getOrderDetailsList();
            
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

    public void updateOrder(ActionEvent e) {
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
    
    public void deleteOrder(ActionEvent e) {
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

    public void searchOrder(ActionEvent e) {
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
    
    //Controller for other order related methods
    
    /**
     * Checks the payment status of a customer. Verifies if the specified customer
     * has made all required payments.
     *
     * @param e The action event that triggers the payment status check.
     */

    public void checkPaymentStatus(ActionEvent e) {
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
    
    /**
     * Checks the payment status for a customer with the given customer number and displays the result in a dialog box.
     *
     * @param orderView       The view component where the payment status message will be displayed.
     * @param customerNumber  The customer number for which to check the payment status.
     */
    
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

    /**
     * Checks the status of an order with the given order number and displays the status in a dialog box.
     *
     * @param orderView    The view component where the status message will be displayed.
     * @param orderNumber  The order number for which to check the status.
     */
    
    public void checkOrderStatus(OrderView orderView, int orderNumber) {
        String status = orderDAO.getOrderStatus(orderNumber);

        if (status != null) {
            JOptionPane.showMessageDialog(orderView, "Order Status for Order Number " + orderNumber + ": " + status);
        } else {
            JOptionPane.showMessageDialog(orderView, "No order found for Order Number: " + orderNumber);
            
            
        }
        
    }
    
    /**
     * Imports a list of orders from a CSV file and inserts them into the database.
     *
     * @param csvFile The CSV file containing order data to be imported.
     * @return true if the import and insertion were successful, false otherwise.
     */
    
    public boolean importOrders(File csvFile) {
        try {
            List<Order> orders = parseCsvFile(csvFile);
            return orderDAO.insertOrdersIntoDatabase(orders);
        } catch (Exception e) {
            e.printStackTrace(); 
            return false;
        }
    }
  
    /**
     * Parses a CSV file containing order data and converts it into a List of Order objects.
     *
     * @param csvFile The CSV file to be parsed.
     * @return A List of Order objects representing the orders parsed from the CSV file.
     * @throws IOException If an error occurs while reading or parsing the CSV file.
     */
    
    public List<Order> parseCsvFile(File csvFile) throws IOException {
        List<Order> orders = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Adjust date format as necessary
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line = br.readLine(); // Read the header to skip it

            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assuming CSV values are comma-separated
                // Make sure you have the correct number of columns in each row
                if (values.length != 6) {
                    throw new IOException("Invalid number of columns in CSV file");
                }
                
                // Parse CSV values into Order fields
                Date orderDate = new Date(dateFormat.parse(values[0]).getTime());
                Date requiredDate = new Date(dateFormat.parse(values[1]).getTime());
                Date shippedDate = new Date(dateFormat.parse(values[2]).getTime());
                String status = values[3];
                String comments = values[4];
                int customerNumber = Integer.parseInt(values[5]);
                
                // Ensure proper values are being parsed and are not the header
                if (!status.equals("status")) { // Assuming 'status' is not a valid order status
                    Order order = new Order(requiredDate, shippedDate, status, comments, customerNumber, orderDate);
                    orders.add(order);
                }
            }
        } catch (ParseException e) {
            throw new IOException("Error parsing the date in the CSV file: " + e.getMessage(), e);
        } catch (NumberFormatException e) {
            throw new IOException("Error parsing the customer number in the CSV file: " + e.getMessage(), e);
        }
        return orders;
    }
    
    /**
	 * ActionListener implementation for an "Order Details" button. It opens a new OrderDetailsView.
	 */

		
		public void orderDetailsView(ActionEvent e) {
			new OrderDetailsView(); 
		
	}
		
			

}
		
	
	
  
 
    
    

