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

public class OrderHandler {

    private final OrderView orderView;
    private final OrderDAO orderDAO;

    public OrderHandler(OrderView orderView, OrderDAO orderDAO) {
        this.orderView = orderView;
        this.orderDAO = orderDAO;
    }

    public ActionListener getAddOrderButtonListener() {
        return this::addOrder;
    }

    public ActionListener getUpdateOrderButtonListener() {
        return this::updateOrder;
    }

    public ActionListener getDeleteOrderButtonListener() {
        return this::deleteOrder;
    }

    public ActionListener getSearchOrderButtonListener() {
        return this::searchOrder;
    }

    public ActionListener getSaveOrderButtonListener() {
        return this::saveOrdersToFile;
    }

    public ActionListener getPaymentButtonListener() {
        return this::checkPaymentStatus;
    }

    public ActionListener getCheckStatusButtonListener() {
        return this::checkOrderStatus;
    }

    private void addOrder(ActionEvent e) {
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

    private void searchOrder(ActionEvent e) {
        String searchCriteria = orderView.gatherUserInputForSearch();
        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            List<Order> searchResults = orderDAO.searchOrder(searchCriteria);
            orderView.gatherUserInputForSearchOrder(searchResults);
        }
    }
    


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
    
    

