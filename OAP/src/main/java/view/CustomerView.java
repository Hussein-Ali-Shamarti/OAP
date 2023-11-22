package view;

import javax.swing.*;
import controller.CustomerHandler;
import model.Customer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
        pack(); 
        setVisible(true); 
    }

    private void initializeUI() {
        add(createTitlePanel(), BorderLayout.NORTH);
        setupTable();
        add(new JScrollPane(table), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
    }

    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131));
        titlePanel.add(createLabel("Customer Management", 20, Color.WHITE));
        return titlePanel;
    }

    private JLabel createLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setForeground(color);
        return label;
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

    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        controlPanel.setBorder(new EmptyBorder(15, 25, 15, 25));
        controlPanel.setBackground(new Color(90, 23, 139));

        controlPanel.add(createButton("Search", new SearchButtonListener()));
        controlPanel.add(createButton("Add", new AddButtonListener()));
        controlPanel.add(createButton("Edit", new UpdateButtonListener()));
        controlPanel.add(createButton("Delete", new DeleteButtonListener()));

        return controlPanel;
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setBackground(Color.WHITE);
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

    // Action listener for "Add" button
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showCustomerForm(null, "Add New Customer", true);
        }
    }

    // Action listener for "Edit" button
    private class UpdateButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int customerNumber = promptForCustomerNumber("Enter Customer Number to edit:");
            if (customerNumber > 0) {
                CustomerHandler handler = new CustomerHandler();
                Customer customer = handler.fetchCustomerData(customerNumber);
                if (customer != null) {
                    showCustomerForm(customer, "Edit Customer Details", false);
                } else {
                    showMessage("Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    // Action listener for "Delete" button
    private class DeleteButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int customerNumber = promptForCustomerNumber("Enter Customer Number to delete:");
            if (customerNumber > 0) {
                CustomerHandler handler = new CustomerHandler();
                Customer customer = handler.fetchCustomerData(customerNumber);
                if (customer != null && confirmDeletion(customer)) {
                    if (handler.deleteCustomer(customerNumber)) {
                        showMessage("Customer deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        showMessage("Failed to delete customer.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }

    // Action listener for "Search" button
    private class SearchButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Implement search functionality here
        }
    }

    private int promptForCustomerNumber(String message) {
        String input = JOptionPane.showInputDialog(this, message);
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException ex) {
            showMessage("Invalid customer number format.", "Error", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
    }

    private void showCustomerForm(Customer customer, String title, boolean isAddMode) {
        JPanel panel = createCustomerFormPanel(customer, isAddMode);
        if (JOptionPane.showConfirmDialog(null, panel, title, JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            // Extract data from form fields and call the appropriate handler method
        }
    }

    private JPanel createCustomerFormPanel(Customer customer, boolean isAddMode) {
        JPanel panel = new JPanel(new GridLayout(0, 2));
        // Add form fields to panel
        // If isAddMode is false, initialize fields with 'customer' data
        return panel;
    }

    private boolean confirmDeletion(Customer customer) {
        return JOptionPane.showConfirmDialog(
            this, 
            "Are you sure you want to delete this customer?\n" +
            "Customer Nr: " + customer.getCustomerNumber() + "\n" +
            "Name: " + customer.getCustomerName() + "\n" +
            "Contact: " + customer.getContactFirstName() + " " + customer.getContactLastName(), 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    private void showMessage(String message, String title, int messageType) {
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }

   
    }



