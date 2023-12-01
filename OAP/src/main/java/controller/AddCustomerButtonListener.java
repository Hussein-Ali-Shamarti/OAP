package controller;

import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.CustomerDAO;
import model.Customer;
import view.CustomerView;


public class AddCustomerButtonListener implements ActionListener {
	private final CustomerView customerView;

    public AddCustomerButtonListener(CustomerView customerView) {
        this.customerView = customerView;
        
    }
    
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
                    boolean success = CustomerDAO.addCustomer(customerNumber, customerName, contactLastName, contactFirstName, 
                                                                 phone, addressLine1, addressLine2, city, state, postalCode, country, 
                                                                 salesRepEmployeeNumber, creditLimit);
                    if (success) {
                        JOptionPane.showMessageDialog(customerView, "Customer added successfully!");
                    } else {
                        JOptionPane.showMessageDialog(customerView, "Failed to add customer.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(customerView, "Invalid input format.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(customerView, "Error: " + ex.getMessage());
                }
            }
        }
    }