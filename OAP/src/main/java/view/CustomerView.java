package view;

import javax.swing.*;


import controller.CustomerHandler;
import model.Customer;

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



public class CustomerView extends MainView {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;

    public CustomerView() {
        super();

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

        JButton searchButton = createButton("Search", new SearchButtonListener());
        JButton addButton = createButton("Add", new AddButtonListener());
        JButton editButton = createButton("Edit", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());
        JButton saveButton = createButton("Save to File", new SaveButtonListener());

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
    
    List<String[]> fetchAndDisplayCustomers() {
        List<String[]> customers = new ArrayList<>();
        tableModel.setRowCount(0); // Clear the existing rows

        try (Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String sql = "SELECT customerNumber, customerName, contactLastName, contactFirstName, " +
                         "phone, addressLine1, addressLine2, city, state, postalCode, country, " +
                         "salesRepEmployeeNumber, creditLimit FROM customers";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                String[] customer = {
                    resultSet.getString("customerNumber"),
                    resultSet.getString("customerName"),
                    resultSet.getString("contactLastName"),
                    resultSet.getString("contactFirstName"),
                    resultSet.getString("phone"),
                    resultSet.getString("addressLine1"),
                    resultSet.getString("addressLine2"),
                    resultSet.getString("city"),
                    resultSet.getString("state"),
                    resultSet.getString("postalCode"),
                    resultSet.getString("country"),
                    resultSet.getString("salesRepEmployeeNumber"),
                    resultSet.getString("creditLimit")
                };
                tableModel.addRow(customer); // Add row to the table model
                customers.add(customer); // Add to the list
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error fetching customer data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
        return customers;
    }


    
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Fields for customer details
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
            JTextField salesRepEmployeeNumberField = new JTextField(10);
            JTextField creditLimitField = new JTextField(10);

            // Panel for the form
            JPanel panel = new JPanel(new GridLayout(0, 2));

            // Adding labels and text fields to the panel
            panel.add(new JLabel("Customer Number:"));
            panel.add(customerNumberField);
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

            // Show confirm dialog with the form
            int result = JOptionPane.showConfirmDialog(null, panel, "Enter New Customer Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    // Retrieving values from text fields
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
                    int salesRepEmployeeNumber = Integer.parseInt(salesRepEmployeeNumberField.getText());
                    BigDecimal creditLimit = new BigDecimal(creditLimitField.getText());

                    // Call to CustomerHandler to add customer
                    boolean success = CustomerHandler.addCustomer(customerNumber, customerName, contactLastName, contactFirstName, 
                                                                 phone, addressLine1, addressLine2, city, state, postalCode, country, 
                                                                 salesRepEmployeeNumber, creditLimit);
                    if (success) {
                        JOptionPane.showMessageDialog(CustomerView.this, "Customer added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(CustomerView.this, "Failed to add customer.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CustomerView.this, "Invalid input format.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(CustomerView.this, "Error: " + ex.getMessage());
                }
            }
        }
    }


    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String customerNumberStr = JOptionPane.showInputDialog(CustomerView.this, "Enter Customer Number to edit:");
            if (customerNumberStr != null && !customerNumberStr.isEmpty()) {
                try {
                    int customerNumber = Integer.parseInt(customerNumberStr);

                    CustomerHandler handler = new CustomerHandler();
                    Customer customer = handler.fetchCustomerData(customerNumber);

                    if (customer != null) {
                        displayEditForm(customer);
                    } else {
                        JOptionPane.showMessageDialog(CustomerView.this, "Customer not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CustomerView.this, "Invalid customer number format.");
                }
            }
        }
    }

    private void displayEditForm(Customer customer) {
        
        JTextField customerNameField = new JTextField(customer.getCustomerName(), 10);
        JTextField contactLastNameField = new JTextField(customer.getContactLastName(), 10);
        JTextField contactFirstNameField = new JTextField(customer.getContactFirstName(), 10);
        JTextField phoneField = new JTextField(customer.getphone(), 10);
        JTextField addressLine1Field = new JTextField(customer.getaddressLine1(), 10);
        JTextField addressLine2Field = new JTextField(customer.getaddressLine2(), 10);
        JTextField cityField = new JTextField(customer.getcity(), 10);
        JTextField stateField = new JTextField(customer.getstate(), 10);
        JTextField postalCodeField = new JTextField(customer.getPostalCode(), 10);
        JTextField countryField = new JTextField(customer.getcountry(), 10);
        JTextField salesRepEmployeeNumberField = new JTextField(String.valueOf(customer.getSalesRepEmployeeNumber()), 10);
        JTextField creditLimitField = new JTextField(customer.getCreditLimit().toString(), 10);

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
                	String addressLine2 = (addressLine2Field.getText().isEmpty()) ? null : addressLine2Field.getText(); // assuming addressLine2 can be null
                	String city = cityField.getText();
                	String state = stateField.getText();
                	String postalCode = postalCodeField.getText();
                	String country = countryField.getText();
                	int salesRepEmployeeNumber = Integer.parseInt(salesRepEmployeeNumberField.getText());
                	BigDecimal creditLimit = new BigDecimal(creditLimitField.getText());

            CustomerHandler handler = new CustomerHandler();
            boolean success = handler.editCustomer(customer.getCustomerNumber(), customerName, contactLastName, contactFirstName, 
            phone, addressLine1, addressLine2, city, state, postalCode, country, 
            salesRepEmployeeNumber, creditLimit); // other parameters
            if (success) {
                JOptionPane.showMessageDialog(CustomerView.this, "Customer updated successfully!");
            } else {
                JOptionPane.showMessageDialog(CustomerView.this, "Failed to update customer.");
            }
        }
    }

    // Action listener for "Delete" button
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String customerNumberStr = JOptionPane.showInputDialog(CustomerView.this, "Enter Customer Number to delete:");
            if (customerNumberStr != null && !customerNumberStr.isEmpty()) {
                try {
                    int customerNumber = Integer.parseInt(customerNumberStr);

                    CustomerHandler handler = new CustomerHandler();
                    Customer customer = handler.fetchCustomerData(customerNumber);

                    if (customer != null) {
                        int confirm = JOptionPane.showConfirmDialog(
                            CustomerView.this, 
                            "Are you sure you want to delete this customer?\n" +
                            "Customer Nr: " + customer.getCustomerNumber() + "\n" +
                            "Name: " + customer.getCustomerName() + "\n" +
                            "Contact: " + customer.getContactFirstName() + " " + customer.getContactLastName(), 
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                        if (confirm == JOptionPane.YES_OPTION) {
                            boolean success = handler.deleteCustomer(customerNumber);
                            if (success) {
                                JOptionPane.showMessageDialog(CustomerView.this, "Customer deleted successfully.");
                            } else {
                                JOptionPane.showMessageDialog(CustomerView.this, "Failed to delete customer.");
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(CustomerView.this, "Customer not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(CustomerView.this, "Invalid customer number format.");
                }
            }
        }
    }

    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField searchField = new JTextField(20);
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new JLabel("Enter search criteria:"));
            panel.add(searchField);
            
            int result = JOptionPane.showConfirmDialog(CustomerView.this, panel, 
                                                       "Search Customers", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                String searchCriteria = searchField.getText().trim();
                List<Customer> searchResults = new CustomerHandler().searchCustomers(searchCriteria);
                updateTableWithSearchResults(searchResults);
            }
        }
        
        private void updateTableWithSearchResults(List<Customer> searchResults) {
            tableModel.setRowCount(0); // Clear existing rows

            for (Customer customer : searchResults) {
                Object[] row = {
                    customer.getCustomerNumber(),
                    customer.getCustomerName(),
                    customer.getContactLastName(),
                    customer.getContactFirstName(),
                    customer.getphone(),
                    customer.getaddressLine1(),
                    customer.getaddressLine2(),
                    customer.getcity(),
                    customer.getstate(),
                    customer.getPostalCode(),
                    customer.getcountry(),
                    customer.getSalesRepEmployeeNumber(),
                    customer.getCreditLimit()
                };
                tableModel.addRow(row);
            }
        }
    }
   private class SaveButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            saveCustomersToFile();
        }
    };
    
    private void saveCustomersToFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getAbsolutePath().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".txt");
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave))) {
                List<String[]> customers = fetchAndDisplayCustomers(); // Fetch customer data
                for (String[] customer : customers) {
                    writer.write(String.join(", ", customer));
                    writer.newLine();
                }
                JOptionPane.showMessageDialog(null, "Customers saved successfully at " + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}




