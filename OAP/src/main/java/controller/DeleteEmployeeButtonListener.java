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
        String employeeNumberStr = JOptionPane.showInputDialog(employeeView, "Enter Employee Number to delete:");
        if (employeeNumberStr != null && !employeeNumberStr.isEmpty()) {
            try {
                int employeeNumber = Integer.parseInt(employeeNumberStr);

                EmployeeDAO handler = new EmployeeDAO();
                Employee employee = handler.fetchEmployeeData(employeeNumber);

                if (employee != null) {
                    int confirm = JOptionPane.showConfirmDialog(employeeView, 
                        "Are you sure you want to delete this employee?\n" +
                        "Employee Nr: " + employee.getEmployeeNumber() + "\n" +
                        "Name: " + employee.getFirstName() + " " + employee.getLastName(), 
                        "Confirm Deletion", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean success = handler.removeEmployeeFromDatabase("employees", employeeNumber);
                        if (success) {
                            JOptionPane.showMessageDialog(employeeView, "Employee deleted successfully.");
                        } else {
                            JOptionPane.showMessageDialog(employeeView, "Failed to delete employee.");
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(employeeView, "Employee not found.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(employeeView, "Invalid employee number format.");
            }
        }
    } 
}