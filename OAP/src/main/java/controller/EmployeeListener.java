package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import view.CustomerView;

public class EmployeeListener {
	
    private static class CustomersListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Open CustomerView when "Customers" button is pressed
            SwingUtilities.invokeLater(() -> {
                CustomerView customerView = new CustomerView();
                customerView.setVisible(true);
               
            });
        }
    }

