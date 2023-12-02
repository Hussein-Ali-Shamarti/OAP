package controller;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import model.CustomerDAO;
import view.CustomerView;

public class DeleteCustomerButtonListener implements ActionListener {
    private CustomerView customerView;
    private CustomerDAO customerDAO;

    public DeleteCustomerButtonListener(CustomerView customerView, CustomerDAO customerDAO) {
        this.customerView = customerView;
        this.customerDAO = customerDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer customerNumberToDelete = customerView.gatherUserInputForDeleteCustomer();

        if (customerNumberToDelete != null) {
            boolean success = customerDAO.deleteCustomer(customerNumberToDelete);

            if (success) {
                JOptionPane.showMessageDialog(customerView, "Customer deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(customerView, "Failed to delete customer.");
            }
        }
    }
}