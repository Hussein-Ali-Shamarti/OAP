package controller;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



import javax.swing.JOptionPane;


import model.CustomerDAO;
import model.Customer;
import view.CustomerView;




import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class AddCustomerButtonListener implements ActionListener {
    private final CustomerView customerView;
    private final CustomerDAO customerDAO;

    public AddCustomerButtonListener(CustomerView customerView, CustomerDAO customerDAO) {
        this.customerView = customerView;
        this.customerDAO = customerDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Customer customer = customerView.gatherUserInputForAddCustomer();

        if (customer != null) {
            boolean success = customerDAO.addCustomer(customer);

            if (success) {
                JOptionPane.showMessageDialog(customerView, "Customer added successfully!");
            } else {
                JOptionPane.showMessageDialog(customerView, "Failed to add customer.");
            }
        }
    }
}
