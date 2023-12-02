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


public class CustomerHandler {

    /**
     * The associated {@link ProductView} instance for which actions are handled.
     */
    private final CustomerView customerView;

    /**
     * The data access object for managing products in the model.
     */
    private final CustomerDAO customerDAO;

    /**
     * Constructs a new {@code ProductHandler} with the given {@link ProductView} and {@link ProductDAO}.
     *
     * @param productView The associated product view.
     * @param productDAO  The data access object for managing products.
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
     * Handles the addition of a new Customer.
     * 
     * @param e The action event that triggers the add operation.
     */
    
    private void  addCustomer (ActionEvent e) {
        Customer customer = customerView.gatherUserInputForAddCustomer();

        if (customer != null) {
            boolean success = customerDAO.addCustomer(customer);

            if (success) {
                JOptionPane.showMessageDialog(customerView, "Customer added successfully!");
            } else {
                JOptionPane.showMessageDialog(customerView, "Failed to add customer.");
            }
        }
    }
    
    /**
     * Handles updating an existing employee.
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
                } else {
                    JOptionPane.showMessageDialog(customerView, "Customer not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(customerView, "Invalid customer number format.");
            }
        }
    }
    
    /**
     * Handles the deletion of an customer.
     * 
     * @param e The action event that triggers the delete operation.
     */

    private void deleteCustomer(ActionEvent e) {
        Integer customerNumberToDelete = customerView.gatherUserInputForDeleteCustomer();

        if (customerNumberToDelete != null) {
            boolean success = customerDAO.deleteCustomer(customerNumberToDelete);

            if (success) {
                JOptionPane.showMessageDialog(customerView, "Customer deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(customerView, "Failed to delete customer.");
            }
        }
    }
    
    /**
     * Handles searching for employees based on a specified criteria.
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
     * Handles saving the current employee data to a file.
     * 
     * @param e The action event that triggers the save operation.
     */

    
    
    
}



