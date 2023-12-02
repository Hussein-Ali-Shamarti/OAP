package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JOptionPane;


import model.Employee;
import model.EmployeeDAO;
import view.EmployeeView;




//Action listener for "Delete" button
public class DeleteEmployeeButtonListener implements ActionListener {

    private EmployeeView employeeView;
    private EmployeeDAO employeeDAO;

    public DeleteEmployeeButtonListener(EmployeeView employeeView, EmployeeDAO employeeDAO) {
        this.employeeView = employeeView;
        this.employeeDAO = employeeDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Integer employeeNumberToDelete = employeeView.gatherUserInputForDelete();

        if (employeeNumberToDelete != null) {
            boolean success = employeeDAO.removeEmployeeFromDatabase("employees", employeeNumberToDelete);

            if (success) {
                JOptionPane.showMessageDialog(employeeView, "Employee deleted successfully.");
            } else {
                JOptionPane.showMessageDialog(employeeView, "Failed to delete employee.");
            }
        }
    }
}
