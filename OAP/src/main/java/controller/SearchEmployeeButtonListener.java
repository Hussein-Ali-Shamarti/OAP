package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


import model.Employee;
import model.EmployeeDAO;
import view.EmployeeView;

// Action listener for "Search" button
public class SearchEmployeeButtonListener implements ActionListener {
    private EmployeeView employeeView;
    private EmployeeDAO employeeDAO;

    public SearchEmployeeButtonListener(EmployeeView employeeView, EmployeeDAO employeeDAO) {
        this.employeeView = employeeView;
        this.employeeDAO = employeeDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JTextField searchField = new JTextField(20);
        JPanel panel = new JPanel();
        panel.add(new JLabel("Search Employees:"));
        panel.add(searchField);

        int result = JOptionPane.showConfirmDialog(employeeView, panel, 
                                                   "Search Employees", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String searchCriteria = searchField.getText().trim();
            List<Employee> searchResults = employeeDAO.searchEmployees(searchCriteria);
            employeeView.updateTableWithSearchResults(searchResults);
        }
    }
}
