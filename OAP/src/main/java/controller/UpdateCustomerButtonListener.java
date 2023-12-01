package controller;
import javax.swing.*;

import model.Customer;
import model.CustomerDAO;
import view.CustomerView;



import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UpdateCustomerButtonListener implements ActionListener {
	private CustomerView customerView;
	private CustomerDAO customerDAO;
	
	
	 public UpdateCustomerButtonListener(CustomerView customerView, CustomerDAO customerDAO) {
        this.customerView = customerView;
        this.customerDAO = customerDAO;
        
    }
    
  
        @Override
        public void actionPerformed(ActionEvent e) {
            String customerNumberStr = JOptionPane.showInputDialog(customerView, "Enter Customer Number to edit:");
            if (customerNumberStr != null && !customerNumberStr.isEmpty()) {
                try {
                    int customerNumber = Integer.parseInt(customerNumberStr);

                    CustomerDAO handler = new CustomerDAO();
                    Customer customer = handler.fetchCustomerData(customerNumber);

                    if (customer != null) {
                        customerView.displayEditForm(customer);
                    } else {
                        JOptionPane.showMessageDialog(customerView, "Customer not found.");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(customerView, "Invalid customer number format.");
                }
            }
        }
    
}