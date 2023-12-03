package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import model.Customer;
import model.CustomerDAO;
import view.CustomerView;


/**
 * CustomerHandler class handles the user interactions related to Customer operations
 * in the application. It acts as a bridge between the CustomerView and CustomerDAO,
 * facilitating actions such as adding, updating, deleting, and searching for customers.
 * It also includes functionalities to save customer data to a file.
 * 
 * @author 7080
 * @version 2.12.2023
 */

public class CustomerHandler {

    /**
     * The associated {@link CustomerView} instance for which actions are handled.
     */
    private final CustomerView customerView;

    /**
     * The data access object for managing customers in the model.
     */
    private final CustomerDAO customerDAO;

    /**
     * Constructs a new {@code customerHandler} with the given {@link CustomerView} and {@link CustomerDAO}.
     *
     * @param customerView The associated product view.
     * @param customerDAO  The data access object for managing customers.
     */
    public CustomerHandler(CustomerView customerView, CustomerDAO customerDAO) {
        this.customerView = customerView;
        this.customerDAO = customerDAO;
    }
    
    /**
     * Returns an ActionListener for adding a new Customer.
     * 
     * @return ActionListener for the add Customer action.
     */

    public ActionListener getAddCustomerButtonListener() {
        return this::addCustomer;
    }
    
    /**
     * Returns an ActionListener for updating an Customer.
     * 
     * @return ActionListener for the update Customer action.
     */

    public ActionListener getUpdateCustomerButtonListener() {
        return this::updateCustomer;
    }
    
    /**
     * Returns an ActionListener for deleting an Customer.
     * 
     * @return ActionListener for the delete Customer action.
     */

    public ActionListener getDeleteCustomerButtonListener() {
        return this::deleteCustomer;
    }
    
    /**
     * Returns an ActionListener for searching Customers.
     * 
     * @return ActionListener for the search Customer action.
     */

    public ActionListener getSearchCustomerButtonListener() {
        return this::searchCustomer;
    }
    
    /**
     * Returns an ActionListener for saving Customer data to a file.
     * 
     * @return ActionListener for the save Customer data action.
     */

//    public ActionListener getSaveCustomerButtonListener() {
//        return this::saveCustomersToFile;
//    }
//    
    /**
     * Handles the addition of a new customer.
     * 
     * @param e The action event that triggers the add operation.
     */
    
    private void  addCustomer (ActionEvent e) {
        Customer customer = customerView.gatherUserInputForAddCustomer();

        if (customer != null) {
            boolean success = customerDAO.addCustomer(customer);
            customerView.fetchAndDisplayCustomers();

            if (success) {
                JOptionPane.showMessageDialog(customerView, "Customer added successfully!");
            } else {
                JOptionPane.showMessageDialog(customerView, "Failed to add customer.");
            }
        }
    }
    
    /**
     * Handles updating an existing customer.
     * 
     * @param e The action event that triggers the update operation.
     */

    private void updateCustomer (ActionEvent e) {
        String customerNumberStr = JOptionPane.showInputDialog(customerView, "Enter Customer Number to edit:");
        if (customerNumberStr != null && !customerNumberStr.isEmpty()) {
            try {
                int customerNumber = Integer.parseInt(customerNumberStr);

                CustomerDAO handler = new CustomerDAO();
                Customer customer = handler.fetchCustomerData(customerNumber);

                if (customer != null) {
                    customerView.gatherUserInputForUpdateCustomer(customer);
                    customerView.fetchAndDisplayCustomers();
                } else {
                    JOptionPane.showMessageDialog(customerView, "Customer not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(customerView, "Invalid customer number format.");
            }
        }
    }
    
    /**
     * Handles the deletion of a customer.
     * 
     * @param e The action event that triggers the delete operation.
     */

    private void deleteCustomer(ActionEvent e) {
        Integer customerNumberToDelete = customerView.gatherUserInputForDeleteCustomer();

        if (customerNumberToDelete != null) {
            boolean success = customerDAO.deleteCustomer(customerNumberToDelete);
            customerView.fetchAndDisplayCustomers();

            if (success) {
                JOptionPane.showMessageDialog(customerView, "Customer deleted successfully.");
                customerView.fetchAndDisplayCustomers();
            } else {
                JOptionPane.showMessageDialog(customerView, "Failed to delete customer.");
            }
        }
    }
    
    /**
     * Handles searching for customer based on a specified criteria.
     * 
     * @param e The action event that triggers the search operation.
     */

    private void searchCustomer(ActionEvent e) {
        String searchCriteria = customerView.gatherUserInputForSearch();

        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            List<Customer> searchResults = customerDAO.searchCustomers(searchCriteria);
            customerView.updateTableWithSearchResults(searchResults);
        }
    }
    
    /**
     * Handles saving the current customer data to a file.
     * 
     * @param e The action event that triggers the save operation.
     */

    

}



