package view;

import javax.swing.*;

import controller.CustomerHandler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomerView extends JFrame {

    private static final long serialVersionUID = 1L;
    private DefaultTableModel tableModel;
    private JTable table;

    public CustomerView() {
        super("Customer Management");

        setLayout(new BorderLayout());
        initializeUI();
        fetchAndDisplayCustomers();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
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

        controlPanel.add(searchButton);
        controlPanel.add(addButton);
        controlPanel.add(editButton);
        controlPanel.add(deleteButton);

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
    
    void fetchAndDisplayCustomers() {
        tableModel.setRowCount(0);
        try (Connection conn = database.DataBaseConnection.getConnection();
             Statement statement = conn.createStatement()) {
            String sql = "SELECT customerNumber, customerName, contactLastName, contactFirstName, " +
                    "phone, addressLine1, addressLine2, city, state, postalCode, country, " +
                    "salesRepEmployeeNumber, creditLimit FROM customers";
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                Object[] row = {
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
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error fetching customer data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Here ALbert is cooking
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Fields for customer details
            JTextField customerNumberField = new JTextField(10);
            JTextField companyNameField = new JTextField(10);
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
            panel.add(companyNameField);
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
                    int customerNr = Integer.parseInt(customerNumberField.getText());
                    String companyName = companyNameField.getText();
                    String contactLastName = contactLastNameField.getText();
                    String contactFirstName = contactFirstNameField.getText();
                    String phone = phoneField.getText();
                    String addressLine1 = addressLine1Field.getText();
                    String addressLine2 = addressLine2Field.getText();
                    String city = cityField.getText();
                    String state = stateField.getText();
                    String postalCode = postalCodeField.getText();
                    String country = countryField.getText();
                    int salesRepEmployeeNr = Integer.parseInt(salesRepEmployeeNumberField.getText());
                    BigDecimal creditLimit = new BigDecimal(creditLimitField.getText());

                    // Call to CustomerHandler to add customer
                    boolean success = CustomerHandler.addCustomer(customerNr, companyName, contactLastName, contactFirstName, 
                                                                 phone, addressLine1, addressLine2, city, state, postalCode, country, 
                                                                 salesRepEmployeeNr, creditLimit);
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


    // Action listener for "Update" button
    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(CustomerView.this, "Update button pressed");
        }
    }

    // Action listener for "Delete" button
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(CustomerView.this, "Delete button pressed");
        }
    }

    // Action listener for "Search" button
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(CustomerView.this, "Search button pressed");
        }
    }

   
 }
