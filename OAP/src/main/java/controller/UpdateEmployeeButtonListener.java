package controller;
import javax.swing.*;

import model.Employee;
import model.EmployeeDAO;
import view.EmployeeView;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;

public class UpdateEmployeeButtonListener implements ActionListener {
    private EmployeeView employeeView;
    private EmployeeDAO employeeDAO;

    public UpdateEmployeeButtonListener(EmployeeView employeeView, EmployeeDAO employeeDAO) {
        this.employeeView = employeeView;
        this.employeeDAO = employeeDAO;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String employeeNumberStr = JOptionPane.showInputDialog(employeeView, "Enter Employee Number to edit:");
        if (employeeNumberStr != null && !employeeNumberStr.isEmpty()) {
            try {
                int employeeNumber = Integer.parseInt(employeeNumberStr);
                Employee employee = employeeDAO.fetchEmployeeData(employeeNumber);
                if (employee != null) {
                    Employee updatedEmployee = employeeView.gatherUserInputForUpdateEmployee(employee);
                    if (updatedEmployee != null) {
                        boolean success = employeeDAO.editEmployeeInDatabase(
                            "employees", // Assuming the table name is provided here
                            updatedEmployee.getEmployeeNumber(),
                            updatedEmployee.getFirstName(),
                            updatedEmployee.getLastName(),
                            updatedEmployee.getExtension(),
                            updatedEmployee.getEmail(),
                            updatedEmployee.getOfficeCode(),
                            updatedEmployee.getReportsTo(),
                            updatedEmployee.getJobTitle()
                        );
                        if (success) {
                            JOptionPane.showMessageDialog(employeeView, "Employee updated successfully!");
                        } else {
                            JOptionPane.showMessageDialog(employeeView, "Failed to update employee.");
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

