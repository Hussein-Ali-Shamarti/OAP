package view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List; // Ensure this import for generic Lists


import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import controller.AddCustomerButtonListener;
import controller.DeleteCustomerButtonListener;
import controller.CustomerHandler;
import controller.SearchCustomerButtonListener;
import controller.UpdateCustomerButtonListener;
import model.Customer;
import model.CustomerDAO;
import model.ProductDAO;




public class CustomerView extends MainView {
	
	private CustomerDAO customerDAO;
	 private CustomerHandler customerHandler;


    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;

    public CustomerView() {
        super();
        this.customerDAO = new CustomerDAO();
        this.customerHandler = new CustomerHandler(this, this.customerDAO);
        

        setLayout(new BorderLayout());
        initializeUI();
        fetchAndDisplayCustomers();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 500);
        setLocationRelativeTo(null);
        pack(); // Adjusts the frame to fit the components
        setVisible(true); // Make sure the frame is visible
    }

    private void initializeUI() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131));
        JLabel titleLabel = new JLabel("Customer Management");
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
        String[] columnNames = {"Customer Number", "Company Name", "Contact Last Name", "Contact First Name", "Phone",
                "Address Line 1", "Address Line 2", "City", "State", "Postal Code", "Country",
                "Sales Rep Employee Nr", "Credit Limit"};
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

        JButton searchButton = createButton("Search", new SearchCustomerButtonListener(this, customerDAO));
        JButton addButton = createButton("Add", new AddCustomerButtonListener(this, customerDAO));
        JButton editButton = createButton("Edit", new UpdateCustomerButtonListener(this, customerDAO));
        JButton deleteButton = createButton("Delete", new DeleteCustomerButtonListener(this, customerDAO));
        JButton saveButton = createButton("Save to File", new SaveCustomerButtonListener());

        controlPanel.add(searchButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);
        controlPanel.add(saveButton);

        JPanel buttonPanelHolder = new JPanel(new BorderLayout());
        buttonPanelHolder.add(controlPanel, BorderLayout.NORTH);
        buttonPanelHolder.add(Box.createVerticalStrut(10), BorderLayout.CENTER); // Add space
        this.add(buttonPanelHolder, BorderLayout.SOUTH);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.addActionListener(listener);
        return button;
    }
    
  

    public Customer gatherUserInputForAddCustomer() {
        // Define fields for customer details
        JTextField customerNumberField = new JTextField(10);
        JTextField customerNameField = new JTextField(10);
        JTextField contactLastNameField = new JTextField(10);
        JTextField contactFirstNameField = new JTextField(10);
        JTextField phoneField = new JTextField(10);
        JTextField addressLine1Field = new JTextField(10);
        JTextField addressLine2Field = new JTextField(10);
        JTextField cityField = new JTextField(10);
        JTextField stateField = new JTextField(10);
        JTextField postalCodeField = new JTextField(10);
        JTextField countryField = new JTextField(10);
        JTextField creditLimitField = new JTextField(10);

        // Fetch the list of Sales Rep Employee Numbers
        CustomerDAO customerDAO = new CustomerDAO();
        List<Integer> salesRepEmployeeNumbers = customerDAO.fetchSalesRepEmployeeNumbers();
        JComboBox<Integer> salesRepEmployeeNumberField = new JComboBox<>(salesRepEmployeeNumbers.toArray(new Integer[0]));

        // Panel for the form
        JPanel panel = new JPanel(new GridLayout(0, 2));
        panel.add(new JLabel("Customer Number:")); panel.add(customerNumberField);
        panel.add(new JLabel("Company Name:")); panel.add(customerNameField);
        panel.add(new JLabel("Contact Last Name:")); panel.add(contactLastNameField);
        panel.add(new JLabel("Contact First Name:")); panel.add(contactFirstNameField);
        panel.add(new JLabel("Phone:")); panel.add(phoneField);
        panel.add(new JLabel("Address Line 1:")); panel.add(addressLine1Field);
        panel.add(new JLabel("Address Line 2:")); panel.add(addressLine2Field);
        panel.add(new JLabel("City:")); panel.add(cityField);
        panel.add(new JLabel("State:")); panel.add(stateField);
        panel.add(new JLabel("Postal Code:")); panel.add(postalCodeField);
        panel.add(new JLabel("Country:")); panel.add(countryField);
        panel.add(new JLabel("Sales Rep Employee Number:")); panel.add(salesRepEmployeeNumberField);
        panel.add(new JLabel("Credit Limit:")); panel.add(creditLimitField);

        // Show confirm dialog with the form
        int result = JOptionPane.showConfirmDialog(null, panel, "Enter New Customer Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int customerNumber = Integer.parseInt(customerNumberField.getText());
                String customerName = customerNameField.getText();
                String contactLastName = contactLastNameField.getText();
                String contactFirstName = contactFirstNameField.getText();
                String phone = phoneField.getText();
                String addressLine1 = addressLine1Field.getText();
                String addressLine2 = addressLine2Field.getText();
                String city = cityField.getText();
                String state = stateField.getText();
                String postalCode = postalCodeField.getText();
                String country = countryField.getText();
                int salesRepEmployeeNumber = (int) salesRepEmployeeNumberField.getSelectedItem();
                BigDecimal creditLimit = new BigDecimal(creditLimitField.getText());

                // Assuming the existence of a constructor in Customer class that takes all these fields
                return new Customer(customerNumber, customerName, contactLastName, contactFirstName, 
                                    phone, addressLine1, addressLine2, city, state, 
                                    postalCode, country, salesRepEmployeeNumber, creditLimit);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid input format.");
                return null;
            }
        }
        return null; // Return null if the user cancels or an error occurs
    }
    
    

    public void gatherUserInputForUpdateCustomer(Customer customer) {
       
    	JTextField customerNameField = new JTextField(customer.getCustomerName(), 10);
        JTextField contactLastNameField = new JTextField(customer.getContactLastName(), 10);
        JTextField contactFirstNameField = new JTextField(customer.getContactFirstName(), 10);
        JTextField phoneField = new JTextField(customer.getPhone(), 10);
        JTextField addressLine1Field = new JTextField(customer.getAddressLine1(), 10);
        JTextField addressLine2Field = new JTextField(customer.getAddressLine2(), 10);
        JTextField cityField = new JTextField(customer.getCity(), 10);
        JTextField stateField = new JTextField(customer.getState(), 10);
        JTextField postalCodeField = new JTextField(customer.getPostalCode(), 10);
        JTextField countryField = new JTextField(customer.getCountry(), 10);
        JTextField creditLimitField = new JTextField(customer.getCreditLimit().toString(), 10);
        
        CustomerDAO customerDAO = new CustomerDAO();

        // Fetch the list of Sales Rep Employee Numbers
        List<Integer> salesRepEmployeeNumbers = customerDAO.fetchSalesRepEmployeeNumbers();
        JComboBox<Integer> salesRepEmployeeNumberField = new JComboBox<>(salesRepEmployeeNumbers.toArray(new Integer[0]));
        salesRepEmployeeNumberField.setSelectedItem(customer.getSalesRepEmployeeNumber());

        JPanel panel = new JPanel(new GridLayout(0, 2));

        panel.add(new JLabel("Company Name:"));
        panel.add(customerNameField);
        panel.add(new JLabel("Contact Last Name:"));
        panel.add(contactLastNameField);
        panel.add(new JLabel("Contact First Name:"));
        panel.add(contactFirstNameField);
        panel.add(new JLabel("Phone:"));
        panel.add(phoneField);
        panel.add(new JLabel("Address Line 1:"));
        panel.add(addressLine1Field);
        panel.add(new JLabel("Address Line 2:"));
        panel.add(addressLine2Field);
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(new JLabel("State:"));
        panel.add(stateField);
        panel.add(new JLabel("Postal Code:"));
        panel.add(postalCodeField);
        panel.add(new JLabel("Country:"));
        panel.add(countryField);
        panel.add(new JLabel("Sales Rep Employee Number:"));
        panel.add(salesRepEmployeeNumberField);
        panel.add(new JLabel("Credit Limit:"));
        panel.add(creditLimitField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Edit Customer Details", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String customerName = customerNameField.getText();
            String contactLastName = contactLastNameField.getText();
            String contactFirstName = contactFirstNameField.getText();
            String phone = phoneField.getText();
            String addressLine1 = addressLine1Field.getText();
            String addressLine2 = (addressLine2Field.getText().isEmpty()) ? null : addressLine2Field.getText();
            String city = cityField.getText();
            String state = stateField.getText();
            String postalCode = postalCodeField.getText();
            String country = countryField.getText();
            int salesRepEmployeeNumber = (int) salesRepEmployeeNumberField.getSelectedItem();
            BigDecimal creditLimit = new BigDecimal(creditLimitField.getText());

            CustomerDAO handler = new CustomerDAO();
            boolean success = handler.editCustomer(customer.getCustomerNumber(), customerName, contactLastName, contactFirstName, 
            phone, addressLine1, addressLine2, city, state, postalCode, country, 
            salesRepEmployeeNumber, creditLimit);
            if (success) {
                JOptionPane.showMessageDialog(CustomerView.this, "Customer updated successfully!");
            } else {
                JOptionPane.showMessageDialog(CustomerView.this, "Failed to update customer.");
            }
        }
    }

   
    public Integer gatherUserInputForDeleteCustomer() {
        String customerNumberStr = JOptionPane.showInputDialog(this, "Enter Customer Number to delete:");
        if (customerNumberStr != null && !customerNumberStr.isEmpty()) {
            try {
                int customerNumber = Integer.parseInt(customerNumberStr);

                CustomerDAO customerDAO = new CustomerDAO();
                Customer customer = customerDAO.fetchCustomerData(customerNumber);

                if (customer != null) {
                    int confirm = JOptionPane.showConfirmDialog(
                        this, 
                        "Are you sure you want to delete this customer?\n" +
                        "Customer Nr: " + customer.getCustomerNumber() + "\n" +
                        "Name: " + customer.getCustomerName() + "\n" +
                        "Contact: " + customer.getContactFirstName() + " " + customer.getContactLastName(), 
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        return customerNumber; // Return customer number to delete
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Customer not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid customer number format.");
            }
        }
        return null; 
    }


    public String gatherUserInputForSearch() {
        JTextField searchField = new JTextField(20);
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Enter search criteria:"));
        panel.add(searchField);

        int result = JOptionPane.showConfirmDialog(this, panel, 
                                                   "Search Customers", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            return searchField.getText().trim();
        }
        return null;
    }

        
        public void updateTableWithSearchResults(List<Customer> searchResults) {
            tableModel.setRowCount(0); // Clear existing rows

            for (Customer customer : searchResults) {
                Object[] row = {
                    customer.getCustomerNumber(),
                    customer.getCustomerName(),
                    customer.getContactLastName(),
                    customer.getContactFirstName(),
                    customer.getPhone(),
                    customer.getAddressLine1(),
                    customer.getAddressLine2(),
                    customer.getCity(),
                    customer.getState(),
                    customer.getPostalCode(),
                    customer.getCountry(),
                    customer.getSalesRepEmployeeNumber(),
                    customer.getCreditLimit()
                };
                tableModel.addRow(row);
            }
        }
    
   private class SaveCustomerButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveCustomersToFile();
        }
    };
    
    private void saveCustomersToFile() {
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
                List<String[]> customers = customerDAO.fetchCustomers(); // Fetch customer data

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
    public void fetchAndDisplayCustomers() {
        CustomerDAO customerDAO = new CustomerDAO();
        List<String[]> customers = customerDAO.fetchCustomers();
        tableModel.setRowCount(0); // Clear the existing rows

        for (String[] customer : customers) {
            tableModel.addRow(customer); // Add row to the table model
        }
    }
}




