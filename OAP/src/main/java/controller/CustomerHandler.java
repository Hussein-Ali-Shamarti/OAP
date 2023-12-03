/**
 * This class, {@code CustomerHandler}, serves as the controller for managing customer-related actions in the application.
 * It handles events triggered by the associated {@link CustomerView} and interacts with the {@link CustomerDAO} to
 * perform operations on customer data.
 *
 * @author 7080
 * @version 2.12.2023
 */

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

    private final CustomerView customerView;
    private final CustomerDAO customerDAO;

    /**
     * Constructs a new {@code CustomerHandler} with the given {@link CustomerView} and {@link CustomerDAO}.
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
     * Returns an ActionListener for updating a Customer.
     *
     * @return ActionListener for the update Customer action.
     */
    public ActionListener getUpdateCustomerButtonListener() {
        return this::updateCustomer;
    }

    /**
     * Returns an ActionListener for deleting a Customer.
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
    public ActionListener getSaveCustomerButtonListener() {
    	return this::saveCustomersToFile;
    }


//    public ActionListener getSaveCustomerButtonListener() {
//        return this::saveCustomersToFile;
//    }
//    
    /**
     * Handles the addition of a new customer.
     * 
     * @param e The action event that triggers the add operation.
     */
    
   

    private void addCustomer(ActionEvent e) {

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

    private void updateCustomer(ActionEvent e) {
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

    private void searchCustomer(ActionEvent e) {
        String searchCriteria = customerView.gatherUserInputForSearch();

        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            List<Customer> searchResults = customerDAO.searchCustomers(searchCriteria);
            customerView.updateTableWithSearchResults(searchResults);
        }
    }



    public void saveCustomersToFile(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a CSV file to save");
        fileChooser.setSelectedFile(new File("Customer.csv")); // Set default file name

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                List<String[]> customers = customerView.fetchAndDisplayCustomers(); // Fetch customer data from view

                // Write header row (optional)
                writer.write("Customer Number, Customer Name, Contact Last Name, Contact First Name, " +
                        "Phone, Address Line 1, Address Line 2, City, State, Postal Code, Country, " +
                        "Sales Rep Employee Number, Credit Limit");
                writer.newLine();

                // Write data rows
                for (String[] customer : customers) {
                    String line = String.join(",", customer); // Comma as delimiter
                    writer.write(line);
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "CSV file saved successfully at " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}