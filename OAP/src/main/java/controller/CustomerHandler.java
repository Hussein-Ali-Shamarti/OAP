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
 * Handles business logic associated with customer management in the application.
 * Coordinates interactions between CustomerView for user interface actions and
 * CustomerDAO for data persistence, enabling operations like adding, updating,
 * deleting, searching, and saving customer data.
 * 
 * @author 7080
 */


public class CustomerHandler {

    private final CustomerView customerView;
    private final CustomerDAO customerDAO; 

    /**
     * Constructs a new CustomerHandler with the specified CustomerView and CustomerDAO.
     *
     * @param customerView The CustomerView instance used for UI interactions.
     * @param customerDAO The CustomerDAO instance used for data access operations.

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
        return new SaveCustomerButtonListener();
    }
    
    
  //Controller for CRUD-methods + search


    /**
     * Handles the addition of a new customer. Gathers user input from the CustomerView
     * and adds the new customer through CustomerDAO.
     * 
     * @param e The action event that triggers the addition operation.
     */
   

    public void addCustomer(ActionEvent e) {

        Customer customer = customerView.gatherUserInputForAddCustomer();

        if (customer != null) {
            boolean success = customerDAO.addCustomer(customer);
            

            if (success) {
                JOptionPane.showMessageDialog(customerView, "Customer added successfully!");
                customerView.fetchAndDisplayCustomers();
            } else {
                JOptionPane.showMessageDialog(customerView, "Failed to add customer.");
            }
        }
    }

    /**
     * Handles the updating of a customer's information. Fetches the relevant customer
     * from the database based on user-provided customer number and prompts for editing details.
     * 
     * @param e The action event that triggers the update operation.
     */
    
    public void updateCustomer(ActionEvent e) {
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
                    customerView.fetchAndDisplayCustomers();
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


    public void deleteCustomer(ActionEvent e) {
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
     * Handles searching for customers based on user-provided search criteria.
     * 
     * @param e The action event that triggers the search operation.
     */

    public void searchCustomer(ActionEvent e) {
        String searchCriteria = customerView.gatherUserInputForSearch();

        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            List<Customer> searchResults = customerDAO.searchCustomers(searchCriteria);
            customerView.updateTableWithSearchResults(searchResults);
        }
    }

    /**
     * Inner class to handle saving customer data to a file when the associated
     * action is performed.
     */
    
    public class SaveCustomerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveCustomersToFile();
        }
    }

    /**
     * Handles saving the current customer data to a CSV file. Prompts the user
     * to choose a file destination and writes the data in CSV format.
     */
    
    public void saveCustomersToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a CSV file to save");
        fileChooser.setSelectedFile(new File("Customer.csv")); 

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                List<String[]> customers = customerView.fetchAndDisplayCustomers(); 

                
                writer.write("Customer Number, Customer Name, Contact Last Name, Contact First Name, " +
                        "Phone, Address Line 1, Address Line 2, City, State, Postal Code, Country, " +
                        "Sales Rep Employee Number, Credit Limit");
                writer.newLine();

                
                for (String[] customer : customers) {
                    String line = String.join(",", customer); 
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