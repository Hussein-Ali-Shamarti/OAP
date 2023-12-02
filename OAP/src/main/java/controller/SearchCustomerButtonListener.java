package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

import model.Customer;
import model.CustomerDAO;
import view.CustomerView;

import java.util.List;

public class SearchCustomerButtonListener implements ActionListener {
    private CustomerView customerView;
    private CustomerDAO customerDAO;

    public SearchCustomerButtonListener(CustomerView customerView, CustomerDAO customerDAO) {
        this.customerView = customerView;
        this.customerDAO = customerDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String searchCriteria = customerView.gatherUserInputForSearch();

        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            List<Customer> searchResults = customerDAO.searchCustomers(searchCriteria);
            customerView.updateTableWithSearchResults(searchResults);
        }
    }
}
