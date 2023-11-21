package view;

import javax.swing.*;

import controller.CustomerHandler;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

public class CustomerView extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CustomerView(ActionListener customersListener) {
        // Set title
        super("Customer Management");

        // Set layout for the frame
        setLayout(new BorderLayout());

        // Set background color
        getContentPane().setBackground(Color.WHITE);

        // Create JLabel
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(84, 11, 131)); // Purple background color
        JLabel titleLabel = new JLabel("Customer Management");
        titleLabel.setBackground(new Color(84, 11, 131)); // Purple background color
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE); // White text color
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titlePanel.add(titleLabel);

        // Create buttons with listeners
        JButton addButton = createButton("Add New", new AddButtonListener());
        JButton updateButton = createButton("Update", new UpdateButtonListener());
        JButton deleteButton = createButton("Delete", new DeleteButtonListener());
        JButton searchButton = createButton("Search", new SearchButtonListener());

        // Create JPanel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);

        // Create JTable and JScrollPane
        String[] columnNames = {"Column 1", "Column 2", "Column 3"}; // Replace with actual column names
        Object[][] data = {{"Data 1", "Data 2", "Data 3"}, {"Data 4", "Data 5", "Data 6"}}; // Replace with actual data
        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);

        // Add components to the frame
        add(titlePanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);

        // Set frame properties
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null); // Center on screen

        // Set the CustomersListener for buttons
        addButton.addActionListener(customersListener);
        updateButton.addActionListener(customersListener);
        deleteButton.addActionListener(customersListener);
        searchButton.addActionListener(customersListener);
    }

    private JButton createButton(String text, ActionListener listener) {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK); // White text color
        button.setBackground(new Color(84, 11, 131)); // Purple background color
        button.setFocusPainted(false); // Remove focus highlighting for better appearance
        button.addActionListener(listener); // Add the listener
        return button;
    }

    // Action listener for "Add New" button
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JTextField customerNumberField = new JTextField(10);
            JTextField companyNameField = new JTextField(10);
            JTextField contactLastNameField = new JTextField(10);
            JTextField contactFirstNameField = new JTextField(10);
            JTextField salesRepEmployeeNumberField = new JTextField(10);
            JTextField creditLimitField = new JTextField(10);

            JPanel panel = new JPanel(new GridLayout(0, 2));
            panel.add(new JLabel("Customer Number:"));
            panel.add(customerNumberField);
            panel.add(new JLabel("Company Name:"));
            panel.add(companyNameField);
            panel.add(new JLabel("Contact Last Name:"));
            panel.add(contactLastNameField);
            panel.add(new JLabel("Contact First Name:"));
            panel.add(contactFirstNameField);
            panel.add(new JLabel("Sales Rep Employee Number:"));
            panel.add(salesRepEmployeeNumberField);
            panel.add(new JLabel("Credit Limit:"));
            panel.add(creditLimitField);

            int result = JOptionPane.showConfirmDialog(null, panel, 
                    "Enter New Customer Details", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int customerNr = Integer.parseInt(customerNumberField.getText());
                    String companyName = companyNameField.getText();
                    String contactLastName = contactLastNameField.getText();
                    String contactFirstName = contactFirstNameField.getText();
                    int salesRepEmployeeNr = Integer.parseInt(salesRepEmployeeNumberField.getText());
                    BigDecimal creditLimit = new BigDecimal(creditLimitField.getText());

                    boolean success = CustomerHandler.addCustomer(customerNr, companyName, contactLastName, contactFirstName, salesRepEmployeeNr, creditLimit);
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
